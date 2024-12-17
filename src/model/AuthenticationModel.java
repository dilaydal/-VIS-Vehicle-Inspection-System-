package model;

import users.*;
import utils.AuthResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class AuthenticationModel {
    private final Connection connection;

    public AuthenticationModel(Connection connection) {
        this.connection = connection;
    }

    // Authenticate a user based on username and password
    public AuthResult authenticateUser(String username, String password) {
        try {
            User user;

            // Check Customers
            user = checkUser("Customers", username, password);
            if (user != null) return new AuthResult("customer", user);

            // Check Mechanics
            user = checkUser("Mechanics", username, password);
            if (user != null) return new AuthResult("mechanic", user);

            // Check Managers
            user = checkUser("Managers", username, password);
            if (user != null) return new AuthResult("manager", user);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Register a new customer
    public boolean registerCustomer(String username, String password, String fullName) throws SQLException {
        if (isUsernameTaken("Customers", username)) {
            return false; 
        }

        String hashedPassword = hashPassword(password);

        // Insert the customer into the Customers table
        String query = "INSERT INTO Customers (userName, password, fullName) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, fullName);
            return statement.executeUpdate() > 0; 
        }
    }

    // Check if a username is already taken
    private boolean isUsernameTaken(String tableName, String username) throws SQLException {
        String query = "SELECT 1 FROM " + tableName + " WHERE userName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); 
        }
    }

    // Validate username and password for login
    private User checkUser(String tableName, String username, String password) throws Exception {
        String query = "SELECT * FROM " + tableName + " WHERE userName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String storedPassword = result.getString("password");
    
                // Check if the stored password is already hashed
                boolean isHashed = storedPassword.length() == 64;
    
                if (isHashed) {
                    if (validatePassword(password, storedPassword)) {
                        return createUserFromResultSet(tableName, result);
                    }
                } else {
                    // Validate plaintext password and update it to hashed
                    if (password.equals(storedPassword)) {
                        String hashedPassword = hashPassword(password);
                        updatePasswordToHash(tableName, username, hashedPassword);
                        return createUserFromResultSet(tableName, result);
                    }
                }
            }
        }
        return null;
    }
    
    private void updatePasswordToHash(String tableName, String username, String hashedPassword) throws SQLException {
        String query = "UPDATE " + tableName + " SET password = ? WHERE userName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, hashedPassword);
            statement.setString(2, username);
            statement.executeUpdate();
            System.out.println("Updated password to hash for user: " + username + " in table: " + tableName);
        }
    }
    
    private User createUserFromResultSet(String tableName, ResultSet result) throws SQLException {
        if (tableName.equals("Customers")) {
            return new Customer(result.getInt("customerID"), result.getString("userName"));
        } else if (tableName.equals("Mechanics")) {
            return new Mechanic(result.getInt("mechanicID"), result.getString("userName"));
        } else if (tableName.equals("Managers")) {
            return new Admin(result.getInt("managerID"), result.getString("userName"));
        }
        return null;
    }

    private boolean validatePassword(String password, String storedHash) {
        String hashedPassword = hashPassword(password);
        return hashedPassword.equals(storedHash);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
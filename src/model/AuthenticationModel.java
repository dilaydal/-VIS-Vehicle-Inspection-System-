package model;

import users.*;
import utils.AuthResult;
import java.sql.*;

public class AuthenticationModel {
    public AuthResult authenticateUser(String username, String password) {
        try (Connection connection = DatabaseConnection.connect()) {
            User user;

            user = checkUser(connection, "Customers", username, password);
            if (user != null) return new AuthResult("customer", user);

            user = checkUser(connection, "Mechanics", username, password);
            if (user != null) return new AuthResult("mechanic", user);

            user = checkUser(connection, "Managers", username, password);
            if (user != null) return new AuthResult("manager", user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private User checkUser(Connection conn, String tableName, String username, String password) throws Exception {
        String query = "SELECT * FROM " + tableName + " WHERE userName = ? AND password = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    if (tableName.equals("Customers")) {
                        return new Customer(result.getInt("customerID"), result.getString("userName"));
                    } else if (tableName.equals("Mechanics")) {
                        return new Mechanic(result.getInt("mechanicID"), result.getString("userName"));
                    } else if (tableName.equals("Managers")) {
                        return new Admin(result.getInt("managerID"), result.getString("userName"));
                    }
                }
            }
        }
        return null;
    }
}
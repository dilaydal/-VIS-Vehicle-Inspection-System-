package View;

import javax.swing.*;

import model.DatabaseConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterFrame extends JFrame {
    public RegisterFrame() {
        setTitle("Register - Vehicle Inspection System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel fullNameLabel = new JLabel("Full Name:");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField fullNameField = new JTextField();
        JButton registerButton = new JButton("Register");

        usernameLabel.setBounds(50, 50, 100, 30);
        usernameField.setBounds(150, 50, 150, 30);
        passwordLabel.setBounds(50, 100, 100, 30);
        passwordField.setBounds(150, 100, 150, 30);
        fullNameLabel.setBounds(50, 150, 100, 30);
        fullNameField.setBounds(150, 150, 150, 30);
        registerButton.setBounds(150, 200, 100, 30);

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(fullNameLabel);
        add(fullNameField);
        add(registerButton);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String fullName = fullNameField.getText();


            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String hashedPassword = hashPassword(password);

            try (Connection connection = DatabaseConnection.connect()) {
                //need to be filled according to SQL code/queries
                //the username should be unique
                String checkQuery = "SELECT COUNT(*) FROM Customers WHERE userName = ?";
                try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, username);

                    try (ResultSet resultSet = checkStmt.executeQuery()) {
                        if (resultSet.next() && resultSet.getInt(1) > 0) {
                            JOptionPane.showMessageDialog(this, "Username already exists. Please choose another username.", "Error", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                }
                String insertQuery = "INSERT INTO Customers (userName, password, fullName) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, username);
                    insertStmt.setString(2, hashedPassword);
                    insertStmt.setString(3, fullName);

                    int rowsAffected = insertStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
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

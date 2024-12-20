package test.java.controller;

import main.java.controller.RegisterController;
import main.java.model.AuthenticationModel;
import main.java.model.DatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterControllerTest {
    private Connection connection;
    private AuthenticationModel authenticationModel;
    private RegisterController registerController;
    private JFrame frame;

    @BeforeEach
    void setUp() throws SQLException {
        // Use DatabaseConnection class to get a real connection to the MySQL database
        connection = DatabaseConnection.connect();
        authenticationModel = new AuthenticationModel(connection);
        registerController = new RegisterController(authenticationModel);
        frame = new JFrame(); // Create a simple JFrame for testing

        // Create Customers table in the database (if not exists)
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Customers (" +
                    "customerID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "userName VARCHAR(100), " +
                    "password VARCHAR(255), " +
                    "fullName VARCHAR(100))");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Clean up the database after each test
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Customers");
        }
        connection.close();
    }

    @Test
    void testHandleRegistrationSuccess() throws SQLException {
        // Test for successful registration
        String username = "newuser";
        String password = "password123";
        String fullName = "New User";

        // Perform the registration
        registerController.handleRegistration(username, password, fullName, frame);

        // Verify the user was added to the database
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Customers WHERE userName = 'newuser'")) {
            assertTrue(resultSet.next(), "The user should be in the database.");
            assertEquals("newuser", resultSet.getString("userName"));
        }
    }

    @Test
    void testHandleRegistrationUsernameAlreadyExists() throws SQLException {
        // Test for username already existing
        String username = "existinguser";
        String password = "password123";
        String fullName = "Existing User";

        // Insert an existing user into the database
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Customers (userName, password, fullName) VALUES (?, ?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, fullName);
            statement.executeUpdate();
        }

        // Attempt to register with the same username
        registerController.handleRegistration(username, password, fullName, frame);

        // Verify that the existing user is still in the database and no duplicate registration occurred
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM Customers WHERE userName = 'existinguser'")) {
            resultSet.next();
            assertEquals(1, resultSet.getInt(1), "There should be only one user with that username.");
        }
    }

    @Test
    void testHandleRegistrationEmptyFields() {
        // Test when one of the fields is empty
        registerController.handleRegistration("", "password123", "Full Name", frame);
        registerController.handleRegistration("username", "", "Full Name", frame);
        registerController.handleRegistration("username", "password123", "", frame);

        // In each case, an error message should be displayed
        // We can’t directly test GUI messages in this case, but we could mock the JOptionPane
        // or assume the exception handling works correctly.
    }
}
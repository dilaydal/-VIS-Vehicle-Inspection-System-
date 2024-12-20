package test.java.model;

import main.java.model.AuthenticationModel;
import main.java.model.DatabaseConnection;
import main.java.users.*;
import main.java.utils.AuthResult;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationModelTest {
    private Connection connection;
    private AuthenticationModel authenticationModel;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize database connection using the custom DatabaseConnection class
        connection = DatabaseConnection.connect();
        authenticationModel = new AuthenticationModel(connection);

        // Create tables for testing
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Customers (" +
                    "customerID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "userName VARCHAR(100), " +
                    "password VARCHAR(255), " +
                    "fullName VARCHAR(100))");
            statement.execute("CREATE TABLE IF NOT EXISTS Mechanics (" +
                    "mechanicID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "userName VARCHAR(100), " +
                    "password VARCHAR(255))");
            statement.execute("CREATE TABLE IF NOT EXISTS Managers (" +
                    "managerID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "userName VARCHAR(100), " +
                    "password VARCHAR(255))");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Clean up by dropping the test tables
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Customers");
            statement.execute("DROP TABLE IF EXISTS Mechanics");
            statement.execute("DROP TABLE IF EXISTS Managers");
        }
        connection.close();
    }

    @Test
    void testAuthenticateCustomerSuccess() throws SQLException {
        // Set up data for a valid customer
        String username = "customerUser";
        String password = "customerPassword";
        String fullName = "Customer Name";
        authenticationModel.registerCustomer(username, password, fullName);

        // Try to authenticate the customer
        AuthResult authResult = authenticationModel.authenticateUser(username, password);

        assertNotNull(authResult);
        assertEquals("customer", authResult.getType());
        assertTrue(authResult.getUser() instanceof Customer);
    }

    @Test
    void testAuthenticateMechanicSuccess() throws SQLException {
        // Set up data for a valid mechanic
        String username = "mechanicUser";
        String password = "mechanicPassword";
        String fullName = "Mechanic Name";
        String query = "INSERT INTO Mechanics (userName, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        }

        // Try to authenticate the mechanic
        AuthResult authResult = authenticationModel.authenticateUser(username, password);

        assertNotNull(authResult);
        assertEquals("mechanic", authResult.getType());
        assertTrue(authResult.getUser() instanceof Mechanic);
    }

    @Test
    void testAuthenticateManagerSuccess() throws SQLException {
        // Set up data for a valid manager
        String username = "managerUser";
        String password = "managerPassword";
        String fullName = "Manager Name";
        String query = "INSERT INTO Managers (userName, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        }

        // Try to authenticate the manager
        AuthResult authResult = authenticationModel.authenticateUser(username, password);

        assertNotNull(authResult);
        assertEquals("manager", authResult.getType());
        assertTrue(authResult.getUser() instanceof Admin);
    }

    @Test
    void testAuthenticateInvalidUser() {
        // Test invalid authentication
        AuthResult authResult = authenticationModel.authenticateUser("invalidUser", "wrongPassword");
        assertNull(authResult);
    }

    @Test
    void testRegisterCustomerSuccess() throws SQLException {
        // Test registering a customer with a new username
        String username = "newCustomer";
        String password = "newPassword";
        String fullName = "New Customer";

        boolean result = authenticationModel.registerCustomer(username, password, fullName);
        assertTrue(result);

        // Verify that the user is in the database
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Customers WHERE userName = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals(username, resultSet.getString("userName"));
        }
    }

    @Test
    void testRegisterCustomerUsernameTaken() throws SQLException {
        // Register a customer with the same username
        String username = "existingCustomer";
        String password = "existingPassword";
        String fullName = "Existing Customer";

        authenticationModel.registerCustomer(username, password, fullName);

        // Attempt to register again with the same username
        boolean result = authenticationModel.registerCustomer(username, "newPassword", "New Customer");
        assertFalse(result);
    }

    @Test
    void testPasswordHashing() throws SQLException {
        // Test password hashing and validation
        String username = "hashedUser";
        String password = "password123";
        String fullName = "Hashed User";

        authenticationModel.registerCustomer(username, password, fullName);

        // Fetch the hashed password from the database
        String query = "SELECT password FROM Customers WHERE userName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                assertNotEquals(password, storedPassword); // Ensure the password is hashed
            }
        }

        // Validate the password using the hash
        boolean isValid = authenticationModel.authenticateUser(username, password) != null;
        assertTrue(isValid);
    }
}

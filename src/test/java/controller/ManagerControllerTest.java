package test.java.controller;

import main.java.controller.ManagerController;
import main.java.model.MechanicModel;
import main.java.model.AppointmentModel;
import main.java.model.DatabaseConnection;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class ManagerControllerTest {
    private Connection connection;
    private MechanicModel mechanicModel;
    private AppointmentModel appointmentModel;
    private ManagerController managerController;
    private JFrame parent;

    @BeforeEach
    void setUp() throws SQLException {
        // Setup database connection
        connection = DatabaseConnection.connect();
        mechanicModel = new MechanicModel(connection);
        appointmentModel = new AppointmentModel(connection);
        managerController = new ManagerController(mechanicModel, appointmentModel);
        parent = new JFrame();  // We can just use a simple JFrame for testing

        // Create test tables and add initial data
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Customers (" +
                    "customerID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "userName VARCHAR(100), " +
                    "password VARCHAR(255), " +
                    "fullName VARCHAR(100))");

            statement.execute("CREATE TABLE IF NOT EXISTS Mechanics (" +
                    "mechanicID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "userName VARCHAR(100), " +
                    "password VARCHAR(255), " +
                    "contactInfo VARCHAR(400))");

            statement.execute("CREATE TABLE IF NOT EXISTS appointments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "mechanicID INT, " +
                    "customerID INT, " +
                    "customer_name VARCHAR(255) NOT NULL, " +
                    "vehicle_type VARCHAR(255) NOT NULL, " +
                    "appointment_date DATE NOT NULL, " +
                    "appointment_time TIME NOT NULL, " +
                    "inspection_status ENUM('Failed', 'Passed') NULL, " +
                    "FOREIGN KEY (mechanicID) REFERENCES Mechanics(mechanicID), " +
                    "FOREIGN KEY (customerID) REFERENCES Customers(customerID))");

            // Insert test data
            statement.execute("INSERT INTO Customers (userName, password, fullName) VALUES " +
                    "('jdoe', 'password123', 'John Doe'), " +
                    "('janed', 'password456', 'Jane Doe')");

            statement.execute("INSERT INTO Mechanics (userName, password, contactInfo) VALUES " +
                    "('mjack', 'mech123', '123-456-7890'), " +
                    "('tstone', 'mech456', '987-654-3210')");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Clean up test database
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS appointments");
            statement.execute("DROP TABLE IF EXISTS Mechanics");
            statement.execute("DROP TABLE IF EXISTS Customers");
        }
        connection.close();
    }

    @Test
    void testAddMechanic() throws SQLException {
        // Test adding a mechanic
        String name = "New Mechanic";
        String password = "password789";
        String contactInfo = "555-555-5555";

        boolean result = mechanicModel.addMechanic(name, password, contactInfo);

        // Assert mechanic was added successfully
        assertTrue(result, "Mechanic should be added successfully.");

        // Verify that the mechanic is added in the database
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Mechanics WHERE userName = 'New Mechanic'")) {
            assertTrue(resultSet.next(), "The mechanic should exist in the database.");
            assertEquals("New Mechanic", resultSet.getString("userName"));
        }
    }

    @Test
    void testRemoveMechanic() throws SQLException {
        // Test removing a mechanic
        String name = "mjack";  // Existing mechanic in the setup

        boolean result = mechanicModel.removeMechanic(name);

        // Assert mechanic was removed successfully
        assertTrue(result, "Mechanic should be removed successfully.");

        // Verify that the mechanic was removed from the database
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Mechanics WHERE userName = 'mjack'")) {
            assertFalse(resultSet.next(), "The mechanic should no longer exist in the database.");
        }
    }

    @Test
    void testViewAllAppointments() throws SQLException {
        // Test viewing all appointments
        String expected = "Customer: John Doe, Vehicle: SUV, Date: 2024-12-25, Time: 10:00:00, Mechanic ID: 1\n" +
                "Customer: Jane Doe, Vehicle: Sedan, Date: 2024-12-26, Time: 14:00:00, Mechanic ID: 2\n";

        // Fetch all appointments
        ResultSet resultSet = appointmentModel.getAllAppointmentsWithJoinCustomers();
        StringBuilder appointments = new StringBuilder();
        while (resultSet.next()) {
            appointments.append("Customer: ").append(resultSet.getString("fullName"))
                    .append(", Vehicle: ").append(resultSet.getString("vehicle_type"))
                    .append(", Date: ").append(resultSet.getDate("appointment_date"))
                    .append(", Time: ").append(resultSet.getTime("appointment_time"))
                    .append(", Mechanic ID: ").append(resultSet.getInt("mechanicID"))
                    .append("\n");
        }

        // Assert that the correct appointments are displayed
        assertEquals(expected, appointments.toString(), "The appointments should match the expected values.");
    }
}
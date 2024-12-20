package test.java.model;

import main.java.model.AppointmentModel;
import main.java.model.DatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentModelTest {
    private Connection connection;
    private AppointmentModel appointmentModel;

    @BeforeEach
    void setUp() throws SQLException {
        // Veritabanına bağlan
        connection = DatabaseConnection.connect();
        appointmentModel = new AppointmentModel(connection);

        // Veritabanı şeması oluştur ve örnek veriler ekle
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

            // Örnek veriler ekle
            statement.execute("INSERT INTO Customers (userName, password, fullName) VALUES " +
                    "('jdoe', 'password123', 'John Doe'), " +
                    "('janed', 'password456', 'Jane Doe')");

            statement.execute("INSERT INTO Mechanics (userName, password, contactInfo) VALUES " +
                    "('mjack', 'mech123', '123-456-7890'), " +
                    "('tstone', 'mech456', '987-654-3210')");

            statement.execute("INSERT INTO appointments (mechanicID, customerID, customer_name, vehicle_type, appointment_date, appointment_time, inspection_status) VALUES " +
                    "(1, 1, 'jdoe', 'SUV', '2024-12-25', '10:00:00', 'Passed'), " +
                    "(2, 2, 'janed', 'Sedan', '2024-12-26', '14:00:00', 'Failed')");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Testlerden sonra veritabanını temizle
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS appointments");
            statement.execute("DROP TABLE IF EXISTS Mechanics");
            statement.execute("DROP TABLE IF EXISTS Customers");
        }
        connection.close();
    }

    @Test
    void testGetAllAppointments() throws SQLException {
        var resultSet = appointmentModel.getAllAppointments();

        ArrayList<String> appointments = new ArrayList<>();
        while (resultSet.next()) {
            appointments.add("ID: " + resultSet.getInt("id") +
                    ", Mechanic ID: " + resultSet.getInt("mechanicID") +
                    ", Customer ID: " + resultSet.getInt("customerID") +
                    ", Customer Name: " + resultSet.getString("customer_name") +
                    ", Vehicle: " + resultSet.getString("vehicle_type") +
                    ", Date: " + resultSet.getDate("appointment_date") +
                    ", Time: " + resultSet.getTime("appointment_time") +
                    ", Status: " + resultSet.getString("inspection_status"));
        }

        assertEquals(2, appointments.size(), "There should be 2 appointments.");
        assertTrue(appointments.get(0).contains("jdoe"), "First appointment should belong to 'jdoe'.");
    }

    @Test
    void testGetAllAppointmentsWithJoinCustomers() throws SQLException {
        var resultSet = appointmentModel.getAllAppointmentsWithJoinCustomers();

        ArrayList<String> appointments = new ArrayList<>();
        while (resultSet.next()) {
            appointments.add("ID: " + resultSet.getInt("id") +
                    ", Customer: " + resultSet.getString("fullName") +
                    ", Mechanic ID: " + resultSet.getInt("mechanicID") +
                    ", Vehicle: " + resultSet.getString("vehicle_type") +
                    ", Date: " + resultSet.getDate("appointment_date") +
                    ", Time: " + resultSet.getTime("appointment_time") +
                    ", Status: " + resultSet.getString("inspection_status"));
        }

        assertEquals(2, appointments.size(), "There should be 2 appointments.");
        assertTrue(appointments.get(0).contains("John Doe"), "First appointment should contain 'John Doe'.");
    }
}
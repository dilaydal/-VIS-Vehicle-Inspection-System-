package test.java.model;

import main.java.model.CustomerModel;
import main.java.model.DatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerModelTest {
    private Connection connection;
    private CustomerModel customerModel;

    @BeforeEach
    void setUp() throws SQLException {
        // Veritabanına bağlan
        connection = DatabaseConnection.connect();
        customerModel = new CustomerModel(connection);

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
    void testGetAvailableMechanics() throws SQLException {
        // Test edilen tarih ve saat
        String appointmentDate = "2024-12-25";
        String appointmentTime = "10:00:00";

        // Mevcut mekanikleri al
        var availableMechanics = customerModel.getAvailableMechanics(appointmentDate, appointmentTime);

        // Sonuçları kontrol et
        assertNotNull(availableMechanics, "Mechanics list should not be null.");
        assertTrue(availableMechanics.isEmpty(), "No mechanics should be available for this appointment.");
    }

    @Test
    void testGetAppointments() throws SQLException {
        // Test edilen müşteri adı
        String customerName = "jdoe";

        // Müşteri randevularını al
        var appointments = customerModel.getAppointments(customerName);

        // Sonuçları kontrol et
        assertNotNull(appointments, "Appointments list should not be null.");
        assertEquals(1, appointments.size(), "Customer 'jdoe' should have 1 appointment.");
        assertTrue(appointments.get(0).contains("2024-12-25"), "Appointment date should match '2024-12-25'.");
    }
}

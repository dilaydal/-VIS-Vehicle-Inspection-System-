package test.java.model;

import main.java.model.DatabaseConnection;
import main.java.model.MechanicModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MechanicModelTest {
    private Connection connection;
    private MechanicModel mechanicModel;

    @BeforeEach
    void setUp() throws SQLException {
        // Veritabanına bağlan
        connection = DatabaseConnection.connect();
        mechanicModel = new MechanicModel(connection);

        // Veritabanı şeması oluştur ve örnek veriler ekle
        try (Statement statement = connection.createStatement()) {
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
                    "FOREIGN KEY (mechanicID) REFERENCES Mechanics(mechanicID))");

            // Örnek mekanikler ekle
            statement.execute("INSERT INTO Mechanics (userName, password, contactInfo) VALUES " +
                    "('mjack', 'mech123', '123-456-7890'), " +
                    "('tstone', 'mech456', '987-654-3210')");

            // Örnek randevular ekle
            statement.execute("INSERT INTO appointments (mechanicID, customer_name, vehicle_type, appointment_date, appointment_time, inspection_status) VALUES " +
                    "(1, 'jdoe', 'SUV', '2024-12-25', '10:00:00', 'Passed'), " +
                    "(2, 'janed', 'Sedan', '2024-12-26', '14:00:00', 'Failed')");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Testlerden sonra veritabanını temizle
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS appointments");
            statement.execute("DROP TABLE IF EXISTS Mechanics");
        }
        connection.close();
    }

    @Test
    void testAddMechanic() throws SQLException {
        // Yeni bir mekanik ekle
        boolean result = mechanicModel.addMechanic("sroberts", "mech789", "321-654-9870");

        // Sonuçları kontrol et
        assertTrue(result, "Mechanic should be added successfully.");

        // Yeni mekanik veritabanına eklenip eklenmediğini kontrol et
        var resultSet = mechanicModel.getAllMechanics();
        assertTrue(resultSet.next(), "Mechanics list should not be empty.");
        assertEquals("sroberts", resultSet.getString("userName"), "The mechanic's name should match.");
    }

    @Test
    void testRemoveMechanic() throws SQLException {
        // Mevcut bir mekanik sil
        boolean result = mechanicModel.removeMechanic("mjack");

        // Sonuçları kontrol et
        assertTrue(result, "Mechanic should be removed successfully.");

        // Silinen mekanik veritabanından kaldırıldığını kontrol et
        var resultSet = mechanicModel.getAllMechanics();
        while (resultSet.next()) {
            assertNotEquals("mjack", resultSet.getString("userName"), "Removed mechanic should not be in the list.");
        }
    }

    @Test
    void testGetAllMechanics() throws SQLException {
        // Tüm mekanikleri al
        var resultSet = mechanicModel.getAllMechanics();

        // Sonuçları kontrol et
        ArrayList<String> mechanics = new ArrayList<>();
        while (resultSet.next()) {
            mechanics.add(resultSet.getString("userName"));
        }

        assertFalse(mechanics.isEmpty(), "Mechanics list should not be empty.");
        assertTrue(mechanics.contains("mjack"), "Mechanic 'mjack' should be in the list.");
    }

    @Test
    void testGetDailyTaskSchedule() throws SQLException {
        // Mevcut bir mekanik için günlük görev programını al
        var resultSet = mechanicModel.getDailyTaskSchedule(1);

        // Sonuçları kontrol et
        ArrayList<String> appointments = new ArrayList<>();
        while (resultSet.next()) {
            appointments.add(resultSet.getString("customer_name") + " | " + resultSet.getString("vehicle_type") + " | " + resultSet.getString("appointment_time"));
        }

        assertFalse(appointments.isEmpty(), "Mechanic should have tasks for today.");
        assertTrue(appointments.get(0).contains("jdoe"), "The task should belong to 'jdoe'.");
    }

    @Test
    void testUpdateInspectionStatus() throws SQLException {
        // Randevu ID'si ile inspection status güncelle
        mechanicModel.updateInspectionStatus(1, "Failed");

        // Güncellenen durumu kontrol et
        var resultSet = mechanicModel.getDailyTaskSchedule(1);
        assertTrue(resultSet.next(), "Appointment should exist.");
        assertEquals("Failed", resultSet.getString("inspection_status"), "Inspection status should be updated to 'Failed'.");
    }
}

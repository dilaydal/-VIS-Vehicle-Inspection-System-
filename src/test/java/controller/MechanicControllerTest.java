package test.java.controller;

import main.java.controller.MechanicController;
import main.java.model.MechanicModel;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class MechanicControllerTest {

    private MechanicModel mechanicModel;
    private MechanicController mechanicController;
    private DefaultTableModel tableModel;
    private JFrame parentFrame;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        // Establish the database connection
        String url = "jdbc:mysql://localhost:3306/VIS"; // Provide the database path here
        connection = DriverManager.getConnection(url);

        // Create Model and Controller objects
        mechanicModel = new MechanicModel(connection);
        mechanicController = new MechanicController(mechanicModel);

        // Create table model
        String[] columnNames = {"AppointmentID", "Customer Name", "Vehicle Type", "Appointment Time", "Inspection Status"};
        tableModel = new DefaultTableModel(columnNames, 0);

        parentFrame = new JFrame(); // Placeholder frame
    }

    @Test
    public void testLoadMechanicSchedule() throws SQLException {
        // Arrange
        int mechanicID = 1;

        // Create test data in the database
        try (Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO daily_schedule (id, customer_name, vehicle_type, appointment_time, inspection_status, mechanic_id) " +
                    "VALUES (101, 'Alex Johnson', 'Car', '09:00 AM', 'Passed', 1), " +
                    "(102, 'Jane Smith', 'Truck', '10:00 AM', 'Failed', 1)");
        }

        // Act
        mechanicController.loadMechanicSchedule(mechanicID, tableModel, parentFrame);

        // Assert
        assertEquals(2, tableModel.getRowCount());
        assertEquals(101, tableModel.getValueAt(0, 0));
        assertEquals("Alex Johnson", tableModel.getValueAt(0, 1));
        assertEquals("Car", tableModel.getValueAt(0, 2));
        assertEquals("09:00 AM", tableModel.getValueAt(0, 3));
        assertEquals("Passed", tableModel.getValueAt(0, 4));

        assertEquals(102, tableModel.getValueAt(1, 0));
        assertEquals("Jane Smith", tableModel.getValueAt(1, 1));
        assertEquals("Truck", tableModel.getValueAt(1, 2));
        assertEquals("10:00 AM", tableModel.getValueAt(1, 3));
        assertEquals("Failed", tableModel.getValueAt(1, 4));

        // Clean up test data
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM daily_schedule WHERE mechanic_id = 1");
        }
    }

    @Test
    public void testUpdateInspectionStatus() throws SQLException {
        // Arrange
        int appointmentID = 101;
        String newStatus = "Completed";

        // Create test data in the database
        try (Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO daily_schedule (id, customer_name, vehicle_type, appointment_time, inspection_status, mechanic_id) " +
                    "VALUES (101, 'Alex Johnson', 'Car', '09:00 AM', 'Passed', 1)");
        }

        // Act
        mechanicController.updateInspectionStatus(appointmentID, newStatus);

        // Retrieve the updated value from the database
        String updatedStatus = null;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT inspection_status FROM daily_schedule WHERE id = 101")) {
            if (resultSet.next()) {
                updatedStatus = resultSet.getString("inspection_status");
            }
        }

        // Assert
        assertEquals(newStatus, updatedStatus);

        // Clean up test data
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM daily_schedule WHERE id = 101");
        }
    }
}

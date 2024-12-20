package test.java.controller;

import main.java.controller.MechanicController;
import main.java.model.MechanicModel;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

public class MechanicControllerTest {

    private MechanicModel mechanicModelMock;
    private MechanicController mechanicController;
    private DefaultTableModel tableModel;
    private JFrame parentFrame;

    @Before
    public void setUp() {
        mechanicModelMock = mock(MechanicModel.class);
        mechanicController = new MechanicController(mechanicModelMock);

        // Create a table model with the appropriate columns
        String[] columnNames = { "AppointmentID", "Customer Name", "Vehicle Type", "Appointment Time", "Inspection Status" };
        tableModel = new DefaultTableModel(columnNames, 0);

        parentFrame = new JFrame(); // Placeholder frame for error dialogs
    }

    @Test
    public void testLoadMechanicSchedule() throws SQLException {
        // Arrange
        int mechanicID = 1;
        ResultSet resultSetMock = mock(ResultSet.class);
        when(mechanicModelMock.getDailyTaskSchedule(mechanicID)).thenReturn(resultSetMock);

        // Mock ResultSet data
        when(resultSetMock.next()).thenReturn(true, true, false);
        when(resultSetMock.getInt("id")).thenReturn(101, 102);
        when(resultSetMock.getString("customer_name")).thenReturn("Alex Johnson", "Jane Smith");
        when(resultSetMock.getString("vehicle_type")).thenReturn("Car", "Truck");
        when(resultSetMock.getString("appointment_time")).thenReturn("09:00 AM", "10:00 AM");
        when(resultSetMock.getString("inspection_status")).thenReturn("Passed", "Failed");

        // Act
        mechanicController.loadMechanicSchedule(mechanicID, tableModel, parentFrame);

        // Assert
        assertEquals(2, tableModel.getRowCount());
        assertEquals(101, tableModel.getValueAt(0, 0));
        assertEquals("Alex Johnson", tableModel.getValueAt(0, 1)); // Changed name here
        assertEquals("Car", tableModel.getValueAt(0, 2));
        assertEquals("09:00 AM", tableModel.getValueAt(0, 3));
        assertEquals("Passed", tableModel.getValueAt(0, 4));

        assertEquals(102, tableModel.getValueAt(1, 0));
        assertEquals("Jane Smith", tableModel.getValueAt(1, 1));
        assertEquals("Truck", tableModel.getValueAt(1, 2));
        assertEquals("10:00 AM", tableModel.getValueAt(1, 3));
        assertEquals("Failed", tableModel.getValueAt(1, 4));
    }

    @Test
    public void testUpdateInspectionStatus() {
        // Arrange
        int appointmentID = 101;
        String newStatus = "Passed";

        // Act
        mechanicController.updateInspectionStatus(appointmentID, newStatus);

        // Assert
        verify(mechanicModelMock).updateInspectionStatus(appointmentID, newStatus);
    }
}

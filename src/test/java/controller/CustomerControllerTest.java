package test.java.controller;

import main.java.controller.CustomerController;
import main.java.model.CustomerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    private CustomerModel mockCustomerModel;
    private CustomerController customerController;
    private JFrame mockFrame;

    @BeforeEach
    void setUp() {
        mockCustomerModel = mock(CustomerModel.class);
        customerController = new CustomerController(mockCustomerModel);
        mockFrame = mock(JFrame.class);
    }

    @Test
    void testGetAppointments() throws SQLException {
        // Success scenario
        ArrayList<String> mockAppointments = new ArrayList<>();
        mockAppointments.add("ID: 1 | Date: 2024-12-25 | Time: 10:00 | Vehicle: Sedan | Mechanic ID: 101");
        when(mockCustomerModel.getAppointments("Jane Smith")).thenReturn(mockAppointments);

        ArrayList<String> appointments = customerController.getAppointments("Jane Smith", mockFrame);
        assertEquals(1, appointments.size(), "Success: Appointments should be fetched correctly.");

        // Failure scenario
        when(mockCustomerModel.getAppointments("Jane Smith")).thenThrow(new SQLException("Database error"));
        appointments = customerController.getAppointments("Jane Smith", mockFrame);
        assertTrue(appointments.isEmpty(), "Failure: Should return an empty list in case of an error.");
    }

    @Test
    void testRescheduleAppointment() throws SQLException {
        // Success scenario
        when(mockCustomerModel.rescheduleAppointment(1, "2024-12-26", "14:00", 102)).thenReturn(true);

        customerController.rescheduleAppointment(1, "2024-12-26", "14:00", 102, mockFrame);
        verify(mockCustomerModel).rescheduleAppointment(1, "2024-12-26", "14:00", 102);

        // Failure scenario
        when(mockCustomerModel.rescheduleAppointment(1, "2024-12-26", "14:00", 102)).thenReturn(false);

        customerController.rescheduleAppointment(1, "2024-12-26", "14:00", 102, mockFrame);
        verify(mockCustomerModel, times(2)).rescheduleAppointment(1, "2024-12-26", "14:00", 102);
    }

    @Test
    void testCreateAppointment() throws SQLException {
        // Success scenario
        when(mockCustomerModel.createAppointment("Jane Smith", "SUV", "2024-12-30", "09:00", 103)).thenReturn(true);

        customerController.createAppointment("Jane Smith", "SUV", "2024-12-30", "09:00", 103, mockFrame);
        verify(mockCustomerModel).createAppointment("Jane Smith", "SUV", "2024-12-30", "09:00", 103);

        // Failure scenario
        when(mockCustomerModel.createAppointment("Jane Smith", "SUV", "2024-12-30", "09:00", 103)).thenReturn(false);

        customerController.createAppointment("Jane Smith", "SUV", "2024-12-30", "09:00", 103, mockFrame);
        verify(mockCustomerModel, times(2)).createAppointment("Jane Smith", "SUV", "2024-12-30", "09:00", 103);
    }

    @Test
    void testCancelAppointment() throws SQLException {
        // Success scenario
        when(mockCustomerModel.cancelAppointment(1)).thenReturn(true);

        customerController.cancelAppointment(1, mockFrame);
        verify(mockCustomerModel).cancelAppointment(1);

        // Failure scenario
        when(mockCustomerModel.cancelAppointment(1)).thenReturn(false);

        customerController.cancelAppointment(1, mockFrame);
        verify(mockCustomerModel, times(2)).cancelAppointment(1);
    }

    @Test
    void testGetAvailableMechanics() throws SQLException {
        // Success scenario
        ArrayList<String> mockMechanics = new ArrayList<>();
        mockMechanics.add("Mechanic ID: 101 | Name: Alice");
        mockMechanics.add("Mechanic ID: 102 | Name: Bob");
        when(mockCustomerModel.getAvailableMechanics("2024-12-25", "10:00")).thenReturn(mockMechanics);

        ArrayList<String> availableMechanics = customerController.getAvailableMechanics(mockFrame, "2024-12-25", "10:00");
        assertEquals(2, availableMechanics.size(), "Success: Should return two available mechanics.");
        assertEquals("Mechanic ID: 101 | Name: Alice", availableMechanics.get(0));
        assertEquals("Mechanic ID: 102 | Name: Bob", availableMechanics.get(1));

        // Failure scenario
        when(mockCustomerModel.getAvailableMechanics("2024-12-25", "10:00")).thenThrow(new SQLException("Database error"));

        availableMechanics = customerController.getAvailableMechanics(mockFrame, "2024-12-25", "10:00");
        assertTrue(availableMechanics.isEmpty(), "Failure: Should return an empty list in case of an error.");
    }
}

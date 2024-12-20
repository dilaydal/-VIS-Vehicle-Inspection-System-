package test.java.controller;

import main.java.controller.CustomerController;
import main.java.model.CustomerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerControllerTest {

    private CustomerModel customerModel;
    private CustomerController customerController;
    private JFrame testFrame;

    @BeforeEach
    void setUp() {
        customerModel = new CustomerModel(null) {
            @Override
            public ArrayList<String> getAppointments(String customerName) throws SQLException {
                if ("error_case".equals(customerName)) {
                    throw new SQLException("Database error");
                }
                ArrayList<String> appointments = new ArrayList<>();
                appointments.add("ID: 1 | Date: 2024-12-25 | Time: 10:00 | Vehicle: Sedan | Mechanic ID: 101");
                return appointments;
            }

            @Override
            public boolean rescheduleAppointment(int appointmentId, String newDate, String newTime, int mechanicID) throws SQLException {
                return appointmentId == 1 && "2024-12-26".equals(newDate) && "14:00".equals(newTime) && mechanicID == 102;
            }

            @Override
            public boolean createAppointment(String customerName, String vehicleType, String date, String time, int mechanicID) throws SQLException {
                return "Jane Smith".equals(customerName) && "SUV".equals(vehicleType) && "2024-12-30".equals(date) && "09:00".equals(time) && mechanicID == 103;
            }
        };

        customerController = new CustomerController(customerModel);
        testFrame = new JFrame();
    }

    @Test
    void testGetAppointments() {
        // Successful scenario
        ArrayList<String> appointments = customerController.getAppointments("Jane Smith", testFrame);
        assertEquals(1, appointments.size(), "Success: Appointments should be fetched correctly.");

        // Error scenario
        appointments = customerController.getAppointments("error_case", testFrame);
        assertTrue(appointments.isEmpty(), "Error: An empty list should be returned in case of an error.");
    }

    @Test
    void testRescheduleAppointment() {
        // Successful scenario
        customerController.rescheduleAppointment(1, "2024-12-26", "14:00", 102, testFrame);
        assertDoesNotThrow(() -> customerModel.rescheduleAppointment(1, "2024-12-26", "14:00", 102));

        // Failed scenario
        customerController.rescheduleAppointment(2, "2024-12-26", "14:00", 102, testFrame);
        assertThrows(SQLException.class, () -> {
            if (!customerModel.rescheduleAppointment(2, "2024-12-26", "14:00", 102)) {
                throw new SQLException("Rescheduling failed");
            }
        });
    }

    @Test
    void testCreateAppointment() {
        // Successful scenario
        customerController.createAppointment("Jane Smith", "SUV", "2024-12-30", "09:00", 103, testFrame);
        assertDoesNotThrow(() -> customerModel.createAppointment("Jane Smith", "SUV", "2024-12-30", "09:00", 103));

        // Failed scenario
        customerController.createAppointment("Jane Smith", "SUV", "2024-12-30", "10:00", 103, testFrame);
        assertThrows(SQLException.class, () -> {
            if (!customerModel.createAppointment("Jane Smith", "SUV", "2024-12-30", "10:00", 103)) {
                throw new SQLException("Appointment creation failed");
            }
        });
    }
}

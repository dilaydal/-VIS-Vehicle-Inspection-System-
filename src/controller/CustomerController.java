package controller;

import model.CustomerModel;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerController {
    private CustomerModel customerModel;

    public CustomerController(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }

    public ArrayList<String> getAppointments(String customerName, JFrame parent) {
        try {
            return customerModel.getAppointments(customerName);
        } catch (SQLException e) {
            showErrorDialog(parent, "Error loading appointments: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void rescheduleAppointment(int appointmentId, String newDate, String newTime, JFrame parent) {
        try {
            if (customerModel.rescheduleAppointment(appointmentId, newDate, newTime)) {
                showInfoDialog(parent, "Appointment rescheduled successfully.");
            } else {
                showErrorDialog(parent, "Failed to reschedule appointment.");
            }
        } catch (SQLException e) {
            showErrorDialog(parent, "Error rescheduling appointment: " + e.getMessage());
        }
    }

    public void cancelAppointment(int appointmentId, JFrame parent) {
        try {
            if (customerModel.cancelAppointment(appointmentId)) {
                showInfoDialog(parent, "Appointment canceled successfully.");
            } else {
                showErrorDialog(parent, "Failed to cancel appointment.");
            }
        } catch (SQLException e) {
            showErrorDialog(parent, "Error canceling appointment: " + e.getMessage());
        }
    }

    public void createAppointment(String customerName, String vehicleType, String date, String time, int mechanicID, JFrame parent) {
        try {
            if (customerModel.createAppointment(customerName, vehicleType, date, time, mechanicID)) {
                showInfoDialog(parent, "Appointment created successfully.");
            } else {
                showErrorDialog(parent, "Failed to create appointment.");
            }
        } catch (SQLException e) {
            showErrorDialog(parent, "Error creating appointment: " + e.getMessage());
        }
    }
    public ArrayList<String> getMechanics(JFrame parent) {
        try {
            return customerModel.getMechanics();
        } catch (SQLException e) {
            showErrorDialog(parent, "Error loading mechanics: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    private void showErrorDialog(JFrame parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfoDialog(JFrame parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
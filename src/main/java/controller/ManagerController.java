package main.java.controller;

import main.java.model.*;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerController {
    private MechanicModel mechanicModel;
    private AppointmentModel appointmentModel;

    public ManagerController(MechanicModel mechanicModel, AppointmentModel appointmentModel) {
        this.mechanicModel = mechanicModel;
        this.appointmentModel = appointmentModel;
    }

    public void addMechanic(String name, String password, String contactInfo, JFrame parent) {
        try {
            if (mechanicModel.addMechanic(name, password, contactInfo)) {
                JOptionPane.showMessageDialog(parent, "Mechanic added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "Failed to add mechanic.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            showErrorDialog(parent, e);
        }
    }

    public void removeMechanic(String name, JFrame parent) {
        try {
            if (mechanicModel.removeMechanic(name)) {
                JOptionPane.showMessageDialog(parent, "Mechanic removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "No such mechanic found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            showErrorDialog(parent, e);
        }
    }

    public void viewAllAppointments(JFrame parent) {
        try {
            ResultSet resultSet = appointmentModel.getAllAppointmentsWithJoinCustomers();
            StringBuilder appointments = new StringBuilder("Appointments:\n");

            while (resultSet.next()) {
                appointments.append("Customer: ").append(resultSet.getString("fullName"))
                        .append(", Vehicle: ").append(resultSet.getString("vehicle_type"))
                        .append(", Date: ").append(resultSet.getDate("appointment_date"))
                        .append(", Time: ").append(resultSet.getTime("appointment_time"))
                        .append(", Mechanic ID: ").append(resultSet.getInt("mechanicID"))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(parent, appointments.toString(), "All Appointments", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            showErrorDialog(parent, e);
        }
    }

    private void showErrorDialog(JFrame parent, SQLException e) {
        JOptionPane.showMessageDialog(parent, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
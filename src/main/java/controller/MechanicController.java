package main.java.controller;

import main.java.model.Appointment;
import main.java.model.MechanicModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MechanicController {
    private MechanicModel mechanicModel;

    public MechanicController(MechanicModel mechanicModel) {
        this.mechanicModel = mechanicModel;
    }

    public void loadMechanicSchedule(int mechanicID, DefaultTableModel tableModel, JFrame parent) {
        try {
            ResultSet resultSet = mechanicModel.getDailyTaskSchedule(mechanicID);

            while (resultSet.next()) {
                int appointmentID = resultSet.getInt("id");
                String customerName = resultSet.getString("customer_name");
                String vehicleType = resultSet.getString("vehicle_type");
                String appointmentTime = resultSet.getString("appointment_time");
                String inspectionStatus = resultSet.getString("inspection_status");
                tableModel.addRow(
                        new Object[] { appointmentID, customerName, vehicleType, appointmentTime, inspectionStatus });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error loading schedule: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * public void updateInspectionStatus(int mechanicID, String customerName,
     * String vehicleType,
     * String appointmentTime, String status, JFrame parent) {
     * try {
     * boolean isUpdated = mechanicModel.updateInspectionStatus(mechanicID,
     * customerName,
     * vehicleType,
     * appointmentTime, status);
     * 
     * if (isUpdated) {
     * JOptionPane.showMessageDialog(parent, "Inspection status updated to: " +
     * status, "Success",
     * JOptionPane.INFORMATION_MESSAGE);
     * } else {
     * JOptionPane.showMessageDialog(parent,
     * "No records were updated. Please check the database.", "Error",
     * JOptionPane.ERROR_MESSAGE);
     * }
     * } catch (SQLException e) {
     * JOptionPane.showMessageDialog(parent, "Error updating status: " +
     * e.getMessage(), "Error",
     * JOptionPane.ERROR_MESSAGE);
     * }
     * }
     * 
     */

    public void updateInspectionStatus(int AppointmentID, String newStatus) {
        mechanicModel.updateInspectionStatus(AppointmentID, newStatus);
    }
}
package main.java.View;

import main.java.controller.*;
import main.java.model.*;
import main.java.users.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.*;

public class MechanicFrame extends JFrame {
    private MechanicController mechanicController;
    private JTable scheduleTable; // Schedule table'ı sınıf düzeyinde tanımladım
    private DefaultTableModel tableModel; // Table model'ini de sınıf düzeyinde tanımladım

    public MechanicFrame(User user) {
        String mechanicName = user.getUsername();
        int mechanicID = user.getId();
        setTitle("Mechanic Dashboard - " + mechanicName);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        MechanicModel mechanicModel = new MechanicModel(main.java.model.DatabaseConnection.connect());
        mechanicController = new MechanicController(mechanicModel);

        JLabel titleLabel = new JLabel("Daily Task Schedule", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        String[] columnNames = { "AppointmentID", "Customer Name", "Vehicle Type", "Appointment Time",
                "Inspection Status" };
        tableModel = new DefaultTableModel(columnNames, 0);
        scheduleTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(scheduleTable);

        JButton updateStatusButton = new JButton("Update Inspection Status");
        /*
         * updateStatusButton.addActionListener(e -> {
         * int selectedRow = scheduleTable.getSelectedRow();
         * if (selectedRow == -1) {
         * JOptionPane.showMessageDialog(this,
         * "Please select a row to update the status.", "Error",
         * JOptionPane.ERROR_MESSAGE);
         * return;
         * }
         * 
         * String customerName = tableModel.getValueAt(selectedRow, 0).toString();
         * String vehicleType = tableModel.getValueAt(selectedRow, 1).toString();
         * String appointmentTime = tableModel.getValueAt(selectedRow, 2).toString();
         * 
         * String[] options = { "Passed", "Failed" };
         * int choice = JOptionPane.showOptionDialog(this,
         * "Update inspection status for " + customerName,
         * "Inspection Status", JOptionPane.DEFAULT_OPTION,
         * JOptionPane.INFORMATION_MESSAGE,
         * null, options, options[0]);
         * 
         * if (choice != -1) {
         * String status = options[choice];
         * mechanicController.updateInspectionStatus(mechanicID, customerName,
         * vehicleType, appointmentTime,
         * status, this);
         * 
         * // Update the table to reflect the new status
         * tableModel.setValueAt(status, selectedRow, 5);
         * }
         * });
         */

        updateStatusButton.addActionListener(e -> updateInspectionStatus());

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            new MainFrame().setVisible(true);
            dispose();
        });

        add(titleLabel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.SOUTH);

        mechanicController.loadMechanicSchedule(mechanicID, tableModel, this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new MainFrame().setVisible(true);
            }
        });
    }

    private void updateInspectionStatus() {
        int selectedRow = scheduleTable.getSelectedRow();
        if (selectedRow != -1) {
            int taskID = (int) tableModel.getValueAt(selectedRow, 0);
            String newStatus = JOptionPane.showInputDialog(this, "Enter new status:");

            if (newStatus != null && !newStatus.isEmpty()) {
                if (newStatus.equalsIgnoreCase("Failed") || newStatus.equalsIgnoreCase("Passed")) {
                    mechanicController.updateInspectionStatus(taskID, newStatus);
                    JOptionPane.showMessageDialog(this, "Status updated successfully.");
                    tableModel.setValueAt(newStatus, selectedRow, 4);
                } else {
                    JOptionPane.showMessageDialog(this, "Please write a valid status (i.e., Failed or Passed).");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to update.");
        }
    }
}
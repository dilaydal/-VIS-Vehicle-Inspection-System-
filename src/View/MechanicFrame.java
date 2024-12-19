package View;

import controller.*;
import model.*;
import users.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;


public class MechanicFrame extends JFrame {
    private MechanicController mechanicController;

    public MechanicFrame(User user) {
        String mechanicName = user.getUsername();
        int mechanicID=user.getId();
        setTitle("Mechanic Dashboard - " + mechanicName);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        MechanicModel mechanicModel = new MechanicModel(model.DatabaseConnection.connect());
        mechanicController = new MechanicController(mechanicModel);

        JLabel titleLabel = new JLabel("Daily Task Schedule", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        String[] columnNames = {"Customer Name", "Vehicle Type", "Appointment Time"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable scheduleTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(scheduleTable);

        JButton updateStatusButton = new JButton("Update Inspection Status");
        updateStatusButton.addActionListener(e -> {
            int selectedRow = scheduleTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to update the status.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String customerName = tableModel.getValueAt(selectedRow, 0).toString();
            String vehicleType = tableModel.getValueAt(selectedRow, 1).toString();
            String appointmentTime = tableModel.getValueAt(selectedRow, 2).toString();

            String[] options = {"Passed", "Failed"};
            int choice = JOptionPane.showOptionDialog(this, "Update inspection status for " + customerName,
                    "Inspection Status", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);

            if (choice != -1) {
                String status = options[choice];
                mechanicController.updateInspectionStatus(customerName, vehicleType, appointmentTime, status, this);
            }
        });

        add(titleLabel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(updateStatusButton, BorderLayout.SOUTH);

        mechanicController.loadMechanicSchedule(mechanicID, tableModel, this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new MainFrame().setVisible(true);
            }
        });
    }
}
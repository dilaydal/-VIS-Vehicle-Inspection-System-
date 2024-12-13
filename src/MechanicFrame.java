import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MechanicFrame extends JFrame {
    public MechanicFrame(String mechanicName) {
        setTitle("Mechanic Dashboard - " + mechanicName);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

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
                updateInspectionStatus(customerName, vehicleType, appointmentTime, status);
                JOptionPane.showMessageDialog(this, "Inspection status updated to: " + status, "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        add(titleLabel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(updateStatusButton, BorderLayout.SOUTH);

        loadMechanicSchedule(mechanicName, tableModel);
    }

    private void loadMechanicSchedule(String mechanicName, DefaultTableModel tableModel) {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT customer_name, vehicle_type, appointment_time FROM appointments WHERE mechanic_name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, mechanicName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String customerName = resultSet.getString("customer_name");
                String vehicleType = resultSet.getString("vehicle_type");
                String appointmentTime = resultSet.getString("appointment_time");
                tableModel.addRow(new Object[]{customerName, vehicleType, appointmentTime});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading schedule: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateInspectionStatus(String customerName, String vehicleType, String appointmentTime, String status) {
        try (Connection connection = DatabaseConnection.connect()) {
            String updateQuery = "UPDATE appointments SET inspection_status = ? WHERE customer_name = ? AND vehicle_type = ? AND appointment_time = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setString(1, status);
            statement.setString(2, customerName);
            statement.setString(3, vehicleType);
            statement.setString(4, appointmentTime);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                JOptionPane.showMessageDialog(this, "No records were updated. Please check the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating status: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

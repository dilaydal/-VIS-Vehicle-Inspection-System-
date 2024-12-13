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
            String[] options = {"Passed", "Failed"};
            int choice = JOptionPane.showOptionDialog(this, "Update inspection status for " + customerName,
                    "Inspection Status", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);

            if (choice != -1) {
                String status = options[choice];
                // Update the status in the database (requires SQL code)
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
}

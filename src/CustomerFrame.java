import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerFrame extends JFrame {
    public CustomerFrame(String customerName) {
        setTitle("Customer Dashboard - " + customerName);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Manage Appointments", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton bookAppointmentButton = new JButton("Book Appointment");
        JButton rescheduleAppointmentButton = new JButton("Reschedule Appointment");
        JButton cancelAppointmentButton = new JButton("Cancel Appointment");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(bookAppointmentButton);
        buttonPanel.add(rescheduleAppointmentButton);
        buttonPanel.add(cancelAppointmentButton);

        bookAppointmentButton.addActionListener(e -> {
            JTextField vehicleTypeField = new JTextField();
            JTextField appointmentDateField = new JTextField();
            JTextField appointmentTimeField = new JTextField();

            Object[] fields = {
                "Vehicle Type:", vehicleTypeField,
                "Appointment Date (YYYY-MM-DD):", appointmentDateField,
                "Appointment Time (HH:MM):", appointmentTimeField
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Book Appointment", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String vehicleType = vehicleTypeField.getText();
                String appointmentDate = appointmentDateField.getText();
                String appointmentTime = appointmentTimeField.getText();

                try (Connection connection = DatabaseConnection.connect()) {
                    String query = "INSERT INTO appointments (customer_name, vehicle_type, appointment_date, appointment_time) VALUES (?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, customerName);
                    statement.setString(2, vehicleType);
                    statement.setString(3, appointmentDate);
                    statement.setString(4, appointmentTime);

                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Appointment booked successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error booking appointment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        rescheduleAppointmentButton.addActionListener(e -> {
            JTextField appointmentIdField = new JTextField();
            JTextField newAppointmentDateField = new JTextField();
            JTextField newAppointmentTimeField = new JTextField();

            Object[] fields = {
                "Appointment ID:", appointmentIdField,
                "New Appointment Date (YYYY-MM-DD):", newAppointmentDateField,
                "New Appointment Time (HH:MM):", newAppointmentTimeField
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Reschedule Appointment", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                int appointmentId = Integer.parseInt(appointmentIdField.getText());
                String newAppointmentDate = newAppointmentDateField.getText();
                String newAppointmentTime = newAppointmentTimeField.getText();

                try (Connection connection = DatabaseConnection.connect()) {
                    String query = "UPDATE appointments SET appointment_date = ?, appointment_time = ? WHERE id = ? AND customer_name = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, newAppointmentDate);
                    statement.setString(2, newAppointmentTime);
                    statement.setInt(3, appointmentId);
                    statement.setString(4, customerName);

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this, "Appointment rescheduled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No appointment found to reschedule.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error rescheduling appointment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelAppointmentButton.addActionListener(e -> {
            JTextField appointmentIdField = new JTextField();

            Object[] fields = {
                "Appointment ID:", appointmentIdField
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Cancel Appointment", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                int appointmentId = Integer.parseInt(appointmentIdField.getText());

                try (Connection connection = DatabaseConnection.connect()) {
                    String query = "DELETE FROM appointments WHERE id = ? AND customer_name = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, appointmentId);
                    statement.setString(2, customerName);

                    int rowsDeleted = statement.executeUpdate();
                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(this, "Appointment canceled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No appointment found to cancel.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error canceling appointment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }
}

package View;

import model.DatabaseConnection;
import users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class CustomerFrame extends JFrame {
    private User user;

    public CustomerFrame(User user) {
        String customerUsername = user.getUsername();
        this.user = user;
        setTitle("Customer Dashboard - " + customerUsername);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        JButton viewAppointmentsButton = new JButton("View Appointments");
        JButton rescheduleAppointmentButton = new JButton("Reschedule Appointment");
        JButton cancelAppointmentButton = new JButton("Cancel Appointment");
        JButton createAppointmentButton = new JButton("Create Appointment");

        viewAppointmentsButton.addActionListener(e -> handleViewAppointments());
        rescheduleAppointmentButton.addActionListener(e -> handleRescheduleAppointment());
        cancelAppointmentButton.addActionListener(e -> handleCancelAppointment());
        createAppointmentButton.addActionListener(e -> handleCreateAppointment());

        add(viewAppointmentsButton);
        add(rescheduleAppointmentButton);
        add(cancelAppointmentButton);
        add(createAppointmentButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new MainFrame().setVisible(true);
            }
        });
    }

    private void handleViewAppointments() {
        ArrayList<String> appointments = loadCustomerAppointments();
        if (appointments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No appointments found.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder message = new StringBuilder("Your Appointments:\n");
            for (String appointment : appointments) {
                message.append(appointment).append("\n");
            }
            JOptionPane.showMessageDialog(this, message.toString(), "Appointments", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private ArrayList<String> loadCustomerAppointments() {
        String customerUsername = user.getUsername();
        ArrayList<String> appointments = new ArrayList<>();
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT id, appointment_date, appointment_time, vehicle_type FROM appointments WHERE customer_name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, customerUsername);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String date = resultSet.getString("appointment_date");
                    String time = resultSet.getString("appointment_time");
                    String vehicle = resultSet.getString("vehicle_type");

                    appointments.add("ID: " + id + " | Date: " + date + " | Time: " + time + " | Vehicle: " + vehicle);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading appointments: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return appointments;
    }

    private void handleRescheduleAppointment() {
        String selectedAppointment = showAppointmentDropdown("Reschedule");
        if (selectedAppointment == null) return;

        String newDate = JOptionPane.showInputDialog(this, "Enter New Appointment Date (YYYY-MM-DD):");
        String newTime = JOptionPane.showInputDialog(this, "Enter New Appointment Time (HH:MM):");

        if (newDate != null && newTime != null && !newDate.isEmpty() && !newTime.isEmpty()) {
            int appointmentId = Integer.parseInt(selectedAppointment.split(" ")[1]);

            try (Connection connection = DatabaseConnection.connect()) {
                String query = "UPDATE appointments SET appointment_date = ?, appointment_time = ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, newDate);
                    statement.setString(2, newTime);
                    statement.setInt(3, appointmentId);

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this, "Appointment rescheduled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to reschedule appointment.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleCancelAppointment() {
        String selectedAppointment = showAppointmentDropdown("Cancel");
        if (selectedAppointment == null) return;

        int appointmentId = Integer.parseInt(selectedAppointment.split(" ")[1]);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel this appointment?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseConnection.connect()) {
                String query = "DELETE FROM appointments WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, appointmentId);

                    int rowsDeleted = statement.executeUpdate();
                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(this, "Appointment canceled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to cancel appointment.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleCreateAppointment() {
        JTextField vehicleTypeField = new JTextField();
        JTextField appointmentDateField = new JTextField();
        JTextField appointmentTimeField = new JTextField();

        Object[] fields = {
                "Vehicle Type:", vehicleTypeField,
                "Appointment Date (YYYY-MM-DD):", appointmentDateField,
                "Appointment Time (HH:MM):", appointmentTimeField
        };

        int result = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Create New Appointment",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String vehicleType = vehicleTypeField.getText();
            String appointmentDate = appointmentDateField.getText();
            String appointmentTime = appointmentTimeField.getText();

            if (vehicleType.isEmpty() || appointmentDate.isEmpty() || appointmentTime.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.connect()) {
                String customerUsername = user.getUsername();
                String query = "INSERT INTO appointments (customer_name, vehicle_type, appointment_date, appointment_time) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, customerUsername);
                    statement.setString(2, vehicleType);
                    statement.setString(3, appointmentDate);
                    statement.setString(4, appointmentTime);

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(this, "Appointment created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to create appointment.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String showAppointmentDropdown(String actionMessage) {
        ArrayList<String> appointments = loadCustomerAppointments();
        if (appointments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No appointments found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

        return (String) JOptionPane.showInputDialog(
                this,
                "Select Appointment to " + actionMessage,
                actionMessage + " Appointment",
                JOptionPane.PLAIN_MESSAGE,
                null,
                appointments.toArray(),
                appointments.get(0)
        );
    }
}

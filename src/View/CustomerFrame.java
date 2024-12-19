package View;

import controller.*;
import model.*;
import users.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class CustomerFrame extends JFrame {
    private CustomerController customerController;

    public CustomerFrame(User user) {
        String customerUsername = user.getUsername();
        setTitle("Customer Dashboard - " + customerUsername);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        CustomerModel customerModel = new CustomerModel(model.DatabaseConnection.connect());
        customerController = new CustomerController(customerModel);

        JButton viewAppointmentsButton = new JButton("View Appointments");
        JButton rescheduleAppointmentButton = new JButton("Reschedule Appointment");
        JButton cancelAppointmentButton = new JButton("Cancel Appointment");
        JButton createAppointmentButton = new JButton("Create Appointment");

        viewAppointmentsButton.addActionListener(e -> handleViewAppointments(customerUsername));
        rescheduleAppointmentButton.addActionListener(e -> handleRescheduleAppointment(customerUsername));
        cancelAppointmentButton.addActionListener(e -> handleCancelAppointment(customerUsername));
        createAppointmentButton.addActionListener(e -> handleCreateAppointment(customerUsername));

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

    private void handleViewAppointments(String customerName) {
        ArrayList<String> appointments = customerController.getAppointments(customerName, this);
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


    private void handleRescheduleAppointment(String customerName) {
        String selectedAppointment = showReschedulingAndCancelingAppointmentDropdown("Reschedule", customerName);
        if (selectedAppointment == null) return;

        String newDate = JOptionPane.showInputDialog(this, "Enter New Appointment Date (YYYY-MM-DD):");
        String newTime = JOptionPane.showInputDialog(this, "Enter New Appointment Time (HH:MM):");

        if (newDate != null && newTime != null && !newDate.isEmpty() && !newTime.isEmpty()) {
            int appointmentId = Integer.parseInt(selectedAppointment.split(" ")[1]);
            customerController.rescheduleAppointment(appointmentId, newDate, newTime, this);
        }
    }

    private void handleCancelAppointment(String customerName) {
        String selectedAppointment = showReschedulingAndCancelingAppointmentDropdown("Cancel", customerName);
        if (selectedAppointment == null) return;

        int appointmentId = Integer.parseInt(selectedAppointment.split(" ")[1]);
        customerController.cancelAppointment(appointmentId, this);
    }

    private void handleCreateAppointment(String customerName) {
        JTextField vehicleTypeField = new JTextField();
        JTextField appointmentDateField = new JTextField();
        JTextField appointmentTimeField = new JTextField();


        Object[] initialFields = {
                "Vehicle Type:", vehicleTypeField,
                "Appointment Date (YYYY-MM-DD):", appointmentDateField,
                "Appointment Time (HH:MM):", appointmentTimeField,
        };

        int initialResult = JOptionPane.showConfirmDialog(
                this,
                initialFields,
                "Create New Appointment",
                JOptionPane.OK_CANCEL_OPTION
        );

        if(initialResult!=JOptionPane.OK_OPTION){
            return; //exit if not ok
        }

        String vehicleType = vehicleTypeField.getText();
        String appointmentDateStr = appointmentDateField.getText();
        String appointmentTimeStr = appointmentTimeField.getText();

        if(vehicleType.isEmpty() || appointmentDateStr.isEmpty() || appointmentTimeStr.isEmpty()){
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE );
            return;
        }

        try{
            ArrayList<String> availableMechanics = customerController.getAvailableMechanics(this, appointmentDateStr, appointmentTimeStr);

            if (availableMechanics.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No mechanics available for selected time and date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JComboBox<String> mechanicDropdown = new JComboBox<>(availableMechanics.toArray(new String[0]));
            Object[] finalFields = {
                    "Select Mechanic: ", mechanicDropdown
            };

            int finalResult = JOptionPane.showConfirmDialog(
                    this,
                    finalFields,
                    "Select Mechanic",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (finalResult != JOptionPane.OK_OPTION) {
                return; // Exit if the user cancels
            }
            String selectedMechanic = (String) mechanicDropdown.getSelectedItem();
            int mechanicID = Integer.parseInt(selectedMechanic.split(" - ")[0]); //extracts id before -

            customerController.createAppointment(customerName,vehicleType,appointmentDateStr,appointmentTimeStr,mechanicID,this);
            JOptionPane.showMessageDialog(this, "Appointment created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid mechanic selection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String showAppointmentDropdown(String actionMessage, String customerName) {
        ArrayList<String> appointments = customerController.getAppointments(customerName, this);
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
    private String showReschedulingAndCancelingAppointmentDropdown(String actionMessage, String customerName) {
        ArrayList<String> appointments = customerController.getAppointments(customerName, this);
        if (appointments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No appointments found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

        ArrayList<String> futureAppointments = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();

        for (String appointment : appointments) {
            String[] parts = appointment.split(" \\| ");
            String dateTimeString = parts[1].split(": ")[1] + " " + parts[2].split(": ")[1];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            try {
                LocalDateTime appointmentDateTime = LocalDateTime.parse(dateTimeString, formatter);
                long hoursDifference = java.time.Duration.between(currentTime, appointmentDateTime).toHours();

                if (hoursDifference >= 24) {
                    futureAppointments.add(appointment);
                }
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date-time format for appointment: " + dateTimeString);
            }
        }

        if (futureAppointments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No appointments found that are at least 24 hours in the future.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

        return (String) JOptionPane.showInputDialog(
                this,
                "Select Appointment to " + actionMessage,
                actionMessage + " Appointment",
                JOptionPane.PLAIN_MESSAGE,
                null,
                futureAppointments.toArray(),
                futureAppointments.get(0)
        );
    }

}
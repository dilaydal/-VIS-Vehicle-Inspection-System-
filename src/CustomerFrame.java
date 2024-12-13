import javax.swing.*;
import java.awt.*;
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
            // Book appointment logic
            JOptionPane.showMessageDialog(this, "Appointment booked successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        rescheduleAppointmentButton.addActionListener(e -> {
            // Reschedule appointment logic
            JOptionPane.showMessageDialog(this, "Appointment rescheduled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        cancelAppointmentButton.addActionListener(e -> {
            // Cancel appointment logic
            JOptionPane.showMessageDialog(this, "Appointment canceled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }}
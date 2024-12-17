package View;

import controller.ManagerController;
import model.MechanicModel;
import model.AppointmentModel;
import model.DatabaseConnection;
import users.User;

import javax.swing.*;
import java.awt.*;

public class ManagerFrame extends JFrame {
    private ManagerController managerController;

    public ManagerFrame(User user) {
        setTitle("Manager Frame - " + user.getUsername());
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize models and controller
        MechanicModel mechanicModel = new MechanicModel(DatabaseConnection.connect());
        AppointmentModel appointmentModel = new AppointmentModel(DatabaseConnection.connect());
        this.managerController = new ManagerController(mechanicModel, appointmentModel);

        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton addRemoveMechanicButton = new JButton("Add/Remove Mechanic");
        addRemoveMechanicButton.addActionListener(e -> openAddRemoveMechanicDialog());

        JButton viewAppointmentsButton = new JButton("View All Appointments");
        viewAppointmentsButton.addActionListener(e -> managerController.viewAllAppointments(this));

        mainPanel.add(addRemoveMechanicButton);
        mainPanel.add(viewAppointmentsButton);

        add(mainPanel);
    }

    private void openAddRemoveMechanicDialog() {
        JDialog dialog = new JDialog(this, "Add/Remove Mechanic", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));

        JButton addMechanicButton = new JButton("Add Mechanic");
        addMechanicButton.addActionListener(e -> openAddMechanicDialog());

        JButton removeMechanicButton = new JButton("Remove Mechanic");
        removeMechanicButton.addActionListener(e -> openRemoveMechanicDialog());

        panel.add(addMechanicButton);
        panel.add(removeMechanicButton);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openAddMechanicDialog() {
        JTextField nameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField contactInfoField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Contact Info:"));
        panel.add(contactInfoField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Mechanic", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            managerController.addMechanic(nameField.getText(), passwordField.getText(), contactInfoField.getText(), this);
        }
    }

    private void openRemoveMechanicDialog() {
        JTextField nameField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Remove Mechanic", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            managerController.removeMechanic(nameField.getText(), this);
        }
    }
}
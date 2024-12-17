package controller;

import model.AuthenticationModel;
import View.MainFrame;
import javax.swing.*;

public class RegisterController {
    private final AuthenticationModel authenticationModel;

    public RegisterController(AuthenticationModel authenticationModel) {
        this.authenticationModel = authenticationModel;
    }

    public void handleRegistration(String username, String password, String fullName, JFrame frame) {
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (authenticationModel.registerCustomer(username, password, fullName)) {
                JOptionPane.showMessageDialog(frame, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); // Close the registration frame
                new MainFrame().setVisible(true); // Open MainFrame
            } else {
                JOptionPane.showMessageDialog(frame, "Username already exists. Please choose another.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error during registration: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
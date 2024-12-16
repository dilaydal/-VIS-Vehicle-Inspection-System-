package controller;

import View.*;
import model.AuthenticationModel;
import utils.AuthResult;


import javax.swing.*;

public class LoginController {
    private AuthenticationModel authenticationModel;

    public LoginController() {
        this.authenticationModel = new AuthenticationModel();
    }

    public void handleLogin(String username, String password, JFrame loginFrame) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginFrame, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AuthResult authResult = authenticationModel.authenticateUser(username, password);

        if (authResult != null) {
            JOptionPane.showMessageDialog(loginFrame, "Login successful! Welcome, " + authResult.getType() + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
            loginFrame.dispose(); // Close the login window

            // Açılan kullanıcı türüne göre ana pencereyi başlat
            openMainWindow(authResult);
        } else {
            JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openMainWindow(AuthResult authResult) {
        String userType = authResult.getType();
        int userId = authResult.getUserId();
        String userName = authResult.getUsername();

        // Kullanıcı tipine göre ana pencereyi aç
        switch (userType) {
            case "customer":
                // Örneğin CustomerMainFrame açılıyor
                new CustomerFrame(userName).setVisible(true);
                break;
            case "mechanic":
                // Örneğin MechanicMainFrame açılıyor
                new MechanicFrame(userName).setVisible(true);
                break;
            case "manager":
                // Örneğin ManagerMainFrame açılıyor
                new ManagerFrame(userName).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unknown user type.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
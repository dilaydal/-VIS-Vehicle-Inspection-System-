package controller;

import model.AuthenticationModel;
import utils.AuthResult;
import users.User;
import View.CustomerFrame;
import View.MechanicFrame;
import View.ManagerFrame;

import javax.swing.*;

public class LoginController {
    private AuthenticationModel authModel;

    public LoginController() {
        this.authModel = new AuthenticationModel(); // Default initialization
    }
    public LoginController(AuthenticationModel authModel) {
        this.authModel = authModel;
    }

    public void handleLogin(String username, String password, JFrame loginFrame) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginFrame, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AuthResult authResult = authModel.authenticateUser(username, password);
        if (authResult != null) {
            User currentUser = authResult.getUser();
            JOptionPane.showMessageDialog(loginFrame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loginFrame.dispose();

            openUserFrame(authResult.getType(), currentUser);
        } else {
            JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openUserFrame(String userType, User user) {
        switch (userType) {
            case "customer":
                new CustomerFrame(user).setVisible(true);
                break;
            case "mechanic":
                new MechanicFrame(user).setVisible(true);
                break;
            case "manager":
                new ManagerFrame(user).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unknown user type.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
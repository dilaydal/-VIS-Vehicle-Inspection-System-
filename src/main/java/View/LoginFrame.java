package main.java.View;
import javax.swing.*;

import main.java.controller.*;
import main.java.model.AuthenticationModel;
import main.java.model.DatabaseConnection;


public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Sign in - Vehicle Inspection System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton= new JButton("Don't have an account?");   

        usernameLabel.setBounds(65, 50, 100, 30);
        usernameField.setBounds(165, 50, 150, 30);
        passwordLabel.setBounds(65, 100, 100, 30);
        passwordField.setBounds(165, 100, 150, 30);
        loginButton.setBounds(115, 150, 150, 30);
        registerButton.setBounds(65,200,250,30);

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(registerButton);
        LoginController loginController = new LoginController(new AuthenticationModel(DatabaseConnection.connect()));

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            loginController.handleLogin(username, password, this);
        });        
        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
         
    }
}

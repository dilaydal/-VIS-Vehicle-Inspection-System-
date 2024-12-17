package View;

import controller.RegisterController;
import model.AuthenticationModel;
import model.DatabaseConnection;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterFrame extends JFrame {
    public RegisterFrame() {
        setTitle("Register - Vehicle Inspection System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel fullNameLabel = new JLabel("Full Name:");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField fullNameField = new JTextField();
        JButton registerButton = new JButton("Register");

        usernameLabel.setBounds(50, 30, 100, 25);
        usernameField.setBounds(150, 30, 200, 25);
        passwordLabel.setBounds(50, 70, 100, 25);
        passwordField.setBounds(150, 70, 200, 25);
        fullNameLabel.setBounds(50, 110, 100, 25);
        fullNameField.setBounds(150, 110, 200, 25);
        registerButton.setBounds(150, 150, 100, 30);

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(fullNameLabel);
        add(fullNameField);
        add(registerButton);

        // Set up the controller
        AuthenticationModel authenticationModel = new AuthenticationModel(DatabaseConnection.connect());
        RegisterController registerController = new RegisterController(authenticationModel);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String fullName = fullNameField.getText().trim();
            registerController.handleRegistration(username, password, fullName, this);
        });

        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new MainFrame().setVisible(true); 
            }
        });
    }
}
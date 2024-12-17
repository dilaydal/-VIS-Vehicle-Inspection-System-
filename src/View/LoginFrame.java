package View;
import javax.swing.*;

import controller.*;
import model.AuthenticationModel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


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
        LoginController loginController = new LoginController(new AuthenticationModel());

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            loginController.handleLogin(username, password, this);
        });



        // Burdaki kodu bozmamak için comment outladım kendim sadece db e bağlantıyı kontrol ettiğim bir kod yazdım alta
        //LoginController ve Authentication kullanarak 



        /*
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.connect()) {
                //need to be filled according to SQL code/queries
                String query = "SELECT password FROM users WHERE username = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, username);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            String storedHash = resultSet.getString("password");
                            if (validatePassword(password, storedHash)) {
                                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                dispose();

                                //need to be filled to open the vis main window

                            } else {
                                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        */

        
        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
         
    }
        

    private boolean validatePassword(String password, String storedHash) {
        String hashedPassword = hashPassword(password);
        return hashedPassword.equals(storedHash);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}

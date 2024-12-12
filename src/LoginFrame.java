import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try (Connection connection = DatabaseConnection.connect()) {
                //need to be filled according to SQL code/queries
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
    }
}

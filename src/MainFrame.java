import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Vehicle Inspection System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.setBounds(100, 80, 200, 40);
        registerButton.setBounds(100, 140, 200, 40);

        add(loginButton);
        add(registerButton);

        loginButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }
}

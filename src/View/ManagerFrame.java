package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ManagerFrame extends JFrame {

    private final Connection connection;

    // Constructor
    public ManagerFrame(String managerName, Connection connection) {
        this.connection = connection;
        // Frame özelliklerini ayarla
        setTitle("Manager Frame " + managerName);
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JButton manageMechanicButton = new JButton("Manage Mechanic");
        manageMechanicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // "Manage Mechanic" butonuna tıklandığında çalışacak kod
                manageMechanic();
            }
        });

        JButton addRemoveMechanicButton = new JButton("Add/Remove Mechanic");
        addRemoveMechanicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // "Add/Remove Mechanic" butonuna tıklandığında çalışacak kod
                addRemoveMechanic();
            }
        });

        JButton viewAllAppointmentsButton = new JButton("View All Appointments");

        viewAllAppointmentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewAllAppointments();
            }
        });

        mainPanel.add(manageMechanicButton);
        mainPanel.add(addRemoveMechanicButton);
        mainPanel.add(viewAllAppointmentsButton);

        add(mainPanel);

        setVisible(true);
    }

    private void manageMechanic() {
        JOptionPane.showMessageDialog(this, "Manage Mechanic button clicked.", "Info", JOptionPane.INFORMATION_MESSAGE);
        // Burada "Manage Mechanic" için gerekli işlemleri başlatabilirsiniz.
    }

    private void addRemoveMechanic() {

        JFrame frame = new JFrame("Add/Remove Mechanic");
        frame.setSize(400,200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JButton addButton = new JButton("Add Mechanic");
        JButton removeButton = new JButton("Remove Mechanic");

        addButton.addActionListener(e -> {
            try {
                addMechanic(connection);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        removeButton.addActionListener(e -> removeMechanic(connection));


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void addMechanic(Connection connection) throws SQLException {
        JTextField mechanicNameField = new JTextField();
        JTextField mechanicIDField = new JTextField();
        JTextField mechanicContactInfoField = new JTextField();
        JTextField mechanicPasswordField = new JTextField();

        JPanel addMechanicPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        addMechanicPanel.add(new JLabel("Mechanic Name:"));
        addMechanicPanel.add(mechanicNameField);

        addMechanicPanel.add(new JLabel("Mechanic ID:"));
        addMechanicPanel.add(mechanicIDField);

        addMechanicPanel.add(new JLabel("Mechanic Contact Info:"));
        addMechanicPanel.add(mechanicContactInfoField);

        addMechanicPanel.add(new JLabel("Mechanic Password:"));
        addMechanicPanel.add(mechanicPasswordField);

        int result = JOptionPane.showConfirmDialog(null, addMechanicPanel, "Add Mechanic",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION){
            String mechanicName = mechanicNameField.getText().trim();
            String mechanicID = mechanicIDField.getText().trim();
            String mechanicContactInfo = mechanicContactInfoField.getText().trim();
            String mechanicPassword = mechanicPasswordField.getText().trim();

            if(mechanicName.isEmpty() || mechanicPassword.isEmpty() || mechanicID.isEmpty() || mechanicContactInfo.isEmpty()){
                JOptionPane.showMessageDialog(null, "Please fill all fields." , "Error", JOptionPane.ERROR_MESSAGE);
                return; //if there is an error, stop.
            }

            String addMechanicQuery = "INSERT INTO Mechanics (mechanicID, userName, password) VALUES (?, ?, ?)";

            PreparedStatement st = connection.prepareStatement(addMechanicQuery);

            // PreparedStatement prevents sql injection, safer than using regular Statement so we use it. It requires adding exception.
            st.setString(1, mechanicID);
            st.setString(2, mechanicName);
            st.setString(3, mechanicPassword);

            int numberOfRowsInserted = st.executeUpdate();
            if(numberOfRowsInserted>0){
                JOptionPane.showMessageDialog(null, "Mechanic addition successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Mechanic addition failed.", "Error", JOptionPane.ERROR_MESSAGE);

            }
            st.close();
        }
    }

    private void removeMechanic(Connection connection){

    }

    private void viewAllAppointments(){
        JOptionPane.showMessageDialog(this, "View All Appointments button is clicked.",
                "Info", JOptionPane.INFORMATION_MESSAGE);
    }
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ManagerFrame("Dilay");
            }
        });
    }

 */

}

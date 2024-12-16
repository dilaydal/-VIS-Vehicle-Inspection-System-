package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerFrame extends JFrame {

    // Constructor
    public ManagerFrame() {
        // Frame özelliklerini ayarla
        setTitle("Manager Frame");
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

        mainPanel.add(manageMechanicButton);
        mainPanel.add(addRemoveMechanicButton);

        add(mainPanel);

        setVisible(true);
    }

    private void manageMechanic() {
        JOptionPane.showMessageDialog(this, "Manage Mechanic button clicked.", "Info", JOptionPane.INFORMATION_MESSAGE);
        // Burada "Manage Mechanic" için gerekli işlemleri başlatabilirsiniz.
    }

    private void addRemoveMechanic() {
        JOptionPane.showMessageDialog(this, "Add/Remove Mechanic button clicked.", "Info",
                JOptionPane.INFORMATION_MESSAGE);
        // Burada "Add/Remove Mechanic" için gerekli işlemleri başlatabilirsiniz.
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ManagerFrame();
            }
        });
    }
}

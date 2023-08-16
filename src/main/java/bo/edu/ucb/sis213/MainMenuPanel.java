import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {
    private ATMFrame atmFrame;

    public MainMenuPanel(ATMFrame atmFrame) {
        this.atmFrame = atmFrame;

        setLayout(new GridLayout(5, 1));

        JButton balanceButton = new JButton("Balance Inquiry");
        JButton depositButton = new JButton("Deposit");
        JButton withdrawalButton = new JButton("Withdrawal");
        JButton changePinButton = new JButton("Change PIN");
        JButton exitButton = new JButton("Exit");

        balanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle balance inquiry logic here
            }
        });

        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle deposit logic here
            }
        });

        withdrawalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle withdrawal logic here
            }
        });

        changePinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle change PIN logic here
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(balanceButton);
        add(depositButton);
        add(withdrawalButton);
        add(changePinButton);
        add(exitButton);
    }
}

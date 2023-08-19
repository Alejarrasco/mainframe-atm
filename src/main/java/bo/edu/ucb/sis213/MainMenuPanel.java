package bo.edu.ucb.sis213;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {
    private ATMFrame atmFrame;

    //Menu principal
    //Asume que el usuario ya se logueó y que el PIN es correcto

    public MainMenuPanel(ATMFrame atmframe) {
        this.atmFrame = atmframe;

        setLayout(new GridLayout(5, 1));

        JButton balanceButton = new JButton("Balance Inquiry");
        JButton depositButton = new JButton("Deposit");
        JButton withdrawalButton = new JButton("Withdrawal");
        JButton changePinButton = new JButton("Change PIN");
        JButton exitButton = new JButton("Exit");

        balanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle balance inquiry logic here
                atmFrame.app.showSaldo();
            }
        });

        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle deposit logic here
                atmFrame.app.realizarDeposito();
            }
        });

        withdrawalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle withdrawal logic here
                atmFrame.app.realizarRetiro();
            }
        });

        changePinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle change PIN logic here
                atmFrame.app.cambiarPIN();
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

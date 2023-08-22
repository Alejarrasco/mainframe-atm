package bo.edu.ucb.sis213.view;

import javax.swing.*;

import bo.edu.ucb.sis213.bl.ATMException;

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

        JButton balanceButton = new JButton("Consulta de Saldo");
        JButton depositButton = new JButton("Deposito");
        JButton withdrawalButton = new JButton("Retiro");
        JButton changePinButton = new JButton("Cambio de PIN");
        JButton exitButton = new JButton("Exit");

        balanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Mostrar el saldo actual en pantalla
                JOptionPane.showMessageDialog(null,"Su saldo actual es: $" + atmFrame.app.getSaldo());
            }
        });

        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle deposit logic here
                Double monto = Double.parseDouble(JOptionPane.showInputDialog("Ingrese la cantidad a depositar: $"));
                try{
                    atmFrame.app.realizarDeposito(monto);
                    JOptionPane.showMessageDialog(null,"Depósito realizado con éxito. Su nuevo saldo es: $" + atmFrame.app.getSaldo());
                }catch(ATMException ex){
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
            }   
        });

        withdrawalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle withdrawal logic here

                Double monto = Double.parseDouble(JOptionPane.showInputDialog("Ingrese la cantidad a depositar: $"));
                try{
                    atmFrame.app.realizarRetiro(monto);
                    JOptionPane.showMessageDialog(null,"Retiro realizado con \u00E9xito. Su nuevo saldo es: $" + atmFrame.app.getSaldo());
                }catch(ATMException ex){
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
            }
        });

        changePinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle change PIN logic here
                int pinIngresado = Integer.parseInt(JOptionPane.showInputDialog("Ingrese su PIN actual: "));
                try {
                    atmFrame.app.validarPINbl(atmFrame.app.getUsername(), pinIngresado);
                } catch (ATMException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                    return ;
                }

                int nuevoPin = Integer.parseInt(JOptionPane.showInputDialog("Ingrese su nuevo PIN: "));
                int confirmacionPin = Integer.parseInt(JOptionPane.showInputDialog("Confirme su nuevo PIN: "));
                try {
                    atmFrame.app.cambiarPIN(nuevoPin, confirmacionPin);
                } catch (ATMException ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                    return;
                }
                JOptionPane.showMessageDialog(null,"PIN actualizado con éxito.");
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

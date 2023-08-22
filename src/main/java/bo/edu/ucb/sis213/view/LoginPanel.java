package bo.edu.ucb.sis213.view;

import javax.swing.*;

import bo.edu.ucb.sis213.bl.ATMException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField pinField;
    private JButton loginButton;
    private ATMFrame atmFrame;

    //Login
    //Primera pantalla que se muestra al iniciar la aplicación

    public LoginPanel(ATMFrame atmframe) {

        this.atmFrame = atmframe;

        setLayout(new GridLayout(3, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("PIN:"));
        pinField = new JPasswordField();
        add(pinField);

        loginButton = new JButton("Login");
        add(loginButton);
        
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle login logic here
                // Verify username and PIN from the database
                // If successful, switch to the main menu panel
                String pass = new String(pinField.getPassword());
                
                if(atmFrame.app.getIntentos()>0){
                    try {
                        atmFrame.app.loginAttempt(usernameField.getText(), Integer.parseInt(pass));
                        atmFrame.app.setApp(usernameField.getText());
                        JOptionPane.showMessageDialog(null, "Bienvenido, "+atmframe.app.getUsuarioNombre());
                        atmFrame.showCard("mainMenu");
                    } catch (ATMException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                        if (ex.getExitStatus()==0)
                            System.exit(0);
                    }
                }
            }
        });
    }
}

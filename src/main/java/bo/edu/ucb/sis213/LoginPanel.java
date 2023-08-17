package bo.edu.ucb.sis213;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField pinField;
    private JButton loginButton;
    private ATMFrame atmFrame;

    public LoginPanel(ATMFrame atmFrame) {

        

        this.atmFrame = atmFrame;

        this.atmFrame.setApp(new App());//Por si acaso

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
                if (app.loginAttempt(usernameField.getText(), Integer.parseInt(pass)))
                    atmFrame.showCard("mainMenu");
            }
        });
    }
}

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
                atmFrame.showCard("mainMenu");
            }
        });
    }
}

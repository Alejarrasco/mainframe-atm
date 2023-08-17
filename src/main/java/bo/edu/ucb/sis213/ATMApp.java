package bo.edu.ucb.sis213;

import javax.swing.*;

public class ATMApp {

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ATMFrame atmFrame = new ATMFrame();
            atmFrame.setVisible(true);
        });
    }
}
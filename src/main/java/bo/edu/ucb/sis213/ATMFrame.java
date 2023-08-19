package bo.edu.ucb.sis213;

import javax.swing.*;
import java.awt.CardLayout;

public class ATMFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public App app =  new App();

    public ATMFrame() {
        setTitle("ATM Banco Casa de Moneda");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(new LoginPanel(this), "login");
        cardPanel.add(new MainMenuPanel(this), "mainMenu");

        add(cardPanel);
    }

    public void showCard(String cardName) {
        cardLayout.show(cardPanel, cardName);
    }

    public void setApp(App app_){
        this.app = app_;
    }
}

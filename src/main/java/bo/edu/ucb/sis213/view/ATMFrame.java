package bo.edu.ucb.sis213.view;

import javax.swing.*;

import bo.edu.ucb.sis213.bl.AppBl;

import java.awt.CardLayout;

public class ATMFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public AppBl app =  new AppBl();

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

    public void setApp(AppBl app_){
        this.app = app_;
    }
}

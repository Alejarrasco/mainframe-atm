import javax.swing.*;

public class ATMFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public ATMFrame() {
        setTitle("ATM Simulation");
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
}

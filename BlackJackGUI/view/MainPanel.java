package BlackJackGUI.view;

import BlackJackGUI.controller.GameControl;
import javax.swing.*;
import java.awt.*; //gbc (grid bag constraints)

public class MainPanel extends JPanel {
    private GameControl controller;

    public MainPanel(GameControl controller) {
        this.controller = controller;
        setBackground(BlackJackGUI.BACKGROUND_COLOR);
        setLayout(new GridBagLayout()); //center components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); //padding
        gbc.fill = GridBagConstraints.HORIZONTAL; //fill horizontally

        JLabel title = new JLabel("♣ Blackjack ♠", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(BlackJackGUI.PRIMARY_BLUE);

        JPanel card = new JPanel(new GridBagLayout()); //main content card
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1), //light gray border
            BorderFactory.createEmptyBorder(40, 60, 40, 60) //internal padding
        ));

        gbc.gridy = 0; //start at row 0
        card.add(title, gbc);
        gbc.gridy++; //move to next row

        JButton playButton = BlackJackGUI.createStyledButton("Play Game");
        JButton statsButton = BlackJackGUI.createStyledButton("Statistics");
        JButton loginButton = BlackJackGUI.createSecondaryButton("Login / Register");
        JButton exitButton = BlackJackGUI.createSecondaryButton("Exit");

        gbc.gridy++; card.add(playButton, gbc); //row 1
        gbc.gridy++; card.add(statsButton, gbc); //row 2
        gbc.gridy++; card.add(loginButton, gbc); //row 3
        gbc.gridy++; card.add(exitButton, gbc); //row 4

        
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (controller.isUserLoggedIn()) {
                    controller.startNewGame(); //start game if logged in
                } else {
                    JOptionPane.showMessageDialog(MainPanel.this, "Please login to play the game!");
                    controller.showPanel("Select"); //redirect to login/register selection
                }
            }
        });

        statsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (controller.isUserLoggedIn()) {
                    controller.showPanel("Stats"); //start game if logged in
                } else {
                    JOptionPane.showMessageDialog(MainPanel.this, "Please login to view statistics!");
                    controller.showPanel("Select"); //redirect to login/register selection
                }
            }
        });

        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.showPanel("Select"); //go to login/register selection
            }
        });
        
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.exitApplication(); //close application
            }
        });

        add(card); //add main panel to panel
    } 
}
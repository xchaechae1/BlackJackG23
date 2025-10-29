package BlackJackGUI.view;

import BlackJackGUI.controller.GameControl;
import javax.swing.*;
import java.awt.*;

public class SelectionPanel extends JPanel {
    public SelectionPanel(GameControl controller) {
        setLayout(new BorderLayout());
        setBackground(BlackJackGUI.BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Blackjack Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(BlackJackGUI.TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0)); //top padding
        add(titleLabel, BorderLayout.NORTH); //add title to top
        
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 15, 15)); 
        buttonPanel.setBackground(BlackJackGUI.BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150));
        
        JButton loginButton = BlackJackGUI.createStyledButton("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.showPanel("Login"); //navigate to login panel
            }
        });
        
        JButton registerButton = BlackJackGUI.createStyledButton("Register");
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.showPanel("Register"); //navigation to register panel
            }
        });
        
        //new: back to main menu button
        JButton backButton = BlackJackGUI.createSecondaryButton("Back to Main Menu");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.showPanel("Main Menu"); //navigation to main menu panel;
            }
        });
        
        JButton exitButton = BlackJackGUI.createSecondaryButton("Exit Game");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.exitApplication(); //close the application
            }
        });
        
        buttonPanel.add(loginButton); //row 1: login
        buttonPanel.add(registerButton); //row 2: register
        buttonPanel.add(backButton); //row 3: back to main menu
        buttonPanel.add(exitButton); //row 4: exit game
        
        add(buttonPanel, BorderLayout.CENTER); //add buttons to center 
    }
}
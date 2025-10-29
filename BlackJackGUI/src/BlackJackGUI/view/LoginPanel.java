package BlackJackGUI.view;

import BlackJackGUI.controller.GameControl;
import javax.swing.*;
import java.awt.*; //gbc (grid bag constraints)
 

public class LoginPanel extends JPanel {
    public LoginPanel(GameControl controller) {
        setBackground(BlackJackGUI.BACKGROUND_COLOR);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(BlackJackGUI.PRIMARY_BLUE);

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(40, 60, 40, 60)
        ));
        //layout components in card panel
        gbc.gridy = 0; card.add(title, gbc);
        gbc.gridy++; card.add(new JLabel("Username:"), gbc);
        gbc.gridy++; card.add(usernameField, gbc);
        gbc.gridy++; card.add(new JLabel("Password:"), gbc);
        gbc.gridy++; card.add(passwordField, gbc);

        JButton loginBtn = BlackJackGUI.createStyledButton("Login");
        JButton backBtn = BlackJackGUI.createSecondaryButton("Back to Selection");
        JButton exitBtn = BlackJackGUI.createSecondaryButton("Exit Game"); //back to menu to exit game

        gbc.gridy++; card.add(loginBtn, gbc);
        gbc.gridy++; card.add(backBtn, gbc);
        gbc.gridy++; card.add(exitBtn, gbc); //add exit button

        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.handleLogin(usernameField.getText(), new String(passwordField.getPassword()));
            }
        });
        
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.showPanel("Select");
            }
        });
        
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.exitApplication();
            }
        });
        
        add(card);
    }
}
package BlackJackGUI.view;

import BlackJackGUI.controller.GameControl;
import javax.swing.*;
import java.awt.*; //gbc (grid bag constraints)

public class RegisterPanel extends JPanel {
    public RegisterPanel(GameControl controller) {
        setBackground(BlackJackGUI.BACKGROUND_COLOR);
        setLayout(new GridBagLayout()); //center components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); //padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL; //fill horizontal space

        JLabel title = new JLabel("Register", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(BlackJackGUI.PRIMARY_BLUE);

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JPasswordField confirmPasswordField = new JPasswordField(15);

        JPanel card = new JPanel(new GridBagLayout()); //main content card
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1), //light gray border
            BorderFactory.createEmptyBorder(40, 60, 40, 60) //internal padding
        ));

        //layout form components in card
        gbc.gridy = 0; card.add(title, gbc); //row 0: title
        gbc.gridy++; card.add(new JLabel("Username:"), gbc); //row 1: username label
        gbc.gridy++; card.add(usernameField, gbc); //row 2: username field
        gbc.gridy++; card.add(new JLabel("Password:"), gbc); //row 3 password label
        gbc.gridy++; card.add(passwordField, gbc); // row 4: password field
        gbc.gridy++; card.add(new JLabel("Confirm Password:"), gbc); //row 5: confirm password label
        gbc.gridy++; card.add(confirmPasswordField, gbc);  //row 6: confirm password field

        JButton registerBtn = BlackJackGUI.createStyledButton("Register");
        JButton backBtn = BlackJackGUI.createSecondaryButton("Back to Selection");
        JButton exitBtn = BlackJackGUI.createSecondaryButton("Exit Game"); 

        gbc.gridy++; card.add(registerBtn, gbc); //row 7: register button
        gbc.gridy++; card.add(backBtn, gbc); //row 8: back button
        gbc.gridy++; card.add(exitBtn, gbc); //row 9: exit button
        
        
        //button listeners
        registerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                controller.handleRegister(username, password, confirmPassword); //pass registration data to controller
            }
        });
        
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.showPanel("Select"); //return to selection screen
            }
        });
        
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.exitApplication(); //close application
            }
        });

        add(card); //add main card to panel
    }
}
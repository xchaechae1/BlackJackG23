package BlackJackGUI.view;

import BlackJackGUI.controller.GameControl;
import BlackJackGUI.model.Stats;
import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {
    private GameControl controller;
    private JTextArea statsArea;
    
    public StatsPanel(GameControl controller) {
        this.controller = controller;
        setBackground(BlackJackGUI.BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10)); //changed to borderlayout

        JLabel title = new JLabel("Player Statistics", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(BlackJackGUI.PRIMARY_BLUE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); //top padding

        statsArea = new JTextArea(15, 40); //increased size
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        statsArea.setMargin(new Insets(10, 10, 10, 10)); //text margin
        
        //initialize with current stats
        updateStatsDisplay();

        JScrollPane scrollPane = new JScrollPane(statsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.setPreferredSize(new Dimension(500, 300)); //set preferred size

        //button panel at bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(BlackJackGUI.BACKGROUND_COLOR);
        
        JButton refreshButton = BlackJackGUI.createSecondaryButton("Refresh Stats");
        JButton backButton = BlackJackGUI.createSecondaryButton("Back to Menu");
        JButton exitButton = BlackJackGUI.createSecondaryButton("Exit Game");

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        buttonPanel.add(exitButton);

        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                updateStatsDisplay(); //refresh stats display
            }
        });
        
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                updateStatsDisplay(); //update before leaving
                controller.showPanel("Main Menu"); //return to main menu
            }
        });
        
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.exitApplication(); //close application
            }
        });

        //main card panel
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1), //border
            BorderFactory.createEmptyBorder(20, 40, 20, 40) //padding
        ));

        card.add(title, BorderLayout.NORTH); //title at top
        card.add(scrollPane, BorderLayout.CENTER); //stats in center
        card.add(buttonPanel, BorderLayout.SOUTH); //buttons at bottom

        add(card); //add card to panel
    }
    
    public void updateStatsDisplay() {
        if (!controller.isUserLoggedIn()) {
            //show guest message
            String guestMessage = 
                "Please login to view statistics!\n\n" +
                "====================\n" +
                "Guest users cannot:\n" +
                "• Track game statistics\n" +
                "• Save progress\n" +
                "• View win history\n" +
                "====================\n\n" +
                "Click 'Back to Menu' and then\n" +
                "'Login / Register' to create an account!";
            
            statsArea.setText(guestMessage);
            return;
        }
        
        //user is logged in - show their stats
        Stats stats = controller.getUserStats();
        
        //use the method names from your current stats class
        int gamesPlayed = stats.getGamesPlayed();
        int wins = stats.getWins();
        int losses = gamesPlayed - wins; //calculate losses
        int blackjacks = 0; //your current stats class doesn't track blackjacks
        int balance = stats.getBalance();
        
        //calculate win rate
        double winRate = 0.0;
        if (gamesPlayed > 0) {
            winRate = (double) wins / gamesPlayed * 100;
        }
        
        String username = controller.getCurrentUser();
        
        //use simple string concatenation to avoid format errors
        String statsText = 
            "Player: " + username + "\n" +
            "====================\n" +
            "Games Played: " + gamesPlayed + "\n" +
            "Wins: " + wins + "\n" +
            "Losses: " + losses + "\n" +
             "Win Rate: " + String.format("%.1f", winRate) + "%\n" +
            "Current Balance: $" + balance + "\n" +
            "====================\n" +
            "Last Updated: " + new java.util.Date().toString();
        
        statsArea.setText(statsText);
        System.out.println("Stats panel updated for user: " + username);
    }
    
    @Override
    public void addNotify() {
        super.addNotify(); //call parent method
        updateStatsDisplay(); //update when panel becomes visible
    }
    
    public void refreshStats() {
        updateStatsDisplay(); //public method to refresh stats
    }
}
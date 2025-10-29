package BlackJackGUI.controller;

import BlackJackGUI.view.*;
import BlackJackGUI.model.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameControl {
    private BlackJackGUI frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private DBManager dbManager;
    private String currentUser;
    private BlackJack game;
    
    
    private GamePanel gamePanel;
    private StatsPanel statsPanel;

    public GameControl(BlackJackGUI frame, JPanel mainPanel, CardLayout cardLayout) {
        this.frame = frame;
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        this.dbManager = new DBManager(); //initialize DBManager
    }
    
    
    public void setPanels(GamePanel gamePanel, StatsPanel statsPanel) {
        this.gamePanel = gamePanel;
        this.statsPanel = statsPanel; //store panels for easy access
    }
    
    public void showPanel(String panelName) {
        //check if user logged in for stats or game panels
        if (("Game".equals(panelName) || "Stats".equals(panelName)) && !isUserLoggedIn()) {
            JOptionPane.showMessageDialog(frame, "Please login to access this feature!");
            showPanel("Select"); //redirects to the login/register panel (SelectionPanel)
            return;
        }
        
        
        if ("Stats".equals(panelName)) {
            refreshStatsPanel(); //refresh stats data when showing stats panel
        }
        
       
        if ("Game".equals(panelName)) {
            if (gamePanel != null) {
                gamePanel.refreshUI(); //refresh game panel ui when showing game panel
            }
        }
        
        cardLayout.show(mainPanel, panelName); //switch to the panel that has been clicked by button (game, stats, login/register)
    }
    
    public void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            dbManager.closeConnections(); //closes db connections
            System.exit(0); //exits application
        }
    }
    
    //checks if the user is logged in
    public boolean isUserLoggedIn() {
        return currentUser != null; //returns true if the currentUser is not null
    }
    
    public void handleLogin(String username, String password) {
        if (dbManager.validateUser(username, password)) {
            this.currentUser = username;

            // Always get balance from database when logging in
            int userBalance = dbManager.getUserBalance(username);

            // Create game with the database balance
            this.game = new BlackJack(userBalance);

            System.out.println("Login successful! Balance from DB: $" + userBalance);
            debugBalanceInfo(); // Debug output

            JOptionPane.showMessageDialog(frame, "Login successful! Balance: $" + userBalance);
            showPanel("Main Menu");
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid username or password.");
        }
    }

    public void handleRegister(String username, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(frame, "Passwords do not match.");
            return;
        }

        if (dbManager.userExists(username)) {
            JOptionPane.showMessageDialog(frame, "Username already exists.");
            return;
        }

        if (dbManager.registerUser(username, password)) {
            this.currentUser = username;

            // Always get starting balance from database
            int userBalance = dbManager.getUserBalance(username);
            this.game = new BlackJack(userBalance);

            System.out.println("Registration successful! Starting balance from DB: $" + userBalance);
            debugBalanceInfo(); // Debug output

            JOptionPane.showMessageDialog(frame, "Registration successful! Starting balance: $" + userBalance);
            showPanel("Main Menu");
        } else {
            JOptionPane.showMessageDialog(frame, "Registration failed.");
        }
    }

    public boolean isDatabaseConnected() {
    return dbManager != null && dbManager.isDatabaseConnected();
    }

    public void debugBalanceInfo() {
        System.out.println("=== BALANCE DEBUG ===");
        System.out.println("User: " + (currentUser != null ? currentUser : "Guest"));
        System.out.println("DB Connected: " + isDatabaseConnected());

        if (game != null) {
            System.out.println("Game Balance: $" + game.getPlayerBalance());
        }

        if (currentUser != null && dbManager != null) {
            int dbBalance = dbManager.getUserBalance(currentUser);
            System.out.println("Database Balance: $" + dbBalance);
        }
        System.out.println("====================");
    }

    public void loginUser(String username, String password) {
        handleLogin(username, password); //calls main login handler
    }

    public void registerUser(String username, String password) {
        JOptionPane.showMessageDialog(frame, "Please use the registration form with password confirmation.");
    }

    //Game methods with login protection
    public void startNewGame() {
        if (!isUserLoggedIn()) { //checks if user is logged in
            JOptionPane.showMessageDialog(frame, "Please login to play the game!");
            showPanel("Select"); //if not redirects to login/register panel
            return;
        }
        
        resetGame(); //resets the game state
        showPanel("Game"); //shows game panel
    }

    public void resetGame() {
        if (currentUser != null && dbManager != null) {
            int currentBalance;

            // Check if database is connected first
            if (isDatabaseConnected()) {
                currentBalance = dbManager.getUserBalance(currentUser);
                System.out.println("Game reset for user: " + currentUser + " with balance from DB: $" + currentBalance);
            } else {
                // Database not connected - use game balance or default
                currentBalance = (game != null) ? game.getPlayerBalance() : 1000;
                System.out.println("Database not connected - using current game balance: $" + currentBalance);
            }

            this.game = new BlackJack(currentBalance);
        } else {
            this.game = new BlackJack(1000); // Default balance for guests
            System.out.println("Game reset with default balance: $1000");
        }

        if (gamePanel != null) {
            gamePanel.resetGameUI();
        }
    }
    
    public void showStatsPanel() {
        if (!isUserLoggedIn()) { //checks if user logged in
            JOptionPane.showMessageDialog(frame, "Please login to view statistics!");
            showPanel("Select"); //if not redirects to login/register
            return;
        }
        
        refreshStatsPanel(); //refresh stats data
        showPanel("Stats"); //show the stats page
    }

    public boolean placeBet(int amount) {
        if (game == null) { //checks if game exists
            JOptionPane.showMessageDialog(frame, "Please start a game first!", "No Game", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (amount <= 0) { //validates the bet amount
            JOptionPane.showMessageDialog(frame, "Bet amount must be greater than zero.", "Invalid Bet", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (amount > game.getPlayerBalance()) { //checks if the user has enough balance
            JOptionPane.showMessageDialog(frame, "You cannot bet more than your current balance.", "Insufficient Balance", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            game.placeBet(amount); //place the bet
            refreshGameUI(); //refreshes/updates the game ui
            
            if (game.hasNaturalBlackjack()) { //check for blackjack
                endGame(); //end the game if blackjack
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void playerHit() {
        if (game != null) {
            game.playerHit(); //player draws card
            refreshGameUI();  //update the game ui
            
            if (game.isGameOver()) { //checks if game has ended
                endGame(); //ends the game
            }
        }
    }

    public void playerStand() {
        if (game != null) {
            game.playerStand(); //player end turn
            refreshGameUI(); //update game ui
            endGame(); //dealer plays cards game ends
        }
    }

    public void doubleDown() {
        if (game != null) {
            game.doubleDown(); //double bet and take one card
            refreshGameUI(); //ends the game
            
            if (game.isGameOver()) { //checks if game has ended
                endGame(); //end game
            }
        }
    }

    public void refreshGameUI() {
        if (gamePanel != null) {
            gamePanel.updateGameState(); //update game with current state;
        }
    }

    
    private void endGame() {
        if (currentUser != null && game != null) {
            boolean gameWon = game.isPlayerWinner();
            boolean hadBlackjack = game.hadBlackjack();
            int currentBalance = game.getPlayerBalance();
            int betAmount = game.getCurrentBet();

            // Only update database if connected
            if (isDatabaseConnected()) {
                dbManager.updateUserStats(currentUser, gameWon, currentBalance, hadBlackjack, betAmount);
                System.out.println("Updated database with new balance: $" + currentBalance);
            } else {
                System.out.println("Database not connected - stats not updated");
            }

            refreshStatsPanel();
            refreshGameUI();

            System.out.println("Game ended - Final balance: $" + currentBalance + 
                             ", Blackjack: " + hadBlackjack + 
                             ", Won: " + gameWon);
            debugBalanceInfo(); // Debug output
        } else {
            System.out.println("Cannot end game - user not logged in or game is null");
        }
    }

    

    private GamePanel getGamePanel() {
        if (gamePanel != null) {
            return gamePanel; //returns stored game panel
        }
        //search for game panel in main panel components
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof GamePanel) {
                return (GamePanel) comp;
            }
        }
        return null; //return null if not found
    }
    
    public void refreshStatsPanel() {
        if (statsPanel != null) {
            statsPanel.refreshStats(); //refresh stats
        }
    }
    
    private StatsPanel getStatsPanel() {
        if (statsPanel != null) {
            return statsPanel; //return stored stats panel data
        }
        
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof StatsPanel) {
                return (StatsPanel) comp; //return stored stats panel reference
            }
        }
        return null;
    }

    // Enhanced balance retrieval for GamePanel
    public int getPlayerBalanceFromDB() {
        if (currentUser != null) {
            return dbManager.getUserBalance(currentUser); //get balance from db
        }
        return 1000; //default balance
    }

    public BlackJack getGame() {
        return game; //return current game instance
    }

    public String getCurrentUser() {
        return currentUser != null ? currentUser : "Guest"; //return username or "Guest"
    }

    public Stats getUserStats() {
        if (currentUser != null) {
            Stats stats = dbManager.getUserStats(currentUser); //get stats from db
            if (stats != null) {
                return stats;
            }
        }
        return new Stats(0, 0, 0, 0, 1000); //default stats for guest
    }
    
    public void logout() {
        if (currentUser != null && game != null) {
            //Save current balance before logging out
            int currentBalance = game.getPlayerBalance();
            dbManager.updateUserBalance(currentUser, currentBalance);
            System.out.println("Saved balance on logout: $" + currentBalance);
        }
        this.currentUser = null; //clear current user
        this.game = null; //clear game instance
        JOptionPane.showMessageDialog(frame, "Logged out successfully!");
        showPanel("Select"); //back to login/register panel
    }

    //tests for blackjacks
    public void testBlackjackTracking() {
        if (currentUser != null) {
            System.out.println("=== BLACKJACK TRACKING TEST ===");
            Stats stats = dbManager.getUserStats(currentUser);
            System.out.println("Current blackjacks for " + currentUser + ": " + stats.getBlackjacks());
            System.out.println("Total games played: " + stats.getGamesPlayed());
            System.out.println("Win/Loss: " + stats.getWins() + "/" + stats.getLosses());
            System.out.println("Current balance: $" + stats.getBalance());
            System.out.println("===============================");
        }
    }
}
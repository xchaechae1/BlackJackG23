package BlackJackGUI.view;

import BlackJackGUI.controller.GameControl;
import BlackJackGUI.model.BlackJack;
import BlackJackGUI.model.Card;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel {
    private GameControl controller;
    private JLabel playerScoreLabel, dealerScoreLabel, balanceLabel, betLabel, resultLabel;
    private JPanel playerHandPanel, dealerHandPanel;
    private JTextField betTextField;
    private JButton hitButton, standButton, doubleButton, placeBetButton, backButton, exitButton;
    private boolean popupShown = false; //track if popup has been shown

    public GamePanel(GameControl controller) {
        this.controller = controller;
        setName("Game");
        initializeUI();
        setupEventHandlers();
        setGameActionsEnabled(false);
        updateBalanceFromDB(); //load initial balance from database
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BlackJackGUI.BACKGROUND_COLOR);

        JPanel topPanel = createTopPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel bottomPanel = createBottomPanel();

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        topPanel.setBackground(BlackJackGUI.BACKGROUND_COLOR);
        
        playerScoreLabel = new JLabel("Player: 0", SwingConstants.CENTER);
        dealerScoreLabel = new JLabel("Dealer: 0", SwingConstants.CENTER);
        balanceLabel = new JLabel("Balance: $0", SwingConstants.CENTER);
        betLabel = new JLabel("Bet: $0", SwingConstants.CENTER);
        resultLabel = new JLabel("Place your bet to start!", SwingConstants.CENTER);
        
        Font infoFont = new Font("Arial", Font.BOLD, 16);
        playerScoreLabel.setFont(infoFont);
        dealerScoreLabel.setFont(infoFont);
        balanceLabel.setFont(infoFont);
        betLabel.setFont(infoFont);
        resultLabel.setFont(infoFont);
        
        resultLabel.setForeground(BlackJackGUI.PRIMARY_BLUE);
        
        topPanel.add(playerScoreLabel);
        topPanel.add(dealerScoreLabel);
        topPanel.add(balanceLabel);
        topPanel.add(betLabel);
        topPanel.add(resultLabel);
        
        return topPanel;
    }

    private JPanel createCenterPanel() {
        playerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        dealerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        playerHandPanel.setBackground(new Color(240, 240, 240));
        dealerHandPanel.setBackground(new Color(240, 240, 240));
        playerHandPanel.setPreferredSize(new Dimension(600, 150));
        dealerHandPanel.setPreferredSize(new Dimension(600, 150));

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBackground(BlackJackGUI.BACKGROUND_COLOR);
        centerPanel.add(createLabeledPanel("DEALER'S HAND", dealerHandPanel));
        centerPanel.add(createLabeledPanel("YOUR HAND", playerHandPanel));
        
        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        bottomPanel.setBackground(BlackJackGUI.BACKGROUND_COLOR);

        //bet panel
        JPanel betPanel = new JPanel(new FlowLayout());
        betPanel.setBackground(BlackJackGUI.BACKGROUND_COLOR);
        betTextField = new JTextField(10);
        betTextField.setText("50");
        placeBetButton = BlackJackGUI.createStyledButton("Place Bet");
        betPanel.add(new JLabel("Bet Amount: $"));
        betPanel.add(betTextField);
        betPanel.add(placeBetButton);

        //game action buttons
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBackground(BlackJackGUI.BACKGROUND_COLOR);
        hitButton = BlackJackGUI.createSecondaryButton("Hit");
        standButton = BlackJackGUI.createSecondaryButton("Stand");
        doubleButton = BlackJackGUI.createSecondaryButton("Double Down");
        actionPanel.add(hitButton);
        actionPanel.add(standButton);
        actionPanel.add(doubleButton);

        //navigation buttons
        JPanel navPanel = new JPanel(new FlowLayout());
        navPanel.setBackground(BlackJackGUI.BACKGROUND_COLOR);
        backButton = BlackJackGUI.createSecondaryButton("Back to Menu");
        exitButton = BlackJackGUI.createSecondaryButton("Exit Game");
        navPanel.add(backButton);
        navPanel.add(exitButton);

        bottomPanel.add(betPanel);
        bottomPanel.add(actionPanel);
        bottomPanel.add(navPanel);
        
        return bottomPanel;
    }

    private JPanel createLabeledPanel(String title, JPanel innerPanel) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BlackJackGUI.BACKGROUND_COLOR);
        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        lbl.setForeground(BlackJackGUI.TEXT_COLOR);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        wrapper.add(lbl, BorderLayout.NORTH);
        wrapper.add(innerPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private void setupEventHandlers() {
        placeBetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    int betAmount = Integer.parseInt(betTextField.getText().trim());
                    if (betAmount > 0) {
                        //disable bet button immediately to prevent multiple clicks
                        placeBetButton.setEnabled(false);
                        betTextField.setEnabled(false);

                        //clear hands immediately
                        clearHandsImmediately();

                        //small delay to ensure ui is cleared before dealing new cards
                        Timer timer = new Timer(50, new java.awt.event.ActionListener() {
                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                //if game is over, reset it automatically before placing bet
                                if (controller.getGame() != null && controller.getGame().isGameOver()) {
                                    controller.resetGame();
                                }
                                controller.placeBet(betAmount);
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        showMessage("Bet amount must be positive!");
                    }
                } catch (NumberFormatException ex) {
                    showMessage("Please enter a valid number for bet amount!");
                }
            }
        });

        hitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.playerHit();
            }
        });

        standButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.playerStand();
            }
        });

        doubleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.doubleDown();
            }
        });

        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.showPanel("Main Menu");
            }
        });

        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.exitApplication();
            }
        });
    }

    //update balance from database using dbmanager
    private void updateBalanceFromDB() {
        if (controller != null && controller.isUserLoggedIn()) {
            //get balance from the current game instance which should be synced with db
            if (controller.getGame() != null) {
                int currentBalance = controller.getGame().getPlayerBalance();
                updateBalance(currentBalance);
            } else {
                //if game is null, we need to create one or get balance directly
                controller.resetGame();
                if (controller.getGame() != null) {
                    int currentBalance = controller.getGame().getPlayerBalance();
                    updateBalance(currentBalance);
                }
            }
        } else {
            //guest mode - show default balance
            updateBalance(1000);
        }
    }

    //ui update methods
    public void setGameActionsEnabled(boolean enabled) {
        hitButton.setEnabled(enabled);
        standButton.setEnabled(enabled);
        doubleButton.setEnabled(enabled && controller.getGame() != null && 
            controller.getGame().getPlayerHand().size() == 2);
    }

    public void updateGameState() {
        if (controller.getGame() != null) {
            BlackJack game = controller.getGame();

            
            if (game.areCardsDealt() || game.isGameOver()) {
                updatePlayerHand(game.getPlayerHand(), game.getPlayerScore());

                boolean showAllDealerCards = game.shouldShowAllDealerCards();
                updateDealerHand(game.getDealerHand(), game.getDealerScore(), showAllDealerCards);
            }

            updateBalance(game.getPlayerBalance());
            updateBet(game.getCurrentBet());

            if (game.isGameOver()) {
                //use result message
                String resultMessage = getImprovedResultMessage(game.getGameResult(), game.isPlayerWinner());
                showResult(resultMessage, game.isPlayerWinner());
                setGameActionsEnabled(false);

                
                betTextField.setEnabled(true);
                placeBetButton.setEnabled(true);
                
                //reset popup flag for next game
                popupShown = false;
            } else if (game.areCardsDealt()) {
                showMessage("Your turn - Hit or Stand?");
                setGameActionsEnabled(true);
            } else {
                showMessage("Place your bet to start!");
                setGameActionsEnabled(false);
            }
        }
    }

    //improved result messages
    private String getImprovedResultMessage(String originalResult, boolean isWin) {
        if (isWin) {
            if (originalResult.contains("Blackjack")) {
                return "Blackjack! You win! Place another bet?";
            } else if (originalResult.contains("win")) {
                return "You win! Place another bet?";
            } else if (originalResult.contains("push") || originalResult.contains("tie")) {
                return "It's a tie! Place another bet?";
            }
        } else {
            if (originalResult.contains("bust")) {
                return "You busted! Place another bet?";
            } else if (originalResult.contains("lose")) {
                return "You lose! Place another bet?";
            } else if (originalResult.contains("Dealer")) {
                return "Dealer wins! Place another bet?";
            }
        }
        
        //fallback to original result with "place another bet?" appended
        return originalResult + " Place another bet?";
    }

    public void updatePlayerHand(List<Card> cards, int score) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                playerHandPanel.removeAll();
                for (Card card : cards) {
                    JLabel cardLabel = createCardLabel(card);
                    playerHandPanel.add(cardLabel);
                }
                playerScoreLabel.setText("Player: " + score);
                playerHandPanel.revalidate();
                playerHandPanel.repaint();
            }
        });
    }

    public void updateDealerHand(List<Card> cards, int score, boolean showAll) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                dealerHandPanel.removeAll();
                for (int i = 0; i < cards.size(); i++) {
                    Card card = cards.get(i);
                    if (i == 0 && !showAll) {
                        JLabel hiddenCard = createHiddenCardLabel();
                        dealerHandPanel.add(hiddenCard);
                    } else {
                        JLabel cardLabel = createCardLabel(card);
                        dealerHandPanel.add(cardLabel);
                    }
                }
                dealerScoreLabel.setText("Dealer: " + (showAll ? score : "?"));
                dealerHandPanel.revalidate();
                dealerHandPanel.repaint();
            }
        });
    }

    private JLabel createCardLabel(Card card) {
        String suitSymbol = getSuitSymbol(card.getSuit());
        String rankSymbol = getRankSymbol(card.getRank());
        
        JLabel label = new JLabel("<html><center>" + rankSymbol + "<br>" + suitSymbol + "</center></html>");
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        label.setPreferredSize(new Dimension(80, 100));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        
        if (card.getSuit().equals("HEARTS") || card.getSuit().equals("DIAMONDS")) {
            label.setForeground(Color.RED);
        } else {
            label.setForeground(Color.BLACK);
        }
        
        return label;
    }

    private void showGameResultPopup(String title, String message, boolean isWin) {
        //only show popup if it hasn't been shown for this game result
        if (popupShown) {
            return;
        }
        
        //mark that popup has been shown
        popupShown = true;
        
        //custom colors
        Color backgroundColor = isWin ? new Color(220, 255, 220) : new Color(255, 220, 220);
        Color titleColor = isWin ? new Color(0, 100, 0) : new Color(139, 0, 0);

        //create custom panel for better styling
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //title label
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(titleColor);

        //message label
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(messageLabel, BorderLayout.CENTER);

        //show the custom dialog
        JOptionPane.showMessageDialog(
            this,
            panel,
            title,
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private String getSuitSymbol(String suit) {
        switch (suit.toUpperCase()) {
            case "HEARTS": return "♥";
            case "DIAMONDS": return "♦";
            case "CLUBS": return "♣";
            case "SPADES": return "♠";
            default: return suit;
        }
    }

    private String getRankSymbol(String rank) {
        switch (rank.toUpperCase()) {
            case "ACE": return "A";
            case "JACK": return "J";
            case "QUEEN": return "Q";
            case "KING": return "K";
            default: return rank;
        }
    }

    private JLabel createHiddenCardLabel() {
        JLabel label = new JLabel("<html><center>???<br>???</center></html>");
        label.setOpaque(true);
        label.setBackground(new Color(70, 130, 180));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        label.setPreferredSize(new Dimension(80, 100));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }

    public void updateBalance(int balance) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                balanceLabel.setText("Balance: $" + balance);
            }
        });
    }

    public void updateBet(int bet) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                betLabel.setText("Bet: $" + bet);
            }
        });
    }

    public void showResult(String message, boolean isWin) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                resultLabel.setText(message);
                resultLabel.setForeground(isWin ? new Color(0, 128, 0) : Color.RED);

                //only show popup for actual game results (not status messages)
                if (!message.equals("Place your bet to start!") && 
                    !message.equals("Your turn - Hit or Stand?") &&
                    !message.equals("Game over! Enter bet for next round.") &&
                    !popupShown) { //check if popup hasn't been shown yet

                    String popupTitle = isWin ? "You Won!" : "You Lost";
                    showGameResultPopup(popupTitle, message, isWin);
                }
            }
        });
    }

    public void showMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                resultLabel.setText(message);
                resultLabel.setForeground(BlackJackGUI.PRIMARY_BLUE);
            }
        });
    }

    //immediate hand clearing to prevent flashing
    public void clearHandsImmediately() {
        //remove all components from hand panels
        playerHandPanel.removeAll();
        dealerHandPanel.removeAll();

        //reset score labels
        playerScoreLabel.setText("Player: 0");
        dealerScoreLabel.setText("Dealer: 0");

        //add empty placeholder panels with proper sizing
        JPanel playerPlaceholder = new JPanel();
        playerPlaceholder.setPreferredSize(new Dimension(600, 150));
        playerPlaceholder.setBackground(new Color(240, 240, 240));
        playerHandPanel.add(playerPlaceholder);

        JPanel dealerPlaceholder = new JPanel();
        dealerPlaceholder.setPreferredSize(new Dimension(600, 150));
        dealerPlaceholder.setBackground(new Color(240, 240, 240));
        dealerHandPanel.add(dealerPlaceholder);

        //force immediate ui update
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
        dealerHandPanel.revalidate();
        dealerHandPanel.repaint();

        //force the entire panel to update
        revalidate();
        repaint();
    }

    public void clearHands() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                playerHandPanel.removeAll();
                dealerHandPanel.removeAll();
                playerScoreLabel.setText("Player: 0");
                dealerScoreLabel.setText("Dealer: 0");
                playerHandPanel.revalidate();
                playerHandPanel.repaint();
                dealerHandPanel.revalidate();
                dealerHandPanel.repaint();
            }
        });
    }

    public void resetGameUI() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                clearHands();
                setGameActionsEnabled(false);
                betTextField.setEnabled(true);
                placeBetButton.setEnabled(true);
                resultLabel.setText("Place your bet to start!");
                resultLabel.setForeground(BlackJackGUI.PRIMARY_BLUE);
                betTextField.setText("50");
                //refresh balance from database when resetting ui
                updateBalanceFromDB();
                
                //reset popup flag for new game
                popupShown = false;
            }
        });
    }

    //method to refresh the entire ui including balance
    public void refreshUI() {
        updateBalanceFromDB();
        resetGameUI();
    }

    //method to refresh just the balance
    public void refreshBalance() {
        updateBalanceFromDB();
    }
}
package BlackJackGUI.view;

import BlackJackGUI.controller.GameControl;
import javax.swing.*;
import java.awt.*;

public class BlackJackGUI extends JFrame {
    private CardLayout cardLayout; //manages panel switching
    private JPanel mainPanel; //main container for card layout
    private GameControl controller; //game logic controller
    
    //colour constants for ui styling
    public static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    public static final Color PRIMARY_BLUE = new Color(100, 149, 237);
    public static final Color HOVER_BLUE = new Color(80, 130, 200);
    public static final Color TEXT_COLOR = new Color(60, 60, 60);
    public static final Color CARD_BACKGROUND = Color.WHITE;
    
    public BlackJackGUI() {
        setTitle("BlackJack");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //center window
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        //initialize controller first
        controller = new GameControl(this, mainPanel, cardLayout);
        
        //create panels passing the controller
        MainPanel mainMenu = new MainPanel(controller);
        SelectionPanel authSelect = new SelectionPanel(controller);
        LoginPanel login = new LoginPanel(controller);
        RegisterPanel register = new RegisterPanel(controller);
        GamePanel game = new GamePanel(controller);
        StatsPanel stats = new StatsPanel(controller);
        
        
        controller.setPanels(game, stats); 
        
        //add to cardlayout
        mainPanel.add(mainMenu, "Main Menu");
        mainPanel.add(authSelect, "Select");
        mainPanel.add(login, "Login");
        mainPanel.add(register, "Register");
        mainPanel.add(game, "Game");
        mainPanel.add(stats, "Stats");
     
        add(mainPanel);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BlackJackGUI();
            }
        });
    }
    
    //creates primary styled button with hover effects
    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(HOVER_BLUE.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(HOVER_BLUE);
                } else {
                    g2.setColor(PRIMARY_BLUE);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
       
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 40));
        
        return button;
    }
    
    //creates secondary styled button with outline
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(220, 220, 220));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(230, 230, 230));
                } else {
                    g2.setColor(CARD_BACKGROUND);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(200, 200, 200));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(TEXT_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 40));
        
        return button;
    }
}
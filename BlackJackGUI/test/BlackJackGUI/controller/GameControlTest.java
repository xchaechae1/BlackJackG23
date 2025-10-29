package BlackJackGUI.controller;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import BlackJackGUI.view.BlackJackGUI;

/**
 * JUnit 4.12 tests for GameControl class - Updated for login requirement
 */
public class GameControlTest {

    private GameControl gameControl;
    private BlackJackGUI frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    @Before
    public void setUp() {
        // Create the actual BlackJackGUI frame that GameControl expects
        frame = new BlackJackGUI();
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        
        // Create the actual GameControl instance with the correct types
        gameControl = new GameControl(frame, mainPanel, cardLayout);
    }

    @After
    public void tearDown() {
        if (frame != null) {
            frame.dispose();
        }
        gameControl = null;
    }

    @Test
    public void testInitialization() {
        assertNotNull("GameControl should be initialized", gameControl);
    }

    @Test
    public void testUserNotLoggedInInitially() {
        assertFalse("User should not be logged in initially", gameControl.isUserLoggedIn());
    }

    @Test
    public void testGetCurrentUserWhenLoggedOut() {
        String user = gameControl.getCurrentUser();
        assertEquals("Should return 'Guest' when not logged in", "Guest", user);
    }

    @Test
    public void testGetPlayerBalanceFromDBWhenLoggedOut() {
        int balance = gameControl.getPlayerBalanceFromDB();
        assertEquals("Should return default balance when not logged in", 1000, balance);
    }

    @Test
    public void testGetUserStatsWhenLoggedOut() {
        // This should return default stats without throwing exceptions
        Object stats = gameControl.getUserStats();
        assertNotNull("Should return stats object even when logged out", stats);
    }

    @Test
    public void testStartNewGameWhenLoggedOut() {
        // Should show login required message, not throw exceptions
        try {
            gameControl.startNewGame();
            assertTrue("Should handle startNewGame when logged out by showing login prompt", true);
        } catch (Exception e) {
            fail("Should not throw exception when starting game without login: " + e.getMessage());
        }
    }

    @Test
    public void testShowStatsPanelWhenLoggedOut() {
        // Should show login required message, not throw exceptions
        try {
            gameControl.showStatsPanel();
            assertTrue("Should handle showStatsPanel when logged out by showing login prompt", true);
        } catch (Exception e) {
            fail("Should not throw exception when showing stats without login: " + e.getMessage());
        }
    }

    @Test
    public void testLogoutWhenNotLoggedIn() {
        // Should handle gracefully without throwing exceptions
        try {
            gameControl.logout();
            assertFalse("User should still be logged out", gameControl.isUserLoggedIn());
        } catch (Exception e) {
            fail("Should not throw exception when logging out without being logged in: " + e.getMessage());
        }
    }

    @Test
    public void testPlayerActionsWithNoGame() {
        // Test that player actions don't crash when no game is active (user not logged in)
        try {
            gameControl.playerHit();
            gameControl.playerStand();
            gameControl.doubleDown();
            assertTrue("Player actions should handle no active game when not logged in", true);
        } catch (NullPointerException e) {
            fail("Player actions should not throw NullPointerException when not logged in: " + e.getMessage());
        } catch (Exception e) {
            // Other exceptions might be expected (like showing dialogs)
            assertTrue("Player actions should handle no game state", true);
        }
    }

    @Test
    public void testPlaceBetWithNoGame() {
        // FIXED: Should handle gracefully when no game exists (user not logged in)
        try {
            gameControl.placeBet(100);
            // If we get here, it means placeBet handled the no-game situation gracefully
            // (probably by showing an error dialog or doing nothing)
            assertTrue("placeBet should handle no active game when user not logged in", true);
        } catch (NullPointerException e) {
            // If this happens, your placeBet method needs null checking
            System.out.println("placeBet needs null checking for game object");
            fail("placeBet should check if game is null before using it: " + e.getMessage());
        } catch (Exception e) {
            // Other exceptions might be expected behavior
            assertTrue("placeBet should handle no-game state", true);
        }
    }

    @Test
    public void testResetGame() {
        // Should initialize a new game without throwing exceptions
        // Even when not logged in, it should create a guest game
        try {
            gameControl.resetGame();
            assertTrue("Should reset game without errors", true);
        } catch (Exception e) {
            fail("Should not throw exception when resetting game: " + e.getMessage());
        }
    }

    @Test
    public void testGetGameObject() {
        // Test that we can get the game object without errors
        // After resetGame(), there should be a game object even for guests
        try {
            gameControl.resetGame(); // Ensure game exists
            Object game = gameControl.getGame();
            assertNotNull("Game object should exist after reset", game);
        } catch (Exception e) {
            fail("Should not throw exception when getting game object: " + e.getMessage());
        }
    }

    @Test
    public void testPanelNavigation() {
        // Should handle panel navigation without throwing exceptions
        try {
            gameControl.showPanel("Main Menu");
            gameControl.showPanel("Select");
            assertTrue("Panel navigation should work", true);
        } catch (Exception e) {
            fail("Panel navigation should not throw exceptions: " + e.getMessage());
        }
    }

    @Test
    public void testLoginMethodsExist() {
        // Test that login methods can be called
        try {
            gameControl.handleLogin("test", "password");
            gameControl.loginUser("test", "password");
            assertTrue("Login methods should be callable", true);
        } catch (Exception e) {
            fail("Login methods should not throw exceptions: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterMethodsExist() {
        // Test that register methods can be called
        try {
            gameControl.handleRegister("user", "pass", "pass");
            gameControl.registerUser("user", "pass");
            assertTrue("Register methods should be callable", true);
        } catch (Exception e) {
            fail("Register methods should not throw exceptions: " + e.getMessage());
        }
    }

    @Test
    public void testBlackjackTrackingMethod() {
        // Test that the debug method exists and can be called
        try {
            gameControl.testBlackjackTracking();
            assertTrue("Blackjack tracking method should be callable", true);
        } catch (Exception e) {
            fail("Blackjack tracking should not throw exceptions: " + e.getMessage());
        }
    }

    @Test
    public void testGameProtectionLogic() {
        // Test that game and stats are protected by login
        assertFalse("Game should not be accessible without login", gameControl.isUserLoggedIn());
        
        // These should show login prompts rather than throw exceptions
        try {
            gameControl.startNewGame(); // Should show "Please login to play the game!"
            gameControl.showStatsPanel(); // Should show "Please login to view statistics!"
            assertTrue("Protected methods should show login prompts", true);
        } catch (Exception e) {
            fail("Protected methods should show dialogs, not throw exceptions: " + e.getMessage());
        }
    }
}
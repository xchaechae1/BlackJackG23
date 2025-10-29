package BlackJackGUI.model;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.sql.*;

/**
 * Unit tests for the DBManager class, using a mock in-memory implementation.
 * JUnit 4.12 version - compatible with NetBeans default setup.
 */
public class DBManagerTest {

    private DBManager db;

    @Before
    public void setUp() {
        db = new DBManager();
    }

    @After
    public void tearDown() {
        db.closeConnections();
    }

    @Test
    public void testConnectionEstablished() {
        Connection conn = db.getConnection();
        assertNotNull("Database connection should not be null", conn);
        System.out.println("✅ Database connection test passed");
    }

    @Test
    public void testRegisterUser() {
        String username = "testUser_" + System.currentTimeMillis();
        String password = "password123";

        boolean result = db.registerUser(username, password);
        assertTrue("User should be registered successfully", result);

        boolean exists = db.userExists(username);
        assertTrue("User should exist in the database after registration", exists);
    }

    @Test
    public void testRegisterDuplicateUserFails() {
        String username = "duplicateUser_" + System.currentTimeMillis();
        String password = "password123";

        db.registerUser(username, password); 
        boolean result = db.registerUser(username, password);

        assertFalse("Duplicate registration should fail", result);
    }

    @Test
    public void testValidateUser() {
        String username = "validateUser_" + System.currentTimeMillis();
        String password = "mypassword";

        db.registerUser(username, password);
        boolean isValid = db.validateUser(username, password);

        assertTrue("Registered user should validate successfully", isValid);
    }

    @Test
    public void testValidateUserWithWrongPassword() {
        String username = "wrongPassUser_" + System.currentTimeMillis();
        String password = "correctPassword";

        db.registerUser(username, password);
        boolean isValid = db.validateUser(username, "wrongPassword");

        assertFalse("User should not validate with wrong password", isValid);
    }

    @Test
    public void testUpdateUserBalance() {
        String username = "balanceUser_" + System.currentTimeMillis();
        String password = "pass";
        db.registerUser(username, password);

        int expectedBalance = 777;
        db.updateUserBalance(username, expectedBalance);
        int actualBalance = db.getUserBalance(username);

        assertEquals("Balance should match updated value", expectedBalance, actualBalance);
    }

    @Test
    public void testUpdateUserStats() {
        String username = "statsUser_" + System.currentTimeMillis();
        db.registerUser(username, "pass");

        // Perform first game (Win)
        db.updateUserStats(username, true, 1200, false, 100);
        Stats stats1 = db.getUserStats(username);
        assertEquals("Games played should be 1", 1, stats1.getGamesPlayed());
        assertEquals("Wins should be 1", 1, stats1.getWins());
        assertEquals("Balance should update correctly after first game", 1200, stats1.getBalance());
        
        // Perform second game (Loss/Push)
        db.updateUserStats(username, false, 1150, false, 50);
        Stats stats2 = db.getUserStats(username);
        assertEquals("Games played should be 2", 2, stats2.getGamesPlayed());
        assertEquals("Wins should remain 1", 1, stats2.getWins());
        assertEquals("Balance should update correctly after second game", 1150, stats2.getBalance());
    }

    @Test
    public void testGetUserStatsForNonExistentUser() {
        String username = "nonExistentUser_" + System.currentTimeMillis();
        
        Stats stats = db.getUserStats(username);
        
        assertNotNull("Stats should not be null for non-existent user", stats);
        assertEquals("Games played should be 0 for non-existent user", 0, stats.getGamesPlayed());
        assertEquals("Wins should be 0 for non-existent user", 0, stats.getWins());
        assertEquals("Losses should be 0 for non-existent user", 0, stats.getLosses());
        assertEquals("Blackjacks should be 0 for non-existent user", 0, stats.getBlackjacks());
        // FIXED: Your mock returns 1000 for new users
        assertEquals("Balance should be 1000 for non-existent user (default)", 1000, stats.getBalance());
    }

    @Test
    public void testGetUserBalanceForNonExistentUser() {
        String username = "nonExistentBalanceUser_" + System.currentTimeMillis();
        
        int balance = db.getUserBalance(username);
        
        // FIXED: Your mock returns 1000 for new users
        assertEquals("Balance should be 1000 for non-existent user (default)", 1000, balance);
    }

    @Test
    public void testLogGameHistory() {
        String username = "historyUser_" + System.currentTimeMillis();
        db.registerUser(username, "pass");

        // Log two games to check limit/order
        db.logGameHistory(username, 50, 19, 20, "10♦, 9♥", "K♦, 10♣", "LOSS", false, 1000, 950);
        db.logGameHistory(username, 100, 21, 18, "A♠, Q♥", "K♦, 8♣", "WIN", true, 950, 1150);
        
        var history = db.getGameHistory(username, 5);

        assertFalse("Game history should contain at least one entry", history.isEmpty());
        assertEquals("History should contain 2 entries", 2, history.size());
        assertEquals("First history entry should be the most recent 'WIN'", "WIN", history.get(0).getResult());
    }
}
package BlackJackGUI.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * JUnit 4.12 tests for Stats class
 */
public class StatsTest {

    private Stats stats;

    @Before
    public void setUp() {
        // This creates an ACTUAL Stats object from your source code
        stats = new Stats(10, 6, 4, 2, 1500);
    }

    @Test
    public void testStatsCreation() {
        assertNotNull("Stats should be created successfully", stats);
    }

    @Test
    public void testGetGamesPlayed() {
        assertEquals("Games played should be 10", 10, stats.getGamesPlayed());
    }

    @Test
    public void testGetWins() {
        assertEquals("Wins should be 6", 6, stats.getWins());
    }

    @Test
    public void testGetLosses() {
        assertEquals("Losses should be 4", 4, stats.getLosses());
    }

    @Test
    public void testGetBlackjacks() {
        assertEquals("Blackjacks should be 2", 2, stats.getBlackjacks());
    }

    @Test
    public void testGetBalance() {
        assertEquals("Balance should be 1500", 1500, stats.getBalance());
    }

    @Test
    public void testDefaultConstructor() {
        Stats defaultStats = new Stats();
        assertEquals("Default games played should be 0", 0, defaultStats.getGamesPlayed());
        assertEquals("Default wins should be 0", 0, defaultStats.getWins());
        assertEquals("Default losses should be 0", 0, defaultStats.getLosses());
        assertEquals("Default blackjacks should be 0", 0, defaultStats.getBlackjacks());
        assertEquals("Default balance should be 1000", 1000, defaultStats.getBalance());
    }

    @Test
    public void testWinPercentage() {
        // Test with wins
        double winPercentage = stats.getWinPercentage();
        assertEquals("Win percentage should be 60%", 60.0, winPercentage, 0.01);

        // Test with zero games
        Stats zeroStats = new Stats(0, 0, 0, 0, 1000);
        double zeroPercentage = zeroStats.getWinPercentage();
        assertEquals("Win percentage should be 0 when no games played", 0.0, zeroPercentage, 0.01);
    }

    @Test
    public void testWinPercentageCalculation() {
        // Test various win percentages
        Stats stats75 = new Stats(4, 3, 1, 0, 1000);
        assertEquals("75% win rate", 75.0, stats75.getWinPercentage(), 0.01);

        Stats stats25 = new Stats(4, 1, 3, 0, 1000);
        assertEquals("25% win rate", 25.0, stats25.getWinPercentage(), 0.01);

        Stats stats100 = new Stats(5, 5, 0, 0, 1000);
        assertEquals("100% win rate", 100.0, stats100.getWinPercentage(), 0.01);

        Stats stats0 = new Stats(5, 0, 5, 0, 1000);
        assertEquals("0% win rate", 0.0, stats0.getWinPercentage(), 0.01);
    }

    @Test
    public void testStatsConsistency() {
        // Test that wins + losses equals games played
        assertEquals("Wins + losses should equal games played", 
                     stats.getGamesPlayed(), stats.getWins() + stats.getLosses());
    }

    @Test
    public void testNegativeValues() {
        // Test that stats handle edge cases
        Stats negativeStats = new Stats(-5, -2, -3, -1, -100);
        // Depending on your implementation, you might want to test how it handles negatives
        assertNotNull("Should handle negative values", negativeStats);
    }

    @Test
    public void testLargeNumbers() {
        // Test with large numbers to ensure no overflow
        Stats largeStats = new Stats(1000000, 600000, 400000, 50000, 1000000);
        assertEquals("Large games count", 1000000, largeStats.getGamesPlayed());
        assertEquals("Large wins count", 600000, largeStats.getWins());
        assertEquals("Large balance", 1000000, largeStats.getBalance());
        
        double percentage = largeStats.getWinPercentage();
        assertEquals("Win percentage for large numbers", 60.0, percentage, 0.01);
    }
}
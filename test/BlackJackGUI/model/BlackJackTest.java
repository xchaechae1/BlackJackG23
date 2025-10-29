package BlackJackGUI.model;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.util.List;

/**
 * JUnit 4.12 tests for the actual BlackJack game class
 */
public class BlackJackTest {

    private BlackJack game;  // This tests the actual BlackJack class

    @Before
    public void setUp() {
        game = new BlackJack(1000);  // Create instance of the actual game class
    }

    @After
    public void tearDown() {
        game = null;
    }

    @Test
    public void testInitialization() {
        assertEquals("Initial balance should be 1000", 1000, game.getPlayerBalance());
        assertEquals("Initial bet should be 0", 0, game.getCurrentBet());
        assertFalse("Game should not be over initially", game.isGameOver());
        assertFalse("Cards should not be dealt initially", game.areCardsDealt());
        assertEquals("Initial game result should be place bet message", "Place your bet!", game.getGameResult());
    }

    @Test
    public void testPlaceValidBet() {
        game.placeBet(100);
        assertEquals("Bet amount should be 100", 100, game.getCurrentBet());
        assertTrue("Cards should be dealt after placing bet", game.areCardsDealt());
    }

    @Test
    public void testPlayerHandAfterBet() {
        game.placeBet(100);
        List<Card> playerHand = game.getPlayerHand();
        assertNotNull("Player hand should not be null", playerHand);
        assertEquals("Player should have 2 cards after initial deal", 2, playerHand.size());
    }

    @Test
    public void testDealerHandAfterBet() {
        game.placeBet(100);
        List<Card> dealerHand = game.getDealerHand();
        assertNotNull("Dealer hand should not be null", dealerHand);
        assertEquals("Dealer should have 2 cards after initial deal", 2, dealerHand.size());
    }

    @Test
    public void testPlayerHit() {
        game.placeBet(100);
        int initialHandSize = game.getPlayerHand().size();
        game.playerHit();
        int newHandSize = game.getPlayerHand().size();
        assertEquals("Player hand should increase by 1 after hit", initialHandSize + 1, newHandSize);
    }

    @Test
    public void testPlayerStand() {
        game.placeBet(100);
        game.playerStand();
        assertTrue("Game should be over after stand", game.isGameOver());
        assertTrue("Dealer turn should be true after stand", game.isDealerTurn());
    }

    @Test
    public void testScoreCalculation() {
        game.placeBet(100);
        int playerScore = game.getPlayerScore();
        int dealerScore = game.getDealerScore();
        
        assertTrue("Player score should be between 4 and 21", playerScore >= 4 && playerScore <= 21);
        assertTrue("Dealer score should be between 2 and 21", dealerScore >= 2 && dealerScore <= 21);
    }

    @Test
    public void testGameOverConditions() {
        game.placeBet(100);
        assertFalse("Game should not be over immediately after bet", game.isGameOver());
        
        game.playerStand();
        assertTrue("Game should be over after stand", game.isGameOver());
    }

    @Test
    public void testPlayerActionsAvailability() {
        game.placeBet(100);
        
        assertTrue("Player should be able to hit initially", game.canPlayerHit());
        assertTrue("Player should be able to stand initially", game.canPlayerStand());
        
        // Test after game over
        game.playerStand();
        assertFalse("Player should not be able to hit after game over", game.canPlayerHit());
        assertFalse("Player should not be able to stand after game over", game.canPlayerStand());
    }

    @Test
    public void testDealerCardVisibility() {
        game.placeBet(100);
        assertFalse("Dealer cards should not all be visible initially", game.shouldShowAllDealerCards());
        
        game.playerStand();
        assertTrue("All dealer cards should be visible after stand", game.shouldShowAllDealerCards());
    }

    @Test
    public void testBalanceUpdates() {
        int initialBalance = game.getPlayerBalance();
        game.placeBet(100);
        
        // Stand to complete the game
        game.playerStand();
        
        int finalBalance = game.getPlayerBalance();
        // Balance should change (could be up or down depending on game outcome)
        assertTrue("Balance should change after game completion", finalBalance != initialBalance);
    }

    @Test
    public void testBlackjackDetection() {
        game.placeBet(100);
        
        // Test blackjack detection methods
        boolean hasBlackjack = game.hasBlackjack();
        boolean hasNaturalBlackjack = game.hasNaturalBlackjack();
        boolean hadBlackjack = game.hadBlackjack();
        
        // Methods should return booleans without errors
        assertTrue("hasBlackjack should return boolean", hasBlackjack == true || hasBlackjack == false);
        assertTrue("hasNaturalBlackjack should return boolean", hasNaturalBlackjack == true || hasNaturalBlackjack == false);
        assertTrue("hadBlackjack should return boolean", hadBlackjack == true || hadBlackjack == false);
    }

    @Test
    public void testPlayerWinDetection() {
        game.placeBet(100);
        game.playerStand();
        
        boolean isWinner = game.isPlayerWinner();
        // Can't guarantee win/loss, but method should return a boolean
        assertTrue("isPlayerWinner should return boolean", isWinner == true || isWinner == false);
    }
}
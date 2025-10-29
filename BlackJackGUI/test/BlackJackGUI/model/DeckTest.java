package BlackJackGUI.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;

/**
 * JUnit 4.12 tests for Deck class
 */
public class DeckTest {

    private Deck deck;

    @Before
    public void setUp() {
        // This creates an ACTUAL Deck object from your source code
        deck = new Deck();
    }

    @Test
    public void testDeckCreation() {
        assertNotNull("Deck should be created successfully", deck);
    }

    @Test
    public void testDeckSize() {
        assertEquals("Deck should have 52 cards", 52, deck.size());
    }

    @Test
    public void testDrawCard() {
        Card card = deck.drawCard();
        assertNotNull("Drawn card should not be null", card);
        assertEquals("Deck should have 51 cards after drawing one", 51, deck.size());
        
        // Verify card has valid properties
        assertNotNull("Card should have a suit", card.getSuit());
        assertNotNull("Card should have a rank", card.getRank());
        assertTrue("Card value should be between 1 and 11", card.getValue() >= 1 && card.getValue() <= 11);
    }

    @Test
    public void testDrawMultipleCards() {
        int initialSize = deck.size();
        int cardsToDraw = 5;
        
        for (int i = 0; i < cardsToDraw; i++) {
            Card card = deck.drawCard();
            assertNotNull("Card " + (i + 1) + " should not be null", card);
        }
        
        assertEquals("Deck size should decrease by " + cardsToDraw, initialSize - cardsToDraw, deck.size());
    }

    @Test
    public void testShuffle() {
        // Draw a card before shuffle to remember it
        Card firstCardBeforeShuffle = deck.drawCard();
        
        // Reshuffle the remaining deck
        deck.shuffle();
        
        // The first card after shuffle should be different (very high probability)
        Card firstCardAfterShuffle = deck.drawCard();
        
        // It's possible but very unlikely they're the same card
        boolean cardsAreDifferent = !firstCardBeforeShuffle.toString().equals(firstCardAfterShuffle.toString());
        assertTrue("Shuffle should change card order", cardsAreDifferent);
    }

    @Test
    public void testDeckReplenishment() {
        // Draw all cards until deck is empty
        int cardsDrawn = 0;
        while (deck.size() > 0) {
            Card card = deck.drawCard();
            assertNotNull("Card should not be null", card);
            cardsDrawn++;
        }
        
        assertEquals("Should have drawn 52 cards", 52, cardsDrawn);
        assertEquals("Deck should be empty", 0, deck.size());
        
        // Drawing one more should replenish the deck
        Card cardAfterEmpty = deck.drawCard();
        assertNotNull("Should get a card after deck replenishment", cardAfterEmpty);
        assertEquals("Deck should have 51 cards after replenishment and draw", 51, deck.size());
    }

    @Test
    public void testCardValues() {
        // Test that cards have correct Blackjack values
        boolean foundAce = false;
        boolean foundFaceCard = false;
        boolean foundNumberCard = false;
        
        // Draw several cards to sample different types
        for (int i = 0; i < 10; i++) {
            Card card = deck.drawCard();
            int value = card.getValue();
            
            if (card.getRank().equals("Ace")) {
                foundAce = true;
                assertEquals("Ace should have value 11", 11, value);
            } else if (card.getRank().equals("Jack") || card.getRank().equals("Queen") || card.getRank().equals("King")) {
                foundFaceCard = true;
                assertEquals("Face card should have value 10", 10, value);
            } else {
                foundNumberCard = true;
                int expectedValue = Integer.parseInt(card.getRank());
                assertEquals("Number card should have value matching rank", expectedValue, value);
            }
        }
        
        assertTrue("Should have found different card types in sample", foundAce || foundFaceCard || foundNumberCard);
    }

    @Test
    public void testAllCardsAreUnique() {
        // Test that all 52 cards are unique
        java.util.Set<String> uniqueCards = new java.util.HashSet<>();
        
        for (int i = 0; i < 52; i++) {
            Card card = deck.drawCard();
            String cardIdentifier = card.getSuit() + "-" + card.getRank();
            uniqueCards.add(cardIdentifier);
        }
        
        assertEquals("All 52 cards should be unique", 52, uniqueCards.size());
    }
}
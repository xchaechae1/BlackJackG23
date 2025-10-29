/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BlackJackGUI.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * JUnit 4.12 tests for Card class
 */
public class CardTest {

    private Card card;

    @Before
    public void setUp() {
        // This creates an ACTUAL Card object from your source code
        card = new Card("Hearts", "Ace", 11);
    }

    @Test
    public void testCardCreation() {
        assertNotNull("Card should be created successfully", card);
    }

    @Test
    public void testGetSuit() {
        assertEquals("Suit should be Hearts", "Hearts", card.getSuit());
    }

    @Test
    public void testGetRank() {
        assertEquals("Rank should be Ace", "Ace", card.getRank());
    }

    @Test
    public void testGetValue() {
        assertEquals("Value should be 11", 11, card.getValue());
    }

    @Test
    public void testToString() {
        String result = card.toString();
        assertNotNull("toString should not return null", result);
        assertTrue("toString should contain rank", result.contains("Ace"));
        assertTrue("toString should contain suit", result.contains("Hearts"));
    }

    @Test
    public void testDifferentCard() {
        Card kingCard = new Card("Spades", "King", 10);
        assertEquals("King card suit should be Spades", "Spades", kingCard.getSuit());
        assertEquals("King card rank should be King", "King", kingCard.getRank());
        assertEquals("King card value should be 10", 10, kingCard.getValue());
    }

    @Test
    public void testNumberCard() {
        Card sevenCard = new Card("Diamonds", "7", 7);
        assertEquals("7 card suit should be Diamonds", "Diamonds", sevenCard.getSuit());
        assertEquals("7 card rank should be 7", "7", sevenCard.getRank());
        assertEquals("7 card value should be 7", 7, sevenCard.getValue());
    }

    @Test
    public void testFaceCardValues() {
        // Test that face cards have value 10
        Card king = new Card("Clubs", "King", 10);
        Card queen = new Card("Hearts", "Queen", 10);
        Card jack = new Card("Spades", "Jack", 10);
        
        assertEquals("King should have value 10", 10, king.getValue());
        assertEquals("Queen should have value 10", 10, queen.getValue());
        assertEquals("Jack should have value 10", 10, jack.getValue());
    }

    @Test
    public void testAceCard() {
        Card ace = new Card("Diamonds", "Ace", 11);
        assertEquals("Ace should have value 11", 11, ace.getValue());
        assertEquals("Ace rank should be Ace", "Ace", ace.getRank());
    }
}
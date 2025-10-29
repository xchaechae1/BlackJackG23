package BlackJackGUI.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards; //list to hold all cards in the deck
    private final String[] SUITS = {"Hearts", "Diamonds", "Clubs", "Spades"}; //all possible suits
    private final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"}; //all possible ranks

    public Deck() {
        cards = new ArrayList<>(); //initialize card list
        initializeDeck(); //create and populate the deck
        shuffle(); //shuffle the deck after creation
    }

    private void initializeDeck() {
        cards.clear(); //clear any existing cards first (for reshuffling)
        for (String suit : SUITS) { //loop through all suits
            for (String rank : RANKS) { //loop through all ranks for each suit
                int value; //card value for blackjack
                switch (rank) {
                    case "Jack": case "Queen": case "King": //face cards are worth 10
                        value = 10;
                        break;
                    case "Ace":
                        value = 11;
                        break;
                    default:
                        value = Integer.parseInt(rank);
                }
                cards.add(new Card(suit, rank, value)); //create card and add to deck
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards); //randomize card order using collections.shuffle
        System.out.println("Deck shuffled! " + cards.size() + " cards remaining.");
    }

    public Card drawCard() {
        if (cards.isEmpty()) { //check if deck is empty
            System.out.println("Deck empty! Replenishing and reshuffling...");
            initializeDeck(); //recreate the deck
            shuffle(); //shuffle the deck
        }
        Card drawnCard = cards.remove(0); //remove and return the top card
        System.out.println("Drew: " + drawnCard + " (" + cards.size() + " cards left)");
        return drawnCard;
    }

    public int size() {
        return cards.size(); //return number of cards remaining in deck
    }
}
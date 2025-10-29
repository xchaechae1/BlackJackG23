package BlackJackGUI.model;


public class Card {
    private String suit; //the suit of the card
    private String rank; //the rank of the card
    private int value;   //the value of the card in Blackjack

   
    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }

    //get the value of cards
    public int getValue() {
        return value;
    }

    //get the suit of card
    public String getSuit() {
        return suit;
    }

    //get the rank of card
    public String getRank() {
        return rank;
    }

    //return a string to show what card recieved
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

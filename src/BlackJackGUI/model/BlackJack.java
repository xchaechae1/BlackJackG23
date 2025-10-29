/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BlackJackGUI.model;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author nicho
 */
public class BlackJack {
    private Deck deck;
    private List<Card> playerHand;
    private List<Card> dealerHand;
    private int playerScore;
    private int dealerScore;
    private boolean gameOver;
    private String gameResult;
    private int betAmount;
    private int playerBalance;
    private boolean dealerTurn;
    private boolean cardsDealt;
    private boolean hadBlackjack;

    public BlackJack(int initialBalance) {
        this.playerBalance = initialBalance;
        this.cardsDealt = false;
        this.hadBlackjack = false;
        initializeGame(); //initialize game
    }

    public void initializeGame() {
        this.deck = new Deck(); //creates new deck
        playerHand = new ArrayList<>(); //initialize player hand
        dealerHand = new ArrayList<>(); //initialize dealer hand
        playerScore = 0; //reset player score
        dealerScore = 0; //reset dealer score
        gameOver = false; //game is active
        gameResult = "Place your bet!"; //default message
        betAmount = 0; //reset bet amount
        dealerTurn = false; //player's turn first
        cardsDealt = false; //cards not dealt yet
        hadBlackjack = false; //reset blackjack flag
        
        System.out.println("=== NEW GAME STARTED ===");
    }

    public void placeBet(int amount) {
        if (amount <= playerBalance && amount > 0) { //validate bet amount
            betAmount = amount; //set bet amount
            
            if (!cardsDealt) { //if cards are not yet dealt
                dealInitialCards(); //deal initial cards
                cardsDealt = true; //mark cards as dealt
            }
            
            gameResult = "Bet placed: $" + amount; //update game message
            System.out.println("Bet placed: $" + amount + ", cards dealt");
            
            //check for blackjack immediately after dealing
            checkForBlackjack(); //check if player has blackjack
        }
    }

    private void dealInitialCards() {
        playerHand.add(deck.drawCard()); //first player card dealt
        dealerHand.add(deck.drawCard()); //first dealer card (face down)
        playerHand.add(deck.drawCard()); //second player card dealt
        dealerHand.add(deck.drawCard()); //second dealer card (face up)
        calculateScores(); //calculate the initial scores
        
        //debug output for card dealing
        System.out.println("Player cards: " + playerHand.get(0).getRank() + " of " + playerHand.get(0).getSuit() + 
                          ", " + playerHand.get(1).getRank() + " of " + playerHand.get(1).getSuit());
        System.out.println("Initial player score: " + playerScore);
    }

    //checks if player has blackjack
    private void checkForBlackjack() {
        boolean isBlackjack = hasBlackjack(); //check for blackjack
        this.hadBlackjack = isBlackjack; //store blackjack status
        
        if (isBlackjack) {
            System.out.println("BLACKJACK DETECTED! Player has 21!");
            processBlackjackPayout(); //process blackjack win instantly
        } else {
            System.out.println("No blackjack - player score: " + playerScore); 
        }
    }

    public void processBlackjackPayout() {
        if (hasBlackjack() && !gameOver) { //if blackjack and game not over
            double blackjackPayout = betAmount * 1.5; //calculate 3:2 payout
            playerBalance += (int)blackjackPayout; //add winnings to player balance
            gameOver = true; //end game
            gameResult = "Blackjack! You win $" + (int)blackjackPayout + "!"; //set result message
            hadBlackjack = true; //confirm blackjack has occured
            System.out.println("Blackjack payout processed: $" + (int)blackjackPayout); 
        }
    }

    //checks if player has blackjack
    public boolean hasBlackjack() {
        if (!cardsDealt) { //cards must be dealt first
            return false;
        }
        
        //checks if player has exactly 21
        boolean isBlackjack = playerScore == 21;
        
        if (isBlackjack) {
            System.out.println("BLACKJACK CONFIRMED: Score = 21 with " + playerHand.size() + " cards");
            //show the cards that made 21
            StringBuilder cards = new StringBuilder();
            for (Card card : playerHand) {
                if (cards.length() > 0) cards.append(" + ");
                cards.append(card.getRank());
            }
            System.out.println("Cards: " + cards.toString() + " = 21");
        }
        
        return isBlackjack;
    }

    //check for natural blackjack
    public boolean hasNaturalBlackjack() {
        //still check for natural blackjack (Ace + 10 in first 2 cards)
        if (!cardsDealt || playerHand.size() != 2) {
            return false;
        }
        
        Card card1 = playerHand.get(0); //first playercard
        Card card2 = playerHand.get(1); //second player card
        
        boolean hasAce = card1.getRank().equals("Ace") || card2.getRank().equals("Ace"); //check for ace
        boolean hasTenValue = 
                (card1.getValue() == 10 && !card1.getRank().equals("Ace")) || 
                (card2.getValue() == 10 && !card2.getRank().equals("Ace")); //check for 10 value card
        
        return hasAce && hasTenValue && playerScore == 21; //natural blackjack conditions
    }



    public boolean hadBlackjack() {
        System.out.println("hadBlackjack() called - returning: " + this.hadBlackjack);
        return this.hadBlackjack; //return whether blackjack occurred
    }

    public void playerHit() {
        if (!gameOver && betAmount > 0 && cardsDealt) { //validate hit conditions
            playerHand.add(deck.drawCard()); //add card to player hand
            calculateScores(); //recalculate scores
            
            //checks for blackjack if player reaches 21 by hitting
            if (playerScore == 21) {
                checkForBlackjack(); 
            }
            
            if (playerScore > 21) { //player busts
                gameOver = true;
                gameResult = "BUST! You lose.";
                playerBalance -= betAmount; //deduct bet amount from balance
                hadBlackjack = false; //no blackjack on bust
            } else if (playerScore == 21 && !hadBlackjack) {
                //if player reaches 21 by hitting, count it as blackjack
                System.out.println("Player reached 21 by hitting!");
                checkForBlackjack();
                if (!gameOver) {
                    playerStand(); //automatically stand on 21
                }
            }
        }
    }

    public void playerStand() {
        if (!gameOver && betAmount > 0 && cardsDealt) { //validate stand conditions
            dealerTurn = true; //switch to dealers turn
            playDealerHand(); //dealer plays their hand
            determineWinner(); //determine who the winner is
        }
    }

    private void playDealerHand() {
        calculateScores(); //calculate current scores
        while (dealerScore < 17) { //dealer must hit until 17 or higher
            dealerHand.add(deck.drawCard()); //dealer draws card
            calculateScores(); //recalculate scores
        }
    }

    private void calculateScores() {
        playerScore = calculateHandValue(playerHand); //calculate player score
        dealerScore = calculateHandValue(dealerHand); //calculate dealer score
    }

    private int calculateHandValue(List<Card> hand) { 
        int value = 0; //total hand value
        int aces = 0; //numebr of aces in hand

        for (Card card : hand) { 
            value += card.getValue(); //add card value
            if (card.getRank().equals("Ace")) {
                aces++; //count aces
            }
        }
        //adjust for aces (1 or 11)
        while (value > 21 && aces > 0) {
            value -= 10; //reduce ace value from 11 to 1
            aces--;
        }

        return value;
    }

    private void determineWinner() {
        gameOver = true; //end the game

        if (playerScore > 21) { //player busts
            gameResult = "BUST! You lose.";
            playerBalance -= betAmount;
            hadBlackjack = false;
        } else if (dealerScore > 21) { //dealer busts
            gameResult = "Dealer busts! You win!";
            playerBalance += betAmount;
        } else if (playerScore > dealerScore) { //player has a higher score
            gameResult = "You win!";
            playerBalance += betAmount;
        } else if (dealerScore > playerScore) { //player has a lower scores
            gameResult = "Dealer wins!";
            playerBalance -= betAmount;
        } else { //tie game
            gameResult = "Push! It's a tie.";
        }
        
        
        System.out.println("FINAL BLACKJACK STATUS: " + hadBlackjack);
        System.out.println("FINAL SCORE - Player: " + playerScore + ", Dealer: " + dealerScore);
    }

    public void doubleDown() {
        if (!gameOver && betAmount > 0 && playerHand.size() == 2 && playerBalance >= betAmount && cardsDealt) {
            playerBalance -= betAmount; //deduct additional bet
            betAmount *= 2; //double the bet
            playerHit(); //take one card
            if (!gameOver) {
                playerStand(); //automatically stand after double down
            }
        }
    }

    //Getters for UI
    public List<Card> getPlayerHand() { 
        return playerHand; //return player cards
    }
    
    
    public List<Card> getDealerHand() { 
        return dealerHand; //return dealer cards
    }
    
    
    public int getPlayerScore() {
        return playerScore;  //return player score
    }
    
    
    public int getDealerScore() {
        return dealerScore;  //return dealer score
    }
    
    
    public String getGameResult() {
       return gameResult; //return game result message
    }
    
    
    public int getPlayerBalance() {
        return playerBalance; //returns current balance
    }
    
    
    public int getCurrentBet() {
        return betAmount; //returns the current bet amount
    }
    
    
    public boolean isGameOver() {
        return gameOver; //return game over status
    }
    
    
    public boolean isDealerTurn() {
        return dealerTurn; //return whose turn it is
    }
    
    
    public boolean areCardsDealt() {
        return cardsDealt; //return if cards have been dealt
    }
    

    public boolean canPlayerHit() { 
        return !gameOver && playerScore < 21 && betAmount > 0 && cardsDealt;  //hit conditions
    }

    
    public boolean canPlayerStand() {
        return !gameOver && betAmount > 0 && playerScore <= 21 && cardsDealt; //stand conditions
    }

    
    public boolean shouldShowAllDealerCards() {
        return gameOver || dealerTurn; //sdhow all cards when game over or dealers turn
    }

    
    public boolean isPlayerWinner() {
        
        if (!gameOver){//game must be over to determine winner
            return false;
        } 
        return (dealerScore > 21) || (playerScore <= 21 && playerScore > dealerScore); //win conditions
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BlackJackGUI.model;

/**
 *
 * @author nicho
 */
public class Stats {
    private int gamesPlayed; //total number of games played
    private int wins; //number of games won
    private int losses; //number of games lost
    private int blackjacks; //number of blackjacks achieved
    private int balance; //current player balance

    //constructor with all parameters
    public Stats(int gamesPlayed, int wins, int losses, int blackjacks, int balance) {
        this.gamesPlayed = gamesPlayed;
        this.wins = wins;
        this.losses = losses;
        this.blackjacks = blackjacks;
        this.balance = balance;
    }

    //default constructor with start values
    public Stats() {
        this(0, 0, 0, 0, 0); //default 0 games, and $1000 balance
    }

    
    
    //getters for all stats
    public int getGamesPlayed() { 
       return gamesPlayed; //return total games played
    }
    
    
    public int getWins() {
       return wins; //return number of wins
    }  
    
    
    public int getLosses() { 
       return losses;  //return number of losses
    } 
    
    
    public int getBlackjacks() {
       return blackjacks; //return number of blackjacks
    } 
    
    
    public int getBalance() { 
       return balance; //return current balance
    }
    
    
    
    public double getWinPercentage() {
        return gamesPlayed == 0 ? 0 : (double) getWins() / gamesPlayed * 100; //avoid division by zero
    }
}

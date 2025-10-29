/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BlackJackGUI.model;

import BlackJackGUI.model.*;

/**
 *
 * @author nicho
 */
public class Stats {
    private int gamesPlayed;
    private int wins;
    private int losses;
    private int blackjacks;
    private int balance;

    // Constructor with balance
    public Stats(int gamesPlayed, int wins, int losses, int blackjacks, int balance) {
        this.gamesPlayed = gamesPlayed;
        this.wins = wins;
        this.losses = losses;
        this.blackjacks = blackjacks;
        this.balance = balance;
    }

    // Default constructor
    public Stats() {
        this(0, 0, 0, 0, 1000);
    }

    // Getters
    public int getGamesPlayed() { 
       return gamesPlayed; 
    }
    public int getWins() {
       return wins; 
    }  // CHANGED FROM getGamesWon()
    public int getLosses() { 
       return losses; 
    } // ADDED THIS METHOD
    public int getBlackjacks() {
       return blackjacks; 
    } // ADDED THIS METHOD
    public int getBalance() { 
       return balance; 
    }
    
    
    
    public double getWinPercentage() {
        return gamesPlayed == 0 ? 0 : (double) getWins() / gamesPlayed * 100;
    }
}

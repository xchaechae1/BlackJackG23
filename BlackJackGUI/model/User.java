package BlackJackGUI.model;

/**
 * Represents a player/user in the Blackjack game.
 * Holds basic account data like username and balance.
 */
public class User {

    private String username; //players username
    private String password; //players password
    private int balance; //players current balance

    /**
     * Constructor for new users (default starting balance).
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0; //default starting balance
    }

    /**
     * Constructor for existing users loaded from the database.
     */
    public User(String username, String password, int balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    
    public String getUsername() {
        return username; //returns username
    }

    public void setUsername(String username) {
        this.username = username; //set username
    }

    public String getPassword() {
        return password; //return password
    }

    public void setPassword(String password) {
        this.password = password; //set password
    }

    public int getBalance() {
        return balance; //return current balance
    }

    public void setBalance(int balance) {
        this.balance = balance; //set balance
    }

    // --- Game-related methods ---
    public void adjustBalance(int amount) {
        this.balance += amount; //add or subtract from balance
    }



    @Override
    public String toString() {
        return "User{" +
               "username='" + username + '\'' +
               ", balance=" + balance +
               '}'; //string representation of user object
    }
}

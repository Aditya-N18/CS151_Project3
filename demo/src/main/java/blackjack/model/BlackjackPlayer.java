package blackjack.model;

public class BlackjackPlayer {

    protected Hand hand;
    protected int balance;
    protected int currentBet;

    public BlackjackPlayer(int startingBalance) {
        this.hand = new Hand();
        this.balance = startingBalance;
        this.currentBet = 0;
    }

    public Hand getHand() {
        return hand;
    }

    public int getBalance() {
        return balance;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void placeBet(int amount) {
        if (amount > balance || amount <= 0) {
            throw new IllegalArgumentException("Invalid bet amount");
        }
        currentBet = amount;
        balance -= amount;
    }

    public void winBet() {
        balance += currentBet * 2;
        currentBet = 0;
    }

    public void loseBet() {
        currentBet = 0;
    }

    public void pushBet() {
        balance += currentBet;
        currentBet = 0;
    }

    public void resetHand() {
        hand.clear();
    }

    public boolean isBust() {
        return hand.isBust();
    }
}

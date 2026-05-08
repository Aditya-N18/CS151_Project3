package blackjack.controller;

import java.util.ArrayList;
import java.util.List;

import blackjack.model.AIPlayer;
import blackjack.model.BlackjackPlayer;
import blackjack.model.Dealer;
import blackjack.model.Deck;

//BETTING --(startNewRound)--> PLAYER_TURN
//PLAYER_TURN --(stand)--> AI1_TURN -> AI2_TURN -> DEALER_TURN -> ROUND_OVER
//PLAYER_TURN --(hit -> bust)--> auto-runs AI/dealer/settle -> ROUND_OVER
//ROUND_OVER --(nextRound)--> BETTING (or GAME_OVER if out of money)

public class BlackjackGameLogic {

    public enum Phase {
        BETTING,
        PLAYER_TURN,
        AI1_TURN,
        AI2_TURN,
        DEALER_TURN,
        ROUND_OVER,
        GAME_OVER
    }

    private static final int AI1_HIT_BELOW = 16;
    private static final int AI2_HIT_BELOW = 17;

    private Deck deck;
    private final BlackjackPlayer humanPlayer;
    private final AIPlayer aiPlayer1;
    private final AIPlayer aiPlayer2;
    private final Dealer dealer;
    private final List<BlackjackPlayer> players;

    private Phase phase;

    public BlackjackGameLogic() {
        deck = new Deck();

        humanPlayer = new BlackjackPlayer(1000);
        aiPlayer1 = new AIPlayer(1000, AI1_HIT_BELOW);
        aiPlayer2 = new AIPlayer(1000, AI2_HIT_BELOW);
        dealer = new Dealer();

        players = new ArrayList<>();
        players.add(humanPlayer);
        players.add(aiPlayer1);
        players.add(aiPlayer2);

        phase = Phase.BETTING;
    }

    public Phase getPhase() {
        return phase;
    }

    /** Test/save-load hook. */
    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public void startNewRound(int bet) {
        if (phase != Phase.BETTING) {
            throw new IllegalStateException(
                    "startNewRound requires BETTING phase, was " + phase);
        }

        deck = new Deck();

        for (BlackjackPlayer player : players) {
            player.resetHand();

            if (player.getBalance() >= bet) {
                player.placeBet(bet);
            }
        }

        dealer.resetHand();

        for (int i = 0; i < 2; i++) {
            for (BlackjackPlayer player : players) {
                player.getHand().addCard(deck.dealCard());
            }
            dealer.getHand().addCard(deck.dealCard());
        }

        phase = Phase.PLAYER_TURN;
    }

    public void humanHit() {
        if (phase != Phase.PLAYER_TURN) {
            throw new IllegalStateException(
                    "humanHit requires PLAYER_TURN phase, was " + phase);
        }

        humanPlayer.getHand().addCard(deck.dealCard());

        if (humanPlayer.isBust()) {
            runRemainderOfRound();
        }
    }

    public void humanStand() {
        if (phase != Phase.PLAYER_TURN) {
            throw new IllegalStateException(
                    "humanStand requires PLAYER_TURN phase, was " + phase);
        }
        runRemainderOfRound();
    }

    private void runRemainderOfRound() {
        phase = Phase.AI1_TURN;
        playAIPlayer(aiPlayer1);

        phase = Phase.AI2_TURN;
        playAIPlayer(aiPlayer2);

        phase = Phase.DEALER_TURN;
        playDealerTurn();

        settleBets();
        finishRound();
    }

    public void nextRound() {
        if (phase != Phase.ROUND_OVER) {
            throw new IllegalStateException(
                    "nextRound requires ROUND_OVER phase, was " + phase);
        }

        deck = new Deck();
        for (BlackjackPlayer player : players) {
            player.resetHand();
        }
        dealer.resetHand();

        phase = Phase.BETTING;
    }

    public void playAITurns() {
        playAIPlayer(aiPlayer1);
        playAIPlayer(aiPlayer2);
    }

    private void playAIPlayer(AIPlayer aiPlayer) {
        while (!aiPlayer.isBust() && aiPlayer.shouldHit(aiPlayer.getHand())) {
            aiPlayer.getHand().addCard(deck.dealCard());
        }
    }

    public void playDealerTurn() {
        while (!dealer.isBust() && dealer.shouldHit(dealer.getHand())) {
            dealer.getHand().addCard(deck.dealCard());
        }
    }

    public void settleBets() {
        int dealerValue = dealer.getHand().getValue();
        boolean dealerBust = dealer.getHand().isBust();

        for (BlackjackPlayer player : players) {
            int playerValue = player.getHand().getValue();

            if (player.isBust()) {
                player.loseBet();
            } else if (dealerBust || playerValue > dealerValue) {
                player.winBet();
            } else if (playerValue == dealerValue) {
                player.pushBet();
            } else {
                player.loseBet();
            }
        }
    }

    private void finishRound() {
        if (humanPlayer.getBalance() <= 0) {
            phase = Phase.GAME_OVER;
        } else {
            phase = Phase.ROUND_OVER;
        }
    }

    public boolean isHumanBust() {
        return humanPlayer.isBust();
    }

    public BlackjackPlayer getHumanPlayer() {
        return humanPlayer;
    }

    public AIPlayer getAiPlayer1() {
        return aiPlayer1;
    }

    public AIPlayer getAiPlayer2() {
        return aiPlayer2;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public List<BlackjackPlayer> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }
}

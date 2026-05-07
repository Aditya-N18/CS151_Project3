package blackjack.controller;

import java.util.ArrayList;
import java.util.List;

import blackjack.model.AIPlayer;
import blackjack.model.BlackjackPlayer;
import blackjack.model.Dealer;
import blackjack.model.Deck;

public class BlackjackGameLogic {

    private Deck deck;
    private BlackjackPlayer humanPlayer;
    private AIPlayer aiPlayer1;
    private AIPlayer aiPlayer2;
    private Dealer dealer;
    private List<BlackjackPlayer> players;

    public BlackjackGameLogic() {
        deck = new Deck();

        humanPlayer = new BlackjackPlayer(1000);
        aiPlayer1 = new AIPlayer(1000);
        aiPlayer2 = new AIPlayer(1000);
        dealer = new Dealer();

        players = new ArrayList<>();
        players.add(humanPlayer);
        players.add(aiPlayer1);
        players.add(aiPlayer2);
    }

    public void startNewRound(int bet) {
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
    }

    public void humanHit() {
        humanPlayer.getHand().addCard(deck.dealCard());
    }

    public void humanStand() {
        playAITurns();
        playDealerTurn();
        settleBets();
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
}
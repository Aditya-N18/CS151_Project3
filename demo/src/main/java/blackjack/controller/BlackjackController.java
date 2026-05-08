package blackjack.controller;

import java.util.List;

import blackjack.controller.BlackjackGameLogic.Phase;
import blackjack.model.AIPlayer;
import blackjack.model.BlackjackPlayer;
import blackjack.model.BlackjackSaveState;
import blackjack.model.Card;
import blackjack.model.Dealer;
import blackjack.view.BlackjackGameView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

public class BlackjackController {

    private final BlackjackGameLogic logic;
    private final BlackjackGameView view;

    public BlackjackController(BlackjackGameView view, BlackjackGameLogic logic) {
        this.view = view;
        this.logic = logic;
    }

    public BlackjackGameLogic getLogic() {
        return logic;
    }

    /**
     * Show a modal dialog with the encrypted save token in a non-editable but
     * selectable {@link TextArea}, ready for the user to copy.
     */
    public void saveGame() {
        String token = BlackjackSaveState.encode(logic);

        TextArea area = new TextArea(token);
        area.setEditable(false);
        area.setWrapText(true);
        area.setPrefRowCount(6);
        area.setPrefColumnCount(48);
        area.setStyle("-fx-font-family: 'monospace';");

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Save State");
        dialog.setHeaderText("Copy this save string to keep your progress.");
        dialog.getDialogPane().setContent(area);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
        // Pre-select the whole token so Ctrl+C is one keystroke.
        area.requestFocus();
        area.selectAll();
        dialog.showAndWait();
    }

    /** Place the chip bar's pending bet and start a new round. */
    public void placeBet(int amount) {
        if (logic.getPhase() != Phase.BETTING) {
            return;
        }
        if (amount <= 0 || amount > logic.getHumanPlayer().getBalance()) {
            view.setStatus("Bet must be between $1 and $"
                    + logic.getHumanPlayer().getBalance() + ".");
            view.refresh();
            return;
        }

        logic.startNewRound(amount);
        view.resetPendingBet();
        view.setStatus("You bet $" + amount + ". Your turn - Hit or Stand?");
        view.refresh();
    }

    public void handleHit() {
        if (logic.getPhase() != Phase.PLAYER_TURN) {
            return;
        }

        RoundSnapshot snap = RoundSnapshot.capture(logic);
        int humanCardsBefore = logic.getHumanPlayer().getHand().getCards().size();

        logic.humanHit();

        Card drawn = logic.getHumanPlayer().getHand().getCards().get(humanCardsBefore);
        int newValue = logic.getHumanPlayer().getHand().getValue();
        boolean bust = logic.getHumanPlayer().isBust();

        StringBuilder sb = new StringBuilder();
        if (bust) {
            sb.append("You drew ").append(prettyCard(drawn))
                    .append(" - busted at ").append(newValue).append(".\n");
            appendRoundSummary(sb, snap);
        } else {
            sb.append("You drew ").append(prettyCard(drawn))
                    .append(". Your total is ").append(newValue).append(".");
        }

        if (logic.getPhase() == Phase.GAME_OVER) {
            sb.append("\nGame Over - you're out of money. ")
                    .append("Use the toolbar to return to Main Menu.");
        }

        view.setStatus(sb.toString().trim());
        view.refresh();
    }

    public void handleStand() {
        if (logic.getPhase() != Phase.PLAYER_TURN) {
            return;
        }

        RoundSnapshot snap = RoundSnapshot.capture(logic);
        int standValue = logic.getHumanPlayer().getHand().getValue();

        logic.humanStand();

        StringBuilder sb = new StringBuilder();
        sb.append("You stood at ").append(standValue).append(".\n");
        appendRoundSummary(sb, snap);

        if (logic.getPhase() == Phase.GAME_OVER) {
            sb.append("\nGame Over - you're out of money. ")
                    .append("Use the toolbar to return to Main Menu.");
        }

        view.setStatus(sb.toString().trim());
        view.refresh();
    }

    public void handleNextRound() {
        if (logic.getPhase() != Phase.ROUND_OVER) {
            return;
        }
        logic.nextRound();
        view.setStatus("Place your bet to start the next round.");
        view.refresh();
    }

    /**
     * Append the AI #1, AI #2, dealer, and per-player payout lines to the
     * given builder. {@code snap} provides the pre-{@code humanStand} state we
     * need to compute "how many cards each AI hit" and the bets that were on
     * the table going into the resolution.
     */
    private void appendRoundSummary(StringBuilder sb, RoundSnapshot snap) {
        appendActorSummary(sb, "AI #1", logic.getAiPlayer1(), snap.ai1HandSize);
        appendActorSummary(sb, "AI #2", logic.getAiPlayer2(), snap.ai2HandSize);
        appendDealerSummary(sb, snap);
        appendOutcomes(sb, snap);
    }

    private static void appendActorSummary(StringBuilder sb, String name,
            AIPlayer p, int cardsBefore) {
        int hits = p.getHand().getCards().size() - cardsBefore;
        int v = p.getHand().getValue();
        sb.append(name).append(' ').append(actionPhrase(hits))
                .append(p.isBust()
                        ? " and busted at "
                        : " and stood at ")
                .append(v).append(".\n");
    }

    private void appendDealerSummary(StringBuilder sb, RoundSnapshot snap) {
        Dealer d = logic.getDealer();
        List<Card> cards = d.getHand().getCards();
        if (cards.size() >= 2 && snap.dealerHole != null) {
            sb.append("Dealer reveals ").append(prettyCard(snap.dealerHole)).append(". ");
        }

        int hits = cards.size() - snap.dealerHandSize;
        int v = d.getHand().getValue();
        sb.append("Dealer ").append(actionPhrase(hits))
                .append(d.isBust()
                        ? " and busted at "
                        : " and stood at ")
                .append(v).append(".\n");
    }

    private void appendOutcomes(StringBuilder sb, RoundSnapshot snap) {
        int dealerValue = logic.getDealer().getHand().getValue();
        boolean dealerBust = logic.getDealer().isBust();
        sb.append(outcomeLine("You", logic.getHumanPlayer(), snap.humanBet, dealerValue, dealerBust));
        sb.append("  ");
        sb.append(outcomeLine("AI #1", logic.getAiPlayer1(), snap.ai1Bet, dealerValue, dealerBust));
        sb.append("  ");
        sb.append(outcomeLine("AI #2", logic.getAiPlayer2(), snap.ai2Bet, dealerValue, dealerBust));
    }

    private static String outcomeLine(String name, BlackjackPlayer p, int bet,
            int dealerValue, boolean dealerBust) {
        if (bet <= 0) {
            return name + " sat out.";
        }
        if (p.isBust()) {
            return name + " lost $" + bet + ".";
        }
        int v = p.getHand().getValue();
        if (dealerBust || v > dealerValue) {
            return name + " won $" + bet + ".";
        }
        if (v == dealerValue) {
            return name + " pushed $" + bet + ".";
        }
        return name + " lost $" + bet + ".";
    }

    private static String actionPhrase(int hits) {
        if (hits <= 0) {
            return "stood";
        }
        return "hit " + hits + (hits == 1 ? " time" : " times");
    }

    private static String prettyCard(Card c) {
        return rankWord(c.getRank()) + " of " + suitWord(c.getSuit());
    }

    private static String rankWord(Card.Rank rank) {
        switch (rank) {
            case ACE:
                return "Ace";
            case KING:
                return "King";
            case QUEEN:
                return "Queen";
            case JACK:
                return "Jack";
            case TEN:
                return "10";
            default:
                return Integer.toString(rank.getValue());
        }
    }

    private static String suitWord(Card.Suit suit) {
        switch (suit) {
            case HEARTS:
                return "Hearts";
            case DIAMONDS:
                return "Diamonds";
            case CLUBS:
                return "Clubs";
            case SPADES:
                return "Spades";
            default:
                return "?";
        }
    }

    /**
     * Snapshot of the round state taken in PLAYER_TURN, before the AI/dealer
     * actions auto-run. We use it to compute per-actor card counts and to
     * remember each player's bet because settling resets {@code currentBet}
     * to zero.
     */
    private static final class RoundSnapshot {

        final int humanBet;
        final int ai1Bet;
        final int ai2Bet;
        final int ai1HandSize;
        final int ai2HandSize;
        final int dealerHandSize;
        final Card dealerHole;

        private RoundSnapshot(int humanBet, int ai1Bet, int ai2Bet,
                int ai1HandSize, int ai2HandSize, int dealerHandSize,
                Card dealerHole) {
            this.humanBet = humanBet;
            this.ai1Bet = ai1Bet;
            this.ai2Bet = ai2Bet;
            this.ai1HandSize = ai1HandSize;
            this.ai2HandSize = ai2HandSize;
            this.dealerHandSize = dealerHandSize;
            this.dealerHole = dealerHole;
        }

        static RoundSnapshot capture(BlackjackGameLogic logic) {
            List<Card> dealerCards = logic.getDealer().getHand().getCards();
            Card hole = dealerCards.size() >= 2 ? dealerCards.get(1) : null;
            return new RoundSnapshot(
                    logic.getHumanPlayer().getCurrentBet(),
                    logic.getAiPlayer1().getCurrentBet(),
                    logic.getAiPlayer2().getCurrentBet(),
                    logic.getAiPlayer1().getHand().getCards().size(),
                    logic.getAiPlayer2().getHand().getCards().size(),
                    dealerCards.size(),
                    hole);
        }
    }
}

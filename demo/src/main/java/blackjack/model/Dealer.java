package blackjack.model;

public class Dealer extends BlackjackPlayer implements TurnStrategy {

    public Dealer() {
        super(0);
    }

    @Override
    public boolean shouldHit(Hand hand) {
        int value = hand.getValue();
        // hit on soft 17 or lower. Stand on hard 17+.
        return value < 17 || (value == 17 && hand.isSoft());
    }
}

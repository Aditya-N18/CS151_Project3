package blackjack.model;

public class Dealer extends BlackjackPlayer implements TurnStrategy {

    public Dealer() {
        super(0);
    }

    @Override
    public boolean shouldHit(Hand hand) {
        return hand.getValue() <= 17;
    }
}

package blackjack.model;

//Simple logic, hit if hand value is below hitBelow, otherwise stand
public class AIPlayer extends BlackjackPlayer implements TurnStrategy {

    private final int hitBelow;

    public AIPlayer(int startingBalance, int hitBelow) {
        super(startingBalance);
        if (hitBelow < 2 || hitBelow > 21) {
            throw new IllegalArgumentException(
                    "hitBelow must be between 2 and 21, got " + hitBelow);
        }
        this.hitBelow = hitBelow;
    }

    public int getHitBelow() {
        return hitBelow;
    }

    @Override
    public boolean shouldHit(Hand hand) {
        return hand.getValue() < hitBelow;
    }
}

package blackjack.model;

import java.util.Random;

public class AIPlayer extends BlackjackPlayer implements TurnStrategy {

    private final Random random = new Random();

    public AIPlayer(int startingBalance) {
        super(startingBalance);
    }

    @Override
    public boolean shouldHit(Hand hand) {
        int value = hand.getValue();

        if (value < 14) return true;
        if (value > 17) return false;

        return random.nextBoolean();
    }
}

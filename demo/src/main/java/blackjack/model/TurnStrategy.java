package blackjack.model;

public interface TurnStrategy {
    boolean shouldHit(Hand hand);
}
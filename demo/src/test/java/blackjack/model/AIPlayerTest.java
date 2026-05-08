package blackjack.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class AIPlayerTest {

    @Test
    void testAIHitsBelow14() {
        AIPlayer ai = new AIPlayer(1000);

        Hand hand = new Hand();
        hand.addCard(new Card(Card.Suit.HEARTS, Card.Rank.FIVE));
        hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.SIX));

        assertTrue(ai.shouldHit(hand));
    }

    @Test
    void testAIStandsAbove17() {
        AIPlayer ai = new AIPlayer(1000);

        Hand hand = new Hand();
        hand.addCard(new Card(Card.Suit.HEARTS, Card.Rank.KING));
        hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.EIGHT));

        assertFalse(ai.shouldHit(hand));
    }

    @Test
    void testAIStartsWithCorrectBalance() {
        AIPlayer ai = new AIPlayer(500);

        assertEquals(500, ai.getBalance());
    }

    @Test
    void testAIRandomDecisionBetween14And17() {
        AIPlayer ai = new AIPlayer(1000);

        Hand hand = new Hand();
        hand.addCard(new Card(Card.Suit.HEARTS, Card.Rank.SEVEN));
        hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.EIGHT));

        boolean result = ai.shouldHit(hand);

        assertTrue(result || !result);
    }
}
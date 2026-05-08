package blackjack.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class HandTest {

    @Test
    void testEmptyHandStartsAtZero() {
        Hand hand = new Hand();

        assertEquals(0, hand.getValue());
    }

    @Test
    void testAddCardIncreasesValue() {
        Hand hand = new Hand();

        hand.addCard(new Card(Card.Suit.HEARTS, Card.Rank.TEN));

        assertEquals(10, hand.getValue());
    }

    @Test
    void testBlackjackDetected() {
        Hand hand = new Hand();

        hand.addCard(new Card(Card.Suit.SPADES, Card.Rank.ACE));
        hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.KING));

        assertTrue(hand.isBlackjack());
    }

    @Test
    void testBustDetected() {
        Hand hand = new Hand();

        hand.addCard(new Card(Card.Suit.HEARTS, Card.Rank.KING));
        hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.QUEEN));
        hand.addCard(new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE));

        assertTrue(hand.isBust());
    }

    @Test
    void testAceAdjustsValue() {
        Hand hand = new Hand();

        hand.addCard(new Card(Card.Suit.HEARTS, Card.Rank.ACE));
        hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.NINE));
        hand.addCard(new Card(Card.Suit.SPADES, Card.Rank.FIVE));

        assertEquals(15, hand.getValue());
    }

    @Test
    void testClearRemovesCards() {
        Hand hand = new Hand();

        hand.addCard(new Card(Card.Suit.HEARTS, Card.Rank.TEN));
        hand.clear();

        assertEquals(0, hand.getCards().size());
    }

    @Test
    void testAddingNullCardThrowsException() {
        Hand hand = new Hand();

        assertThrows(IllegalArgumentException.class, () -> hand.addCard(null));
    }
}
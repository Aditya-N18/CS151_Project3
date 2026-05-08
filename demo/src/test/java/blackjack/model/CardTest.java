package blackjack.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CardTest {

    @Test
    void testCardValue() {
        Card card = new Card(Card.Suit.HEARTS, Card.Rank.ACE);

        assertEquals(11, card.getValue());
    }

    @Test
    void testCardSuit() {
        Card card = new Card(Card.Suit.SPADES, Card.Rank.KING);

        assertEquals(Card.Suit.SPADES, card.getSuit());
    }

    @Test
    void testCardRank() {
        Card card = new Card(Card.Suit.CLUBS, Card.Rank.QUEEN);

        assertEquals(Card.Rank.QUEEN, card.getRank());
    }

    @Test
    void testToString() {
        Card card = new Card(Card.Suit.DIAMONDS, Card.Rank.TEN);

        assertEquals("TEN of DIAMONDS", card.toString());
    }
}
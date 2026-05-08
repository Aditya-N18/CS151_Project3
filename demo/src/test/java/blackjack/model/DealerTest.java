package blackjack.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class DealerTest {

    @Test
    void testDealerHitsBelow17() {
        Dealer dealer = new Dealer();

        Hand hand = new Hand();
        hand.addCard(new Card(Card.Suit.HEARTS, Card.Rank.SIX));
        hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.NINE));

        assertTrue(dealer.shouldHit(hand));
    }

    @Test
    void testDealerHitsOn17() {
        Dealer dealer = new Dealer();

        Hand hand = new Hand();
        hand.addCard(new Card(Card.Suit.HEARTS, Card.Rank.TEN));
        hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.SEVEN));

        assertTrue(dealer.shouldHit(hand));
    }

    @Test
    void testDealerStandsAbove17() {
        Dealer dealer = new Dealer();

        Hand hand = new Hand();
        hand.addCard(new Card(Card.Suit.HEARTS, Card.Rank.TEN));
        hand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.EIGHT));

        assertFalse(dealer.shouldHit(hand));
    }

    @Test
    void testDealerStartsWithZeroBalance() {
        Dealer dealer = new Dealer();

        assertEquals(0, dealer.getBalance());
    }
}
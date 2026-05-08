package blackjack.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class DeckTest {

    @Test
    void testDeckStartsWith52Cards() {
        Deck deck = new Deck();

        assertEquals(52, deck.size());
    }

    @Test
    void testDealCardReducesDeckSize() {
        Deck deck = new Deck();

        deck.dealCard();

        assertEquals(51, deck.size());
    }

    @Test
    void testDealCardReturnsCard() {
        Deck deck = new Deck();

        Card card = deck.dealCard();

        assertNotNull(card);
    }

    @Test
    void testDeckBecomesEmpty() {
        Deck deck = new Deck();

        for (int i = 0; i < 52; i++) {
            deck.dealCard();
        }

        assertEquals(0, deck.size());
    }

    @Test
    void testDealingFromEmptyDeckThrowsException() {
        Deck deck = new Deck();

        for (int i = 0; i < 52; i++) {
            deck.dealCard();
        }

        assertThrows(IllegalStateException.class, deck::dealCard);
    }
}
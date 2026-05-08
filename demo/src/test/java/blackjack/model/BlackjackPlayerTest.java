package blackjack.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BlackjackPlayerTest {

    @Test
    void testPlayerStartsWithBalance() {
        BlackjackPlayer player = new BlackjackPlayer(1000);

        assertEquals(1000, player.getBalance());
    }

    @Test
    void testPlayerStartsWithZeroBet() {
        BlackjackPlayer player = new BlackjackPlayer(1000);

        assertEquals(0, player.getCurrentBet());
    }

    @Test
    void testPlaceBetReducesBalance() {
        BlackjackPlayer player = new BlackjackPlayer(1000);

        player.placeBet(100);

        assertEquals(900, player.getBalance());
        assertEquals(100, player.getCurrentBet());
    }

    @Test
    void testCannotBetMoreThanBalance() {
        BlackjackPlayer player = new BlackjackPlayer(100);

        assertThrows(IllegalArgumentException.class, () -> player.placeBet(200));
    }

    @Test
    void testCannotBetZeroOrNegativeAmount() {
        BlackjackPlayer player = new BlackjackPlayer(1000);

        assertThrows(IllegalArgumentException.class, () -> player.placeBet(0));
        assertThrows(IllegalArgumentException.class, () -> player.placeBet(-10));
    }

    @Test
    void testWinBetAddsWinningsAndClearsBet() {
        BlackjackPlayer player = new BlackjackPlayer(1000);

        player.placeBet(100);
        player.winBet();

        assertEquals(1100, player.getBalance());
        assertEquals(0, player.getCurrentBet());
    }

    @Test
    void testLoseBetClearsBetOnly() {
        BlackjackPlayer player = new BlackjackPlayer(1000);

        player.placeBet(100);
        player.loseBet();

        assertEquals(900, player.getBalance());
        assertEquals(0, player.getCurrentBet());
    }

    @Test
    void testPushBetReturnsBetAndClearsBet() {
        BlackjackPlayer player = new BlackjackPlayer(1000);

        player.placeBet(100);
        player.pushBet();

        assertEquals(1000, player.getBalance());
        assertEquals(0, player.getCurrentBet());
    }

    @Test
    void testResetHandClearsCards() {
        BlackjackPlayer player = new BlackjackPlayer(1000);

        player.getHand().addCard(new Card(Card.Suit.HEARTS, Card.Rank.TEN));
        player.resetHand();

        assertEquals(0, player.getHand().getCards().size());
    }

    @Test
    void testIsBustUsesHandValue() {
        BlackjackPlayer player = new BlackjackPlayer(1000);

        player.getHand().addCard(new Card(Card.Suit.HEARTS, Card.Rank.KING));
        player.getHand().addCard(new Card(Card.Suit.CLUBS, Card.Rank.QUEEN));
        player.getHand().addCard(new Card(Card.Suit.SPADES, Card.Rank.FIVE));

        assertTrue(player.isBust());
    }
}
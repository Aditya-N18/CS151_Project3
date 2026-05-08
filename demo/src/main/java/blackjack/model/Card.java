package blackjack.model;

public class Card {

    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6),
        SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(10), QUEEN(10), KING(10), ACE(11);

        private final int value;

        Rank(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public int getValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    // Save-state: single-char rank token + '-' + single-char suit token.
    public String toCode() {
        return rankToken(rank) + "-" + suitToken(suit);
    }

    public static Card fromCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Card code cannot be null");
        }
        String trimmed = code.trim();
        int dash = trimmed.indexOf('-');
        if (dash < 0 || dash != trimmed.length() - 2) {
            throw new IllegalArgumentException("Invalid card code: " + code);
        }
        String rankPart = trimmed.substring(0, dash);
        String suitPart = trimmed.substring(dash + 1);
        if (rankPart.length() != 1) {
            throw new IllegalArgumentException("Invalid card code: " + code);
        }
        return new Card(suitFromToken(suitPart.charAt(0)),
                rankFromToken(rankPart.charAt(0)));
    }

    private static char rankToken(Rank r) {
        switch (r) {
            case ACE:
                return 'A';
            case KING:
                return 'K';
            case QUEEN:
                return 'Q';
            case JACK:
                return 'J';
            case TEN:
                return 'T';
            case NINE:
                return '9';
            case EIGHT:
                return '8';
            case SEVEN:
                return '7';
            case SIX:
                return '6';
            case FIVE:
                return '5';
            case FOUR:
                return '4';
            case THREE:
                return '3';
            case TWO:
                return '2';
            default:
                throw new IllegalStateException("Unknown rank: " + r);
        }
    }

    private static Rank rankFromToken(char c) {
        switch (c) {
            case 'A':
                return Rank.ACE;
            case 'K':
                return Rank.KING;
            case 'Q':
                return Rank.QUEEN;
            case 'J':
                return Rank.JACK;
            case 'T':
                return Rank.TEN;
            case '9':
                return Rank.NINE;
            case '8':
                return Rank.EIGHT;
            case '7':
                return Rank.SEVEN;
            case '6':
                return Rank.SIX;
            case '5':
                return Rank.FIVE;
            case '4':
                return Rank.FOUR;
            case '3':
                return Rank.THREE;
            case '2':
                return Rank.TWO;
            default:
                throw new IllegalArgumentException("Unknown rank token: " + c);
        }
    }

    private static char suitToken(Suit s) {
        switch (s) {
            case HEARTS:
                return 'H';
            case DIAMONDS:
                return 'D';
            case CLUBS:
                return 'C';
            case SPADES:
                return 'S';
            default:
                throw new IllegalStateException("Unknown suit: " + s);
        }
    }

    private static Suit suitFromToken(char c) {
        switch (c) {
            case 'H':
                return Suit.HEARTS;
            case 'D':
                return Suit.DIAMONDS;
            case 'C':
                return Suit.CLUBS;
            case 'S':
                return Suit.SPADES;
            default:
                throw new IllegalArgumentException("Unknown suit token: " + c);
        }
    }
}
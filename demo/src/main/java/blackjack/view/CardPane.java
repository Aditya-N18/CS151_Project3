package blackjack.view;

import blackjack.model.Card;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

//to display cards
public class CardPane extends StackPane {

    private static final double CARD_WIDTH = 56;
    private static final double CARD_HEIGHT = 78;
    private static final CornerRadii RADII = new CornerRadii(6);
    private static final Color RED = Color.web("#c81e1e");
    private static final Color BLACK = Color.web("#1a1a1a");

    public CardPane(Card card) {
        sizeSelf();

        Region bg = buildBackground(Color.WHITE);

        Color color = isRed(card.getSuit()) ? RED : BLACK;
        String suitGlyph = suitGlyph(card.getSuit());
        String rankToken = rankDisplay(card.getRank());

        Label corner = new Label(rankToken + "\n" + suitGlyph);
        corner.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");
        corner.setTextFill(color);

        Label center = new Label(suitGlyph);
        center.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        center.setTextFill(color);

        StackPane.setAlignment(corner, Pos.TOP_LEFT);
        StackPane.setMargin(corner, new Insets(3, 0, 0, 5));
        StackPane.setAlignment(center, Pos.CENTER);

        getChildren().addAll(bg, corner, center);
    }

    private CardPane() {
        sizeSelf();

        Region bg = buildBackground(Color.web("#3f5275"));

        Label q = new Label("?");
        q.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        q.setTextFill(Color.WHITE);

        StackPane.setAlignment(q, Pos.CENTER);
        getChildren().addAll(bg, q);
    }

    public static CardPane faceDown() {
        return new CardPane();
    }

    private void sizeSelf() {
        setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        setMinSize(CARD_WIDTH, CARD_HEIGHT);
        setMaxSize(CARD_WIDTH, CARD_HEIGHT);
    }

    private static Region buildBackground(Color fill) {
        Region bg = new Region();
        bg.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        bg.setBackground(new Background(
                new BackgroundFill(fill, RADII, Insets.EMPTY)));
        bg.setBorder(new Border(new BorderStroke(
                Color.web("#222"),
                BorderStrokeStyle.SOLID,
                RADII,
                new BorderWidths(1))));
        return bg;
    }

    private static boolean isRed(Card.Suit suit) {
        return suit == Card.Suit.HEARTS || suit == Card.Suit.DIAMONDS;
    }

    private static String suitGlyph(Card.Suit suit) {
        switch (suit) {
            case HEARTS:
                return "\u2665";
            case DIAMONDS:
                return "\u2666";
            case CLUBS:
                return "\u2663";
            case SPADES:
                return "\u2660";
            default:
                return "?";
        }
    }

    private static String rankDisplay(Card.Rank rank) {
        switch (rank) {
            case ACE:
                return "A";
            case KING:
                return "K";
            case QUEEN:
                return "Q";
            case JACK:
                return "J";
            case TEN:
                return "10";
            case NINE:
                return "9";
            case EIGHT:
                return "8";
            case SEVEN:
                return "7";
            case SIX:
                return "6";
            case FIVE:
                return "5";
            case FOUR:
                return "4";
            case THREE:
                return "3";
            case TWO:
                return "2";
            default:
                return "?";
        }
    }
}

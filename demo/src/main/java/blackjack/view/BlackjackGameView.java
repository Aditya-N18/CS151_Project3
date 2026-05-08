package blackjack.view;

import java.util.ArrayList;
import java.util.List;

import blackjack.controller.BlackjackController;
import blackjack.controller.BlackjackGameLogic;
import blackjack.controller.BlackjackGameLogic.Phase;
import blackjack.model.BlackjackPlayer;
import blackjack.model.Card;
import blackjack.model.Dealer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import manager.controller.NavigationController;
import utils.MusicPlayer;

/**
 * Full Blackjack game UI: dealer row up top, three player panes (AI #1, AI #2,
 * You) below, then a status panel, a chip bet bar, and an action bar (Hit /
 * Stand / Next Round). The active player's pane is highlighted with a gold
 * border keyed off {@link Phase}.
 */
public class BlackjackGameView {

    private static final int[] CHIP_DENOMINATIONS = { 5, 25, 50, 100 };

    private static final String PANE_BASE_STYLE = "-fx-background-color: #f3efe7;"
            + " -fx-background-radius: 8;"
            + " -fx-padding: 10;";
    private static final String IDLE_BORDER = " -fx-border-color: #c8c1b0; -fx-border-width: 1; -fx-border-radius: 8;";
    private static final String ACTIVE_BORDER = " -fx-border-color: #d4a017; -fx-border-width: 3; -fx-border-radius: 8;";

    private final BlackjackGameLogic logic;
    private final BlackjackController controller;
    private final VBox root;

    private int pendingBet;

    private final VBox dealerPane;
    private final HBox dealerCardRow;
    private final Label dealerTotalLabel;

    private final PlayerPane ai1Pane;
    private final PlayerPane ai2Pane;
    private final PlayerPane humanPane;

    private final Label statusLabel;
    private final Label pendingBetLabel;
    private final List<Button> chipButtons;
    private final Button clearChipButton;
    private final Button placeBetButton;
    private final HBox chipBar;

    private final Button hitButton;
    private final Button standButton;
    private final Button nextRoundButton;
    private final HBox actionBar;

    private final VBox gameOverPanel;

    public BlackjackGameView(NavigationController nav) {
        this(nav, new BlackjackGameLogic());
    }

    // Constructor used when loading a saved game with an already-populated logic.
    public BlackjackGameView(NavigationController nav, BlackjackGameLogic logic) {
        this.logic = logic;
        this.controller = new BlackjackController(this, logic);
        MusicPlayer.play("blackjack_theme.mp3");

        this.dealerCardRow = new HBox(6);
        this.dealerTotalLabel = new Label();
        this.dealerPane = new VBox(6);

        this.ai1Pane = new PlayerPane("AI #1");
        this.ai2Pane = new PlayerPane("AI #2");
        this.humanPane = new PlayerPane("You");

        this.statusLabel = new Label();
        this.pendingBetLabel = new Label();
        this.chipButtons = new ArrayList<>(CHIP_DENOMINATIONS.length);
        this.clearChipButton = new Button("Clear");
        this.placeBetButton = new Button("Place Bet");
        this.chipBar = new HBox(8);

        this.hitButton = new Button("Hit");
        this.standButton = new Button("Stand");
        this.nextRoundButton = new Button("Next Round");
        this.actionBar = new HBox(10, hitButton, standButton, nextRoundButton);

        this.gameOverPanel = buildGameOverPanel();

        this.root = build();

        wireActions();
        setStatus(initialStatusForPhase(logic.getPhase()));
        refresh();
    }

    public Region getRoot() {
        return root;
    }

    public BlackjackController getController() {
        return controller;
    }

    public BlackjackGameLogic getLogic() {
        return logic;
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    // refresh the view
    public void refresh() {
        Phase phase = logic.getPhase();
        boolean betting = phase == Phase.BETTING;
        boolean playerTurn = phase == Phase.PLAYER_TURN;
        boolean roundOver = phase == Phase.ROUND_OVER;
        boolean gameOver = phase == Phase.GAME_OVER;

        boolean hideDealerHole = phase.ordinal() < Phase.DEALER_TURN.ordinal();
        renderDealer(hideDealerHole);

        ai1Pane.render(logic.getAiPlayer1());
        ai2Pane.render(logic.getAiPlayer2());
        humanPane.render(logic.getHumanPlayer());

        applyTurnHighlight(phase);

        int balance = logic.getHumanPlayer().getBalance();
        for (int i = 0; i < chipButtons.size(); i++) {
            int d = CHIP_DENOMINATIONS[i];
            chipButtons.get(i).setDisable(!betting || pendingBet + d > balance);
        }
        clearChipButton.setDisable(!betting || pendingBet == 0);
        placeBetButton.setDisable(!betting || pendingBet <= 0 || pendingBet > balance);
        pendingBetLabel.setText("Pending bet: $" + pendingBet);
        chipBar.setDisable(gameOver);

        hitButton.setDisable(!playerTurn);
        standButton.setDisable(!playerTurn);
        nextRoundButton.setDisable(!roundOver);

        gameOverPanel.setVisible(gameOver);
        gameOverPanel.setManaged(gameOver);
    }

    public void resetPendingBet() {
        pendingBet = 0;
    }

    private VBox build() {
        Label title = new Label("Blackjack");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #a40000;");

        buildDealerPane();
        buildChipBar();
        styleStatus();
        styleActionBar();

        HBox playersRow = new HBox(16, ai1Pane, ai2Pane, humanPane);
        playersRow.setAlignment(Pos.CENTER);

        VBox box = new VBox(
                12,
                title,
                dealerPane,
                playersRow,
                statusLabel,
                gameOverPanel,
                chipBar,
                actionBar);
        box.setAlignment(Pos.TOP_CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #094008;");
        return box;
    }

    private void buildDealerPane() {
        Label name = new Label("DEALER");
        name.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        dealerCardRow.setAlignment(Pos.CENTER);
        dealerCardRow.setMinHeight(82);

        dealerTotalLabel.setStyle("-fx-font-size: 13px;");

        dealerPane.getChildren().addAll(name, dealerCardRow, dealerTotalLabel);
        dealerPane.setAlignment(Pos.CENTER);
        dealerPane.setMinWidth(360);
        dealerPane.setStyle(PANE_BASE_STYLE + IDLE_BORDER);
    }

    private void buildChipBar() {
        pendingBetLabel.setStyle("-fx-font-size: 13px;");

        chipBar.setAlignment(Pos.CENTER);
        chipBar.getChildren().add(pendingBetLabel);

        for (int d : CHIP_DENOMINATIONS) {
            Button b = new Button("+$" + d);
            b.setOnAction(e -> {
                pendingBet += d;
                refresh();
            });
            chipButtons.add(b);
            chipBar.getChildren().add(b);
        }

        chipBar.getChildren().addAll(clearChipButton, placeBetButton);
    }

    private void styleStatus() {
        statusLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: white;");
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(720);
        statusLabel.setMinHeight(60);
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setTextAlignment(TextAlignment.CENTER);
    }

    private void styleActionBar() {
        actionBar.setAlignment(Pos.CENTER);
    }

    private void wireActions() {
        clearChipButton.setOnAction(e -> {
            pendingBet = 0;
            refresh();
        });
        placeBetButton.setOnAction(e -> controller.placeBet(pendingBet));
        hitButton.setOnAction(e -> controller.handleHit());
        standButton.setOnAction(e -> controller.handleStand());
        nextRoundButton.setOnAction(e -> controller.handleNextRound());
    }

    private VBox buildGameOverPanel() {
        Label header = new Label("GAME OVER");
        header.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #b22222;");

        Label body = new Label(
                "You're out of money. Use the toolbar to return to Main Menu.");
        body.setStyle("-fx-font-size: 14px; -fx-text-fill: #2b2b2b;");
        body.setWrapText(true);
        body.setTextAlignment(TextAlignment.CENTER);
        body.setMaxWidth(360);

        VBox panel = new VBox(8, header, body);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(16, 24, 16, 24));
        panel.setStyle(
                "-fx-background-color: #fff5f5;"
                        + " -fx-border-color: #b22222;"
                        + " -fx-border-width: 2;"
                        + " -fx-background-radius: 8;"
                        + " -fx-border-radius: 8;");
        panel.setVisible(false);
        panel.setManaged(false);
        return panel;
    }

    private void renderDealer(boolean hideHole) {
        Dealer dealer = logic.getDealer();
        List<Card> cards = dealer.getHand().getCards();

        dealerCardRow.getChildren().clear();
        for (int i = 0; i < cards.size(); i++) {
            if (hideHole && i == 1) {
                dealerCardRow.getChildren().add(CardPane.faceDown());
            } else {
                dealerCardRow.getChildren().add(new CardPane(cards.get(i)));
            }
        }

        if (cards.isEmpty()) {
            dealerTotalLabel.setText("Waiting for bet.");
        } else if (hideHole) {
            dealerTotalLabel.setText("Showing: " + rankToken(cards.get(0).getRank()));
        } else {
            dealerTotalLabel.setText("Total: " + dealer.getHand().getValue());
        }
    }

    private void applyTurnHighlight(Phase phase) {
        boolean dealerActive = phase == Phase.DEALER_TURN;
        dealerPane.setStyle(PANE_BASE_STYLE + (dealerActive ? ACTIVE_BORDER : IDLE_BORDER));
        ai1Pane.setActive(phase == Phase.AI1_TURN);
        ai2Pane.setActive(phase == Phase.AI2_TURN);
        humanPane.setActive(phase == Phase.PLAYER_TURN);
    }

    private static String initialStatusForPhase(Phase phase) {
        switch (phase) {
            case BETTING:
                return "Place your bet to start the round.";
            case PLAYER_TURN:
                return "Your turn. Hit or stand.";
            case ROUND_OVER:
                return "Round over. Click Next Round to continue.";
            case GAME_OVER:
                return "Game Over - You're out of money. "
                        + "Use the toolbar to return to Main Menu.";
            default:
                return "";
        }
    }

    private static String rankToken(Card.Rank rank) {
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
            default:
                return Integer.toString(rank.getValue());
        }
    }

    private static final class PlayerPane extends VBox {

        private final String displayName;
        private final Label headerLabel;
        private final HBox cardRow;
        private final Label moneyLabel;

        PlayerPane(String displayName) {
            super(6);
            this.displayName = displayName;

            this.headerLabel = new Label(displayName);
            headerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            this.cardRow = new HBox(4);
            cardRow.setAlignment(Pos.CENTER);
            cardRow.setMinHeight(82);

            this.moneyLabel = new Label();
            moneyLabel.setStyle("-fx-font-size: 12px;");

            getChildren().addAll(headerLabel, cardRow, moneyLabel);
            setAlignment(Pos.CENTER);
            setMinWidth(190);
            setStyle(PANE_BASE_STYLE + IDLE_BORDER);
        }

        void render(BlackjackPlayer p) {
            int total = p.getHand().getValue();
            String suffix = p.getHand().getCards().isEmpty() ? "" : "  total: " + total;
            headerLabel.setText(displayName + suffix);

            cardRow.getChildren().clear();
            for (Card c : p.getHand().getCards()) {
                cardRow.getChildren().add(new CardPane(c));
            }

            moneyLabel.setText("$" + p.getBalance() + "   bet: $" + p.getCurrentBet());
        }

        void setActive(boolean active) {
            setStyle(PANE_BASE_STYLE + (active ? ACTIVE_BORDER : IDLE_BORDER));
        }
    }
}

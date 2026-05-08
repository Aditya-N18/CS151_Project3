package blackjack.view;

import blackjack.controller.BlackjackController;
import blackjack.controller.BlackjackGameLogic;
import blackjack.controller.BlackjackGameLogic.Phase;
import blackjack.model.BlackjackPlayer;
import blackjack.model.Dealer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import manager.controller.NavigationController;
import utils.MusicPlayer;

public class BlackjackGameView {

    private final NavigationController nav;
    private final VBox root;
    private final BlackjackGameLogic logic;
    private final BlackjackController controller;

    private final Label playerCardsLabel;
    private final Label dealerCardsLabel;
    private final Label balanceLabel;
    private final Label statusLabel;
    private final VBox gameOverPanel;
    private final Button hitButton;
    private final Button standButton;
    private final Button nextRoundButton;

    public BlackjackGameView(NavigationController nav) {
        this.nav = nav;
        this.logic = new BlackjackGameLogic();
        this.controller = new BlackjackController(this, logic);
        MusicPlayer.play("blackjack_theme.mp3");
        
        logic.startNewRound(100);

        playerCardsLabel = new Label();
        dealerCardsLabel = new Label();
        balanceLabel = new Label();
        statusLabel = new Label("Game Started");
        gameOverPanel = buildGameOverPanel();

        hitButton = new Button("Hit");
        standButton = new Button("Stand");
        nextRoundButton = new Button("Next Round");

        hitButton.setOnAction(e -> controller.handleHit());
        standButton.setOnAction(e -> controller.handleStand());
        nextRoundButton.setOnAction(e -> controller.handleNextRound());

        this.root = build();
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

    private VBox build() {

        Label title = new Label("Blackjack Game");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        VBox box = new VBox(
                15,
                title,
                dealerCardsLabel,
                playerCardsLabel,
                balanceLabel,
                statusLabel,
                gameOverPanel,
                hitButton,
                standButton,
                nextRoundButton);

        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));

        return box;
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

    public void refresh() {

        BlackjackPlayer player = logic.getHumanPlayer();
        Dealer dealer = logic.getDealer();

        playerCardsLabel.setText("Player Hand: " + player.getHand());
        dealerCardsLabel.setText("Dealer Hand: " + dealer.getHand());
        balanceLabel.setText("Balance: $" + player.getBalance());

        Phase phase = logic.getPhase();
        boolean playerTurn = phase == Phase.PLAYER_TURN;
        boolean roundOver = phase == Phase.ROUND_OVER;
        boolean gameOver = phase == Phase.GAME_OVER;

        hitButton.setDisable(!playerTurn || gameOver);
        standButton.setDisable(!playerTurn || gameOver);
        nextRoundButton.setDisable(!roundOver || gameOver);

        gameOverPanel.setVisible(gameOver);
        gameOverPanel.setManaged(gameOver);
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }
}

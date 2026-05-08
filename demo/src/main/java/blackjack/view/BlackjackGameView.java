package blackjack.view;

import blackjack.controller.BlackjackController;
import blackjack.controller.BlackjackGameLogic;
import blackjack.model.BlackjackPlayer;
import blackjack.model.Dealer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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

        this.root = build();
        refresh();
    }

    public Region getRoot() {
        return root;
    }

    private VBox build() {

        Label title = new Label("Blackjack Game");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Button hitButton = new Button("Hit");
        Button standButton = new Button("Stand");

        hitButton.setOnAction(e -> controller.handleHit());
        standButton.setOnAction(e -> controller.handleStand());


        VBox box = new VBox(
                15,
                title,
                dealerCardsLabel,
                playerCardsLabel,
                balanceLabel,
                statusLabel,
                hitButton,
                standButton
        );

        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));

        return box;
    }

    public void refresh() {

        BlackjackPlayer player = logic.getHumanPlayer();
        Dealer dealer = logic.getDealer();

        playerCardsLabel.setText(
                "Player Hand: " + player.getHand()
        );

        dealerCardsLabel.setText(
                "Dealer Hand: " + dealer.getHand()
        );

        balanceLabel.setText(
                "Balance: $" + player.getBalance()
        );
    }
    public void setStatus(String message){
        statusLabel.setText(message);
    }
}
package blackjack.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import manager.controller.NavigationController;

//Start or load game menu
public class BlackjackMenuView {

    private final NavigationController nav;
    private final VBox root;

    public BlackjackMenuView(NavigationController nav) {
        this.nav = nav;
        this.root = build();
    }

    public Region getRoot() {
        return root;
    }

    private VBox build() {
        Label title = new Label("Blackjack Menu");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Button gameBtn = new Button("Blackjack Game");
        gameBtn.setPrefWidth(200);
        gameBtn.setPrefHeight(50);
        gameBtn.setStyle("-fx-font-size: 16px;");
        gameBtn.setOnAction(e -> nav.goToBlackjackGame());

        VBox box = new VBox(14, title, gameBtn);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        return box;
    }
}

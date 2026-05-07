package blackjack.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import manager.controller.NavigationController;

//Blackjack game interface
public class BlackjackGameView {

    private final NavigationController nav;
    private final VBox root;

    public BlackjackGameView(NavigationController nav) {
        this.nav = nav;
        this.root = build();
    }

    public Region getRoot() {
        return root;
    }

    private VBox build() {
        Label title = new Label("Blackjack Game");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        VBox box = new VBox(12, title);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        return box;
    }
}

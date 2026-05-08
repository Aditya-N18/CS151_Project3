package blackjack.view;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
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
        title.setStyle(
                "-fx-font-size: 32px;"
                        + " -fx-font-weight: bold;"
                        + " -fx-text-fill: white;"
                        + " -fx-effect: dropshadow(gaussian, black, 8, 0.4, 0, 2);");

        Button startBtn = new Button("Start New Game");
        styleMenuButton(startBtn);
        startBtn.setOnAction(e -> nav.goToBlackjackGame());

        Button loadBtn = new Button("Load Game");
        styleMenuButton(loadBtn);
        loadBtn.setOnAction(e -> handleLoad());

        VBox box = new VBox(14, title, startBtn, loadBtn);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        applyBackground(box);
        return box;
    }

    private static void applyBackground(Region region) {
        Image image = new Image(
                BlackjackMenuView.class.getResource("/images/blackjack_menu_bg.png")
                        .toExternalForm(),
                false);
        BackgroundImage bg = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        true,
                        true,
                        false,
                        true));
        region.setBackground(new Background(bg));
    }

    private static void styleMenuButton(Button b) {
        b.setPrefWidth(220);
        b.setPrefHeight(54);
        b.setStyle(
                "-fx-font-size: 16px;"
                        + " -fx-font-weight: bold;"
                        + " -fx-background-color: rgba(255, 255, 255, 0.92);"
                        + " -fx-text-fill: #1a1a1a;"
                        + " -fx-background-radius: 8;"
                        + " -fx-border-color: #d4a017;"
                        + " -fx-border-width: 2;"
                        + " -fx-border-radius: 8;");
    }

    private void handleLoad() {
        Optional<String> token = promptForToken();
        if (token.isEmpty()) {
            return;
        }
        String trimmed = token.get().trim();
        if (trimmed.isEmpty()) {
            return;
        }
        try {
            nav.goToBlackjackGameFromSave(trimmed);
        } catch (RuntimeException ex) {
            Alert alert = new Alert(AlertType.ERROR,
                    "Invalid save string.");
            alert.setHeaderText("Could not load game");
            alert.showAndWait();
        }
    }

    private Optional<String> promptForToken() {
        TextArea input = new TextArea();
        input.setPromptText("Paste your save string here");
        input.setWrapText(true);
        input.setPrefRowCount(6);
        input.setPrefColumnCount(48);

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Load Game");
        dialog.setHeaderText("Paste a save string to resume your game.");
        dialog.getDialogPane().setContent(input);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return input.getText();
            }
            return null;
        });
        // Focus the paste target so users can Ctrl+V immediately.
        javafx.application.Platform.runLater(input::requestFocus);
        return dialog.showAndWait();
    }
}

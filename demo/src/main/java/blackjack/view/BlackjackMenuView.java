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

        Button startBtn = new Button("Start New Game");
        styleMenuButton(startBtn);
        startBtn.setOnAction(e -> nav.goToBlackjackGame());

        Button loadBtn = new Button("Load Game");
        styleMenuButton(loadBtn);
        loadBtn.setOnAction(e -> handleLoad());

        VBox box = new VBox(14, title, startBtn, loadBtn);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        return box;
    }

    private static void styleMenuButton(Button b) {
        b.setPrefWidth(200);
        b.setPrefHeight(50);
        b.setStyle("-fx-font-size: 16px;");
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

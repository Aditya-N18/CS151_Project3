package manager.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

//toolbar is always visible post login

public class ToolbarView {

    private final HBox root = new HBox(8);
    private final Button mainMenuBtn = new Button("Main Menu");
    private final HBox extrasArea = new HBox(8);
    private final Label userLabel = new Label("");
    private Runnable onMainMenu = () -> {
    };

    public ToolbarView() {
        root.setPadding(new Insets(8, 12, 8, 12));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle("-fx-background-color: #2b3140; -fx-border-color: #1a1f2b; -fx-border-width: 0 0 1 0;");

        mainMenuBtn.setOnAction(e -> onMainMenu.run());
        mainMenuBtn.setStyle("-fx-font-weight: bold;");

        extrasArea.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        userLabel.setStyle("-fx-text-fill: #cfd6e6;");

        root.getChildren().addAll(
                mainMenuBtn,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                extrasArea,
                spacer,
                userLabel);
    }

    public Node createToolbar() {
        return root;
    }

    public Node getNode() {
        return root;
    }

    public void setOnMainMenu(Runnable action) {
        this.onMainMenu = action == null ? () -> {
        } : action;
    }

    public void addButton(String label, Runnable action) {
        Button b = new Button(label);
        b.setOnAction(e -> {
            if (action != null)
                action.run();
        });
        extrasArea.getChildren().add(b);
    }

    public void addLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: #cfd6e6; -fx-padding: 0 4 0 4;");
        extrasArea.getChildren().add(lbl);
    }

    public void clearExtras() {
        extrasArea.getChildren().clear();
    }

    public void setUserLabel(String username) {
        if (username == null || username.isBlank()) {
            userLabel.setText("");
        } else {
            userLabel.setText("Signed in: " + username);
        }
    }

    public void updateForScene(String sceneName) {
        clearExtras();
        if (sceneName == null) {
            return;
        }
        switch (sceneName) {
            case "mainmenu":
            case "blackjack-menu":
                break;
            case "blackjack-game":
                addButton("Save State", () -> {
                });
                break;
            case "snake":
                addLabel("Pause (ESC)");
                break;
            default:
                break;
        }
    }
}

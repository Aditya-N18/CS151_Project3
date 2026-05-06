package manager.view;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import manager.controller.MainMenuController;
import manager.score.HighScoreEntry;

//leaderboard and game launching buttons

public class MainMenuView {

    private static final int TOP_N = 5;

    private final MainMenuController controller;
    private final HBox root = new HBox(16);
    private final VBox blackjackList = new VBox(4);
    private final VBox snakeList = new VBox(4);

    public MainMenuView(MainMenuController controller) {
        this.controller = controller;
        build();
    }

    public Region getRoot() {
        return root;
    }

    private void build() {
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);

        VBox leftPane = buildLeaderboards();
        GridPane rightPane = buildButtonGrid();

        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

        root.getChildren().addAll(leftPane, rightPane);
    }

    private VBox buildLeaderboards() {
        Label header = new Label("Leaderboards");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox blackjackBox = leaderboardBox("Top 5 \u2014 Blackjack", blackjackList);
        VBox snakeBox = leaderboardBox("Top 5 \u2014 Snake", snakeList);

        VBox box = new VBox(16, header, blackjackBox, snakeBox);
        box.setPadding(new Insets(10));
        box.setStyle(
                "-fx-background-color: #f3f5fa; -fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #d6dbe5;");
        return box;
    }

    private VBox leaderboardBox(String title, VBox listHolder) {
        Label t = new Label(title);
        t.setStyle("-fx-font-weight: bold;");
        listHolder.setPadding(new Insets(4, 0, 4, 0));
        VBox box = new VBox(4, t, listHolder);
        return box;
    }

    private GridPane buildButtonGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        Button blackjackBtn = bigButton("Blackjack");
        // blackjackBtn.setOnAction(e -> controller.launchBlackjack());

        Button snakeBtn = bigButton("Snake");
        snakeBtn.setOnAction(e -> controller.launchSnake());

        Button future1 = bigButton("Future Game 1");
        future1.setDisable(true);
        future1.setTooltip(new Tooltip("Reserved for future games."));

        Button future2 = bigButton("Future Game 2");
        future2.setDisable(true);
        future2.setTooltip(new Tooltip("Reserved for future games."));

        grid.add(blackjackBtn, 0, 0);
        grid.add(snakeBtn, 1, 0);
        grid.add(future1, 0, 1);
        grid.add(future2, 1, 1);

        return grid;
    }

    private Button bigButton(String label) {
        Button b = new Button(label);
        b.setPrefSize(160, 80);
        b.setStyle("-fx-font-size: 16px;");
        return b;
    }

    public void setBlackjackTop(List<HighScoreEntry> entries) {
        renderList(blackjackList, entries);
    }

    public void setSnakeTop(List<HighScoreEntry> entries) {
        renderList(snakeList, entries);
    }

    private void renderList(VBox listHolder, List<HighScoreEntry> entries) {
        listHolder.getChildren().clear();
        if (entries == null || entries.isEmpty()) {
            listHolder.getChildren().add(new Label("(no scores yet)"));
            return;
        }
        int rank = 1;
        int limit = Math.min(TOP_N, entries.size());
        for (int i = 0; i < limit; i++) {
            HighScoreEntry e = entries.get(i);
            Label row = new Label(String.format("%d. %-14s %d",
                    rank++, e.getUsername(), e.getScore()));
            row.setStyle("-fx-font-family: 'monospace';");
            listHolder.getChildren().add(row);
        }
    }
}

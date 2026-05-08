package snake.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import manager.controller.NavigationController;
import utils.MusicPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import manager.controller.NavigationController;
import snake.controller.SnakeGameController;
import snake.model.GridPoint;
import snake.model.SnakeGameModel;

public class SnakeGameView {

    private static final int TILE_SIZE = 20;
    private static final int COLS = 30;
    private static final int ROWS = 25;

    private final Pane root;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final Label scoreLabel;

    private final SnakeGameModel model;

    public SnakeGameView(NavigationController nav) {
        this.model = new SnakeGameModel(COLS, ROWS);

        root = new Pane();
        root.setStyle("-fx-background-color: #111;");

        scoreLabel = new Label();
        scoreLabel.setLayoutX(20);
        scoreLabel.setLayoutY(10);
        scoreLabel.setFont(Font.font(20));
        scoreLabel.setTextFill(Color.WHITE);

        canvas = new Canvas(COLS * TILE_SIZE, ROWS * TILE_SIZE);
        canvas.setLayoutX(20);
        canvas.setLayoutY(50);

        gc = canvas.getGraphicsContext2D();
        MusicPlayer.play("snake_theme.mp3");

        root.getChildren().addAll(scoreLabel, canvas);

        draw();

        new SnakeGameController(model, this, nav);
    }

    public Region getRoot() {
        return root;
    }

    public void draw() {
        drawBackground();
        drawFood();
        drawSnake();
        drawScore();

        if (model.isPaused()) {
            drawPausedScreen();
        }

        if (model.isGameOver()) {
            drawGameOverScreen();
        }
    }

    private void drawBackground() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.WHITE);
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawSnake() {
        gc.setFill(Color.LIMEGREEN);

        for (GridPoint part : model.getSnake()) {
            gc.fillRect(
                    part.getX() * TILE_SIZE,
                    part.getY() * TILE_SIZE,
                    TILE_SIZE - 1,
                    TILE_SIZE - 1);
        }
    }

    private void drawFood() {
        GridPoint food = model.getFood();

        gc.setFill(Color.RED);
        gc.fillOval(
                food.getX() * TILE_SIZE,
                food.getY() * TILE_SIZE,
                TILE_SIZE,
                TILE_SIZE);
    }

    private void drawScore() {
        scoreLabel.setText("Score: " + model.getScore());
    }

    private void drawPausedScreen() {
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(40));
        gc.fillText("PAUSED", 220, 250);
    }

    private void drawGameOverScreen() {
        gc.setFill(Color.color(0, 0, 0, 0.7));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(36));
        gc.fillText("GAME OVER", 185, 210);

        gc.setFont(Font.font(24));
        gc.fillText("Final Score: " + model.getScore(), 210, 260);
        gc.fillText("Press R to Restart", 190, 310);
    }
}
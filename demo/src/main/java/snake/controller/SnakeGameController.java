package snake.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import snake.model.Direction;
import snake.model.SnakeGameModel;
import snake.view.SnakeGameView;

public class SnakeGameController {

    private final SnakeGameModel model;
    private final SnakeGameView view;
    private Timeline gameLoop;

    public SnakeGameController(SnakeGameModel model, SnakeGameView view) {
        this.model = model;
        this.view = view;

        setupKeyboardControls();
        setupGameLoop();
    }

    private void setupKeyboardControls() {
        view.getRoot().setFocusTraversable(true);

        view.getRoot().setOnKeyPressed(event -> {
            KeyCode key = event.getCode();

            if (key == KeyCode.UP) {
                model.changeDirection(Direction.UP);
            } else if (key == KeyCode.DOWN) {
                model.changeDirection(Direction.DOWN);
            } else if (key == KeyCode.LEFT) {
                model.changeDirection(Direction.LEFT);
            } else if (key == KeyCode.RIGHT) {
                model.changeDirection(Direction.RIGHT);
            } else if (key == KeyCode.ESCAPE) {
                model.togglePause();
                view.draw();
            } else if (key == KeyCode.R && model.isGameOver()) {
                restartGame();
            }
        });

        view.getRoot().requestFocus();
    }

    private void setupGameLoop() {
        gameLoop = new Timeline(
                new KeyFrame(Duration.millis(150), event -> updateGame()));

        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    private void updateGame() {
        model.update();
        view.draw();

        if (model.isGameOver()) {
            gameLoop.stop();

            // Later we will save high score here.
            // Example:
            // nav.getHighScoreManager().recordScore(...)
        }
    }

    private void restartGame() {
        model.reset();
        view.draw();
        gameLoop.play();
    }
}
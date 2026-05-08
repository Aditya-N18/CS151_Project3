package snake.controller;

import javafx.scene.input.KeyEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import snake.model.Direction;
import snake.model.SnakeGameModel;
import snake.view.SnakeGameView;
import manager.controller.NavigationController;

public class SnakeGameController {

    private final SnakeGameModel model;
    private final SnakeGameView view;
    private Timeline gameLoop;
    private NavigationController nav;
    private boolean scoreSaved = false;

    public SnakeGameController(SnakeGameModel model, SnakeGameView view, NavigationController nav) {
        this.model = model;
        this.view = view;
        this.nav = nav;

        setupKeyboardControls();
        setupGameLoop();
    }

    private void setupKeyboardControls() {
        view.getRoot().setFocusTraversable(true);

        javafx.scene.Scene scene = view.getRoot().getScene();
        if (scene != null) {
            attachKeyListener(scene);
        } else {

            view.getRoot().sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    attachKeyListener(newScene);
                }
            });
        }

        Platform.runLater(() -> view.getRoot().requestFocus());
    }

    private void attachKeyListener(javafx.scene.Scene scene) {
        scene.setOnKeyPressed(event -> {
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
            event.consume();
        });

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
            saveScoreOnce();
            gameLoop.stop();
        }
    }

    private void saveScoreOnce() {
        if (scoreSaved) {
            return;
        }

        scoreSaved = true;

        String username = nav.getCurrentUser();
        int score = model.getScore();

        nav.getHighScoreManager().recordScore(username, "Snake", score);
    }

    private void restartGame() {
        gameLoop.stop();

        model.reset();
        view.draw();

        gameLoop = new Timeline(
                new KeyFrame(Duration.millis(150), event -> updateGame()));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }
}
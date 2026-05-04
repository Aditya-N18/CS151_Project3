package snake.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.canvas.Canvas;
import manager.controller.NavigationController;

public class SnakeGameView {
    private final Pane root;
    private Canvas canvas;
    private GraphicsContext gc;

    public SnakeGameView(NavigationController nav) {
        root = new Pane();
        canvas = new Canvas(400, 400);
        gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        drawSnake();
    }

    public Region getRoot() {
        return root;
    }

    private void drawSnake() {
        gc.fillRect(50, 50, 10, 10); // one square = snake body
    }
}

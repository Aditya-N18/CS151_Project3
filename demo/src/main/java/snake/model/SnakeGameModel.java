package snake.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SnakeGameModel {

    private final int cols;
    private final int rows;
    private final Random random = new Random();

    private final List<GridPoint> snake = new ArrayList<>();

    private GridPoint food;
    private Direction direction;
    private Direction nextDirection;

    private int score;
    private boolean gameOver;
    private boolean paused;

    public SnakeGameModel(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        reset();
    }

    public void reset() {
        snake.clear();

        int startX = cols / 2;
        int startY = rows / 2;

        Direction[] directions = Direction.values();
        direction = directions[random.nextInt(directions.length)];
        nextDirection = direction;

        snake.add(new GridPoint(startX, startY));

        switch (direction) {
            case UP:
                snake.add(new GridPoint(startX, startY + 1));
                snake.add(new GridPoint(startX, startY + 2));
                break;

            case DOWN:
                snake.add(new GridPoint(startX, startY - 1));
                snake.add(new GridPoint(startX, startY - 2));
                break;

            case LEFT:
                snake.add(new GridPoint(startX + 1, startY));
                snake.add(new GridPoint(startX + 2, startY));
                break;

            case RIGHT:
                snake.add(new GridPoint(startX - 1, startY));
                snake.add(new GridPoint(startX - 2, startY));
                break;
        }

        score = 0;
        gameOver = false;
        paused = false;

        spawnFood();
    }

    public void update() {
        if (gameOver || paused) {
            return;
        }

        direction = nextDirection;

        GridPoint head = snake.get(0);
        GridPoint newHead = head.move(direction);

        if (hitWall(newHead) || hitSelf(newHead)) {
            gameOver = true;
            return;
        }

        snake.add(0, newHead);

        if (newHead.equals(food)) {
            score += 10;
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    public void changeDirection(Direction newDirection) {
        if (newDirection == Direction.UP && direction != Direction.DOWN) {
            nextDirection = Direction.UP;
        } else if (newDirection == Direction.DOWN && direction != Direction.UP) {
            nextDirection = Direction.DOWN;
        } else if (newDirection == Direction.LEFT && direction != Direction.RIGHT) {
            nextDirection = Direction.LEFT;
        } else if (newDirection == Direction.RIGHT && direction != Direction.LEFT) {
            nextDirection = Direction.RIGHT;
        }
    }

    public void togglePause() {
        if (!gameOver) {
            paused = !paused;
        }
    }

    private boolean hitWall(GridPoint point) {
        return point.getX() < 0
                || point.getX() >= cols
                || point.getY() < 0
                || point.getY() >= rows;
    }

    private boolean hitSelf(GridPoint point) {
        return snake.contains(point);
    }

    private void spawnFood() {
        while (true) {
            GridPoint newFood = new GridPoint(
                    random.nextInt(cols),
                    random.nextInt(rows));

            if (!snake.contains(newFood)) {
                food = newFood;
                return;
            }
        }
    }

    public List<GridPoint> getSnake() {
        return Collections.unmodifiableList(snake);
    }

    public GridPoint getFood() {
        return food;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isPaused() {
        return paused;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }
}
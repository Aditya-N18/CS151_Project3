package snake.controller;

import snake.model.*;
import java.util.Random;
public class SnakeGameLogic {
    private final int rows;
    private final int cols;
    private Snake snake;
    private Food food;
    private int score;
    private boolean paused;
    private boolean gameOver;
    private Random random;

    public SnakeGameLogic(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.random = new Random();
        resetGame();
    }

    public void resetGame(){
        this.score = 0;
        this.gameOver = false;
        this.paused = false;

        int startRow = random.nextInt(rows);
        int startCol = random.nextInt(cols);
        Position startPos = new Position(startRow, startCol);

        Direction[] directions = Direction.values();
        Direction startDir = directions[random.nextInt(directions.length)];

        this.snake = new Snake(startPos, startDir);

        spawnFood();
    }

    public void startGame(){
        resetGame();
    }

    public void update(){
        if (gameOver || paused)
            return;

        boolean eating = hasEatenFood();

        snake.move(eating);

        if (eating){
            score += 1;
            spawnFood();
        }

        if (isOutOfBounds() || snake.hitsItself()){
            gameOver = true;
        }
    }

    public void changeDirection(Direction direction){
        if (!snake.getDirection().isOpposite(direction)){
            snake.setDirection(direction);
        }
    }

    private boolean hasEatenFood(){
        Position head = snake.getHead();
        int nextRow = head.getRow();
        int nextCol = head.getCol();

        switch(snake.getDirection()){
            case UP:
                nextRow--;
                break;
            case DOWN:
                nextRow++;
                break;
            case LEFT:
                nextCol--;
                break;
            case RIGHT:
                nextCol++;
                break;
        }

        Position nextPos = new Position(nextRow, nextCol);
        return nextPos.equals(food.getPosition());
    }

    public boolean hasCollision(){
        return isOutOfBounds() || snake.hitsItself();
    }

    private boolean isOutOfBounds(){
        Position head = snake.getHead();
        return head.getRow()<0 || head.getRow() >= rows ||
               head.getCol()<0 || head.getCol() >= cols;
    }

    public void spawnFood(){
        Position newPos;

        do{
            newPos = new Position(random.nextInt(rows), random.nextInt(cols));
        } while (snake.collidesWith(newPos));

        if (food == null){
            food = new Food(newPos);
        }
        else {
            food.setPosition(newPos);
        }
    }

    public void pause(){
        this.paused = true;
    }
    public Snake getSnake(){
        return snake;
    }

    public Food getFood(){
        return food;
    }

    public int getScore(){
        return score;
    }

    public boolean isPaused(){
        return paused;
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public void togglePause(){
        this.paused = !this.paused;
    }

}

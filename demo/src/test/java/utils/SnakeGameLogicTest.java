package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import snake.controller.SnakeGameLogic;
import snake.model.Position;
import static org.junit.jupiter.api.Assertions.*;

public class SnakeGameLogicTest {
    private SnakeGameLogic logic;
    private final int SIZE = 10;

    @BeforeEach
    void setUp() {
        logic = new SnakeGameLogic(SIZE, SIZE);
    }

    @Test
    void testGameOverOnWallHit() {
        for (int i = 0; i < SIZE; i++) {
            logic.update();
        }
        assertTrue(logic.isGameOver(), "Game should be over after hitting a wall");
    }

    @Test
    void testScoreIncrements() {
        int initialScore = logic.getScore();
        
        // Manually placing food in front of the snake
        Position head = logic.getSnake().getHead();
        logic.getFood().setPosition(new Position(head.getRow(), head.getCol() + 1));
        
        logic.getSnake().setDirection(snake.model.Direction.RIGHT);
        
        logic.update();
        
        assertTrue(logic.getScore() > initialScore, "Score should increase after eating");
    }

    @Test
    void testTogglePause() {
        assertFalse(logic.isPaused());
        logic.togglePause();
        assertTrue(logic.isPaused());
        logic.togglePause();
        assertFalse(logic.isPaused());
    }
}

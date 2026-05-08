package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import snake.model.*;
import static org.junit.jupiter.api.Assertions.*;

public class SnakeTest {
    private Snake snake;
    private Position startPos;

    @BeforeEach
    void setUp() {
        startPos = new Position(5, 5);
        snake = new Snake(startPos, Direction.RIGHT);
    }

    @Test
    void testInitialState() {
        assertEquals(1, snake.length());
        assertEquals(startPos, snake.getHead());
    }

    @Test
    void testMoveWithoutGrowth() {
        snake.move(false);
        assertEquals(1, snake.length());
        assertEquals(new Position(5, 6), snake.getHead());
        assertFalse(snake.collidesWith(startPos), "Tail should have been removed");
    }

    @Test
    void testMoveWithGrowth() {
        snake.move(true);
        assertEquals(2, snake.length());
        assertEquals(new Position(5, 6), snake.getHead());
        assertTrue(snake.collidesWith(startPos), "Tail should still be there after growing");
    }

    @Test
    void testHitsItself() {
        // Grow snake to length 5
        for(int i = 0; i < 4; i++) {
            snake.move(true);
        }
        
        // Force a circle: Right -> Down -> Left -> Up
        snake.setDirection(Direction.DOWN);
        snake.move(false);
        snake.setDirection(Direction.LEFT);
        snake.move(false);
        snake.setDirection(Direction.UP);
        snake.move(false);
        
        assertTrue(snake.hitsItself(), "Snake should detect it hits its own body");
    }
}

package snake.model;

import java.util.Objects;

public class GridPoint {

    private final int x;
    private final int y;

    public GridPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public GridPoint move(Direction direction) {
        switch (direction) {
            case UP:
                return new GridPoint(x, y - 1);
            case DOWN:
                return new GridPoint(x, y + 1);
            case LEFT:
                return new GridPoint(x - 1, y);
            case RIGHT:
                return new GridPoint(x + 1, y);
            default:
                return this;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GridPoint)) {
            return false;
        }

        GridPoint other = (GridPoint) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
package snake.model;

import java.util.Objects;

public class Position {
    private final int row;
    private final int col;

    public Position (int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        Position pos = (Position) obj;
            return row == pos.row && col == pos.col;
    }

    public int hashCode(){
        return Objects.hash(row, col);
    }
}
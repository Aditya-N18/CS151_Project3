package snake.model;

import java.util.LinkedList;
import java.util.List;

public class Snake {
    private LinkedList<Position> body;
    private Direction direction;


    public Snake(Position start, Direction direction){
        body = new LinkedList<>();
            body.add(start);
            this.direction = direction;
    }
    
    public List<Position> getBody(){
        return body;
    }

    public Position getHead(){
        return body.getFirst();
    }

    public Direction getDirection(){
        return direction;
    }

    public void setDirection(Direction d){
        this.direction = d;
    }

    public void move (boolean grow){
        Position head = getHead();
        int newRow = head.getRow();
        int newCol = head.getCol();

        //calculating new head posiition based on current direction
        switch (direction){
            case UP:
                newRow--;
                break;
            case DOWN:
                newRow++;
                break;
            case LEFT:
                newCol--;
                break;
            case RIGHT:
                newCol++;
                break;
        }

        body.addFirst(new Position(newRow, newCol));

        if(!grow){
            body.removeLast();
        }
    }

    public boolean hitsItself(){
        Position head = getHead();
        for (int i = 1; i < body.size(); i++){
            if (head.equals(body.get(i)))
                return true;
        }
        return false;
    }

    //@Override
    public boolean occupies (Position position){
        return body.contains(position);
    }
    
    public int length(){
        return body.size();
    }

    public void reset (Position start, Direction direction){
        body.clear();
        body.add(start);
        this.direction = direction;
    }
}

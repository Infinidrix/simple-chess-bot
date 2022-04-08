package player;

import board.Board;

abstract public class Player {
    boolean color;
    Player(boolean color) {
        this.color = color;
    }

    abstract public void makeMove(Board board);
}

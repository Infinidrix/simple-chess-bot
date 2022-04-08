package player;

import board.Board;

abstract public class Player {
    boolean color;
    Player(boolean color) {
        this.color = color;
    }

    abstract void makeMove(Board board);
}

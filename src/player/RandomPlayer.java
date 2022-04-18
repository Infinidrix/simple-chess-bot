package player;

import board.Board;
import board.MoveHistory;
import gameUI.MainUI;

import java.util.ArrayList;
import java.util.Random;

import static board.Board.BLACK;
import static board.Board.WHITE;

public class RandomPlayer extends Player{
    RandomPlayer(boolean color) {
        super(color);
    }

    @Override
    public void makeMove(Board board) {
        var possibleMoves = board.getPossibleMoves(color);

        var nextMoveInd = (new Random()).nextInt(possibleMoves.size());

        var nextMove = possibleMoves.get(nextMoveInd);
        board.movePiece(nextMove.getPiece(), nextMove.getNewLoc());
    }

    public static void main(String[] args) {
        new MainUI(new RandomPlayer(WHITE), new RandomPlayer(BLACK), new Board());
    }
}

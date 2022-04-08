package player;

import board.Board;
import board.MoveHistory;

import java.util.ArrayList;
import java.util.Random;

import static board.Board.WHITE;

public class RandomPlayer extends Player{
    RandomPlayer(boolean color) {
        super(color);
    }

    @Override
    void makeMove(Board board) {
        var pieces = board.BlackPieces;
        if (color == WHITE){
            pieces = board.WhitePieces;
        }
        ArrayList<MoveHistory> possibleMoves = new ArrayList<>();
        for (var piece : pieces) {
            for (var move : piece.generateMovesValid(board)){
                possibleMoves.add(new MoveHistory(piece.coord, move, piece, board.findPiece(move)));
            }
        }

        var nextMoveInd = (new Random()).nextInt(possibleMoves.size());

        var nextMove = possibleMoves.get(nextMoveInd);
        board.movePiece(nextMove.getPiece(), nextMove.getNewLoc());
    }
}

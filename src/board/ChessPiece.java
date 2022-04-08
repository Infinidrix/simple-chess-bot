package board;

import java.util.Arrays;
import java.util.stream.Collectors;

import static board.Board.BLACK;
import static board.Board.KING;

abstract public class ChessPiece {
    public Coord coord;
    public boolean color;
    public int type;

    @Override
    public String toString() {
        return "ChessPiece{" +
                "coord=" + coord +
                ", color=" + color +
                '}';
    }

    public abstract Coord[] generateMoves(Board board);

    public Coord[] generateMovesValid(Board board){
        System.out.println(Arrays.toString(generateMoves(board)));
        return Arrays.stream(generateMoves(board)).filter(e -> {
            System.out.println(board.printBoard());
            if (board.movePiece(this, e)) {
                System.out.println("Got to undoing " + e);
                board.undo();
                return true;
            }
            return false;
        }).toArray(Coord[]::new);
    }

    public boolean canCheck(Board board) {
        Coord targetCoord = board.BlackPieces[KING].coord;
        if (color == BLACK)
            targetCoord = board.WhitePieces[KING].coord;
        for (Coord move : generateMoves(board)) {
            if (move.equals(targetCoord))
                return true;
        }
        return false;
    }
}

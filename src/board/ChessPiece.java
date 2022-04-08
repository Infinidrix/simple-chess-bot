package board;

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

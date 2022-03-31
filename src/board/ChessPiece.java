package board;

import static board.Board.BLACK;
import static board.Board.KING;

abstract public class ChessPiece {
    Coord coord;
    boolean color;

    @Override
    public String toString() {
        return "ChessPiece{" +
                "coord=" + coord +
                ", color=" + color +
                '}';
    }

    abstract Coord[] generateMoves(Board board);

    boolean canCheck(Board board) {
        Coord targetCoord = board.BlackPieces[KING].coord;
        if (color == BLACK)
            targetCoord = board.WhitePieces[KING].coord;
            for (Coord move: generateMoves(board)) {
                if (move == targetCoord)
                    return true;
            }
            return false;
    }
}

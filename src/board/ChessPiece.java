package board;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static board.Board.*;

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
        return Arrays.stream(generateMoves(board)).filter(e -> {
            if (board.movePiece(this, e)) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type && coord.equals(that.coord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coord, color, type);
    }
}

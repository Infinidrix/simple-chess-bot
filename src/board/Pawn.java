package board;

import java.util.ArrayList;

import static board.Board.*;

public class Pawn extends ChessPiece{
    Pawn(Coord coord, boolean color){
        this.coord = coord;
        this.color = color;
    }

    Pawn(int x, int y, boolean color){
        this(new Coord(x, y), color);
    }

    @Override
    Coord[] generateMoves(Board board) {
        int dir = 1;
        if (!color) {
            dir = -1;
        }
        ArrayList<Coord> moves = new ArrayList<>();
        boolean rightLoc = false;// new Coord(coord.x + dir, coord.y + 1);
        boolean leftLoc = false; //new Coord(coord.x + dir, coord.y - 1);
        boolean nextUp = false;//new Coord(coord.x + dir, coord.y);
        boolean nextTwoUp = false;//new Coord(coord.x + dir + dir, coord.y);
        for (int i = 0; i < board.BlackPieces.length; i++){
            rightLoc = rightLoc ||
                    (board.BlackPieces[i].color != color && board.BlackPieces[i].coord.equals(new Coord(coord.x + dir, coord.y + 1))) ||
                    (board.WhitePieces[i].color != color && board.WhitePieces[i].coord.equals(new Coord(coord.x + dir, coord.y + 1)));
            leftLoc = leftLoc ||
                    (board.BlackPieces[i].color != color && board.BlackPieces[i].coord.equals(new Coord(coord.x + dir, coord.y - 1))) ||
                    (board.WhitePieces[i].color != color && board.WhitePieces[i].coord.equals(new Coord(coord.x + dir, coord.y - 1)));
            nextUp = nextUp ||
                    board.BlackPieces[i].coord.equals(new Coord(coord.x + dir, coord.y)) ||
                    board.WhitePieces[i].coord.equals(new Coord(coord.x + dir, coord.y));
            nextTwoUp = nextTwoUp ||
                    board.BlackPieces[i].coord.equals(new Coord(coord.x + dir + dir, coord.y)) ||
                    board.WhitePieces[i].coord.equals(new Coord(coord.x + dir + dir, coord.y));
        }
        if (rightLoc){
            moves.add(new Coord(coord.x + dir, coord.y + 1));
        }
        if (leftLoc){
            moves.add(new Coord(coord.x + dir, coord.y - 1));
        }
        if (!nextUp && coord.x + dir >= 0 && coord.x + dir < BOARD_SIZE){
            moves.add(new Coord(coord.x + dir, coord.y));
        }
        if (!nextUp &&
                !nextTwoUp &&
                ((color == WHITE && coord.x == 1) || (color == BLACK && coord.x == 6)) &&
                coord.x + (2*dir) >= 0 && coord.x + (2*dir) < BOARD_SIZE
        ){
            moves.add(new Coord(coord.x + dir + dir, coord.y));
        }
        return moves.toArray(new Coord[]{});
    }
}

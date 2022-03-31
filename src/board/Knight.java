package board;

import java.util.ArrayList;

import static board.Board.*;

public class Knight extends ChessPiece{
    Knight(Coord coord, boolean color){
        this.coord = coord;
        this.color = color;
    }

    Knight(int x, int y, boolean color){
        this(new Coord(x, y), color);
    }

    @Override
    Coord[] generateMoves(Board board) {
        ArrayList<Coord> moves = new ArrayList<>();
        for (int move1 : new int[]{2, -2}){
            for (int move2 : new int[]{1, -1}){
                Coord fullMove1 = new Coord(coord.x + move1, coord.y + move2);
                if (fullMove1.x >= 0 && fullMove1.x < BOARD_SIZE &&
                        fullMove1.y >= 0 && fullMove1.y < BOARD_SIZE &&
                        (board.findPiece(fullMove1) == null ||
                        board.findPiece(fullMove1).color != color)
                ) {
                    moves.add(fullMove1);
                }
                Coord fullMove2 = new Coord(coord.x + move2, coord.y + move1);
                if (fullMove2.x >= 0 && fullMove2.x < BOARD_SIZE &&
                        fullMove2.y >= 0 && fullMove2.y < BOARD_SIZE &&
                        (board.findPiece(fullMove2) == null ||
                        board.findPiece(fullMove2).color != color)
                ) {
                    moves.add(fullMove2);
                }
            }
        }
        return moves.toArray(new Coord[]{});
    }
}

package board;

import java.util.ArrayList;

import static board.Board.*;

public class Queen extends ChessPiece{
    Queen(Coord coord, boolean color, int type){
        this.coord = coord;
        this.color = color;
        this.type = type;
    }

    Queen(int x, int y, boolean color, int type){
        this(new Coord(x, y), color, type);
    }
    @Override
    public Coord[] generateMoves(Board board) {
        ArrayList<Coord> moves = new ArrayList<>();
        for (int move1 : new int[]{1, -1}){
            for (int move2 : new int[]{1, -1}){
                Coord fullMove1 = new Coord(coord.x + move1, coord.y + move2);
                while (fullMove1.x >= 0 && fullMove1.x < BOARD_SIZE &&
                        fullMove1.y >= 0 && fullMove1.y < BOARD_SIZE){
                    var piece = board.findPiece(fullMove1);
                    if (piece != null){
                        if (piece.color != color){
                            moves.add(new Coord(fullMove1.x, fullMove1.y));
                        }
                        break;
                    }
                    moves.add(new Coord(fullMove1.x, fullMove1.y));
                    fullMove1.x += move1;
                    fullMove1.y += move2;
                }
            }
        }
        System.out.println(moves);
        for (int move1 : new int[]{1, -1}){
            for (boolean flip : new boolean[]{true, false}){
                Coord fullMove1 = new Coord(coord.x + move1, coord.y);
                if (flip)
                    fullMove1 = new Coord(coord.x, coord.y + move1);

                while (fullMove1.x >= 0 && fullMove1.x < BOARD_SIZE &&
                        fullMove1.y >= 0 && fullMove1.y < BOARD_SIZE){
                    var piece = board.findPiece(fullMove1);
                    if (piece != null){
                        if (piece.color != color){
                            moves.add(new Coord(fullMove1.x, fullMove1.y));
                        }
                        break;
                    }
                    moves.add(new Coord(fullMove1.x, fullMove1.y));
                    if (flip){
                        fullMove1.y += move1;
                    } else {
                        fullMove1.x += move1;
                    }
                }
            }
        }
        System.out.println(moves);
        return moves.toArray(new Coord[]{});
    }
}

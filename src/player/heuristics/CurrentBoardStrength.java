package player.heuristics;

import board.Board;

import java.util.HashMap;

import static board.Board.*;

public class CurrentBoardStrength {
    static final HashMap<Integer, Integer> pieceValue = new HashMap<>();
    static {
        pieceValue.put(ROOK_LEFT, 5);
        pieceValue.put(KNIGHT_LEFT, 4);
        pieceValue.put(BISHOP_LEFT, 4);
        pieceValue.put(KING, 0);
        pieceValue.put(QUEEN, 9);
        pieceValue.put(BISHOP_RIGHT, 4);
        pieceValue.put(KNIGHT_RIGHT, 4);
        pieceValue.put(ROOK_RIGHT, 5);
        pieceValue.put(PAWN_1, 1);
        pieceValue.put(PAWN_2, 1);
        pieceValue.put(PAWN_3, 1);
        pieceValue.put(PAWN_4, 1);
        pieceValue.put(PAWN_5, 1);
        pieceValue.put(PAWN_6, 1);
        pieceValue.put(PAWN_7, 1);
        pieceValue.put(PAWN_8, 1);

    }
    public static int pieceStrengths(Board board, boolean color) {
        int whiteStrength = 0, blackStrength = 0;

        for (var whitePiece : board.WhitePieces ) {
            if (whitePiece.coord.getX() >= 0) {
                whiteStrength += pieceValue.get(whitePiece.type);
            }
        }
        for (var blackPiece : board.BlackPieces ) {
            if (blackPiece.coord.getX() >= 0) {
                blackStrength += pieceValue.get(blackPiece.type);
            }
        }
        if (color == WHITE){
            return whiteStrength - blackStrength;
        }
        return blackStrength - whiteStrength;
    }

    public static int winFactor(Board board, boolean color){
        board.updateGameStatus();
        var status = board.getCurrentStatus();

        if (status == Status.BLACK_WIN){
            return (color == BLACK) ? 10000 : -10000;
        } else if (status == Status.WHITE_WIN){
            return (color == WHITE) ? 10000 : -10000;
        }
        return 1;
    }
}

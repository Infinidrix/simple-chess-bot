package board;

import java.io.Console;
import java.util.*;

public class Board {
    static final int ROOK_LEFT = 0;
    static final int KNIGHT_LEFT = 1;
    static final int BISHOP_LEFT = 2;
    static final int KING = 3;
    static final int QUEEN = 4;
    static final int BISHOP_RIGHT = 5;
    static final int KNIGHT_RIGHT = 6;
    static final int ROOK_RIGHT = 7;
    static final int PAWN_1 = 8;
    static final int PAWN_2 = 9;
    static final int PAWN_3 = 10;
    static final int PAWN_4 = 11;
    static final int PAWN_5 = 12;
    static final int PAWN_6 = 13;
    static final int PAWN_7 = 14;
    static final int PAWN_8 = 15;
    public static String representations = "RHBKQBHRPPPPPPPP";

    static final int BOARD_SIZE = 8;
    public static final boolean WHITE = true;
    public static final boolean BLACK = false;
    static final Coord EATEN = new Coord(-100, -100);

    ChessPiece[] WhitePieces, BlackPieces;
    GameHistory gameHistory;

    public Board() {
        WhitePieces = new ChessPiece[16];
        BlackPieces = new ChessPiece[16];
        gameHistory = new GameHistory();

        WhitePieces[0] = new Rook(0, 0, WHITE, ROOK_LEFT);
        WhitePieces[1] = new Knight(0, 1, WHITE, KNIGHT_LEFT);
        WhitePieces[2] = new Bishop(0, 2, WHITE, BISHOP_LEFT);
        WhitePieces[3] = new King(0, 3, WHITE, KING);
        WhitePieces[4] = new Queen(0, 4, WHITE, QUEEN);
        WhitePieces[5] = new Bishop(0, 5, WHITE, BISHOP_RIGHT);
        WhitePieces[6] = new Knight(0, 6, WHITE, KNIGHT_RIGHT);
        WhitePieces[7] = new Rook(0, 7, WHITE, ROOK_RIGHT);

        BlackPieces[0] = new Rook(7, 0, BLACK, ROOK_LEFT);
        BlackPieces[1] = new Knight(7, 1, BLACK, KNIGHT_LEFT);
        BlackPieces[2] = new Bishop(7, 2, BLACK, BISHOP_LEFT);
        BlackPieces[3] = new King(7, 3, BLACK, KING);
        BlackPieces[4] = new Queen(7, 4, BLACK, QUEEN);
        BlackPieces[5] = new Bishop(7, 5, BLACK, BISHOP_RIGHT);
        BlackPieces[6] = new Knight(7, 6, BLACK, KNIGHT_RIGHT);
        BlackPieces[7] = new Rook(7, 7, BLACK, ROOK_RIGHT);

        for (int i = 8; i < 16; i++){
            WhitePieces[i] = new Pawn(i / 8, i % 8, WHITE, PAWN_1 + i - 8);
            BlackPieces[i] = new Pawn(7 - (i / 8), i % 8, BLACK, PAWN_1 + i - 8);
        }
        System.out.println(printBoard());
    }

    @Override
    public String toString() {
        return "Board{" +
                "WhitePieces=" + Arrays.toString(WhitePieces) +
                ", BlackPieces=" + Arrays.toString(BlackPieces) +
                '}';
    }

    public String printBoard() {
        StringBuilder builder = new StringBuilder();
        builder.append(' ');
        builder.append(' ');
        for (int i = 0; i < BOARD_SIZE; i++){
            builder.append(i);
            builder.append(' ');
        }
        builder.append('\n');
        for (int i = BOARD_SIZE - 1; i >= 0; i--){
            builder.append(i);
            builder.append('|');
            for (int j = 0; j < BOARD_SIZE; j++){
                boolean found = false;
                Coord testCoord = new Coord(i, j);
                for (int k = 0; k < 16; k++){
                    if (WhitePieces[k].coord.equals(testCoord)){
                        found = true;
                        builder.append(representations.charAt(k));
                        break;
                    }
                    if (BlackPieces[k].coord.equals(testCoord)){
                        found = true;
                        builder.append(Character.toString(representations.charAt(k)).toLowerCase(Locale.ROOT));
                        break;
                    }
                }
                if (!found){
                    builder.append(' ');
                }
                builder.append('|');
            }
            builder.append(i);
            builder.append('\n');
        }
        builder.append(' ');
        builder.append(' ');
        for (int i = 0; i < BOARD_SIZE; i++){
            builder.append(i);
            builder.append(' ');
        }
        builder.append('\n');
        return builder.toString();
    }

    public ChessPiece findPiece(Coord loc) {
        return Arrays.stream(WhitePieces)
                .filter(chessPiece -> chessPiece.coord.equals(loc))
                .findFirst()
                .orElse(
                        Arrays.stream(BlackPieces)
                                .filter(chessPiece -> chessPiece.coord.equals(loc))
                                .findFirst()
                                .orElse(null)
                );
    }

    public boolean movePiece(Coord oldLoc, Coord newLoc){
        ChessPiece targetPiece;
        targetPiece = findPiece(oldLoc);
        if (targetPiece == null){
            System.out.println("We are unable to find anything");
            return false;
        }
        return movePiece(targetPiece, newLoc);

    }

    public boolean isChecked(boolean color){
        var targetPieces = WhitePieces;
        if (color == WHITE)
            targetPieces = BlackPieces;
        for (var piece:
             targetPieces) {
            if (piece.canCheck(this)){
                return true;
            }
        }
        return false;
    }

    public boolean movePiece(ChessPiece targetPiece, Coord newLoc){
        if (validateMove(targetPiece, newLoc)){
            var piece = findPiece(newLoc);
            if (piece != null){
                piece.coord = EATEN;
            }
            gameHistory.add(targetPiece, newLoc);
            targetPiece.coord = newLoc;
            if (isChecked(targetPiece.color)){
                gameHistory.undo();
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean validateMove(ChessPiece targetPiece, Coord newLoc) {
        System.out.println(targetPiece.getClass());
        System.out.println("Got to the validating point for " + targetPiece.coord);
        System.out.println(Arrays.toString(targetPiece.generateMoves(this)));
        return Arrays.asList(targetPiece.generateMoves(this)).contains(newLoc);
    }

    public static void main(String[] args){
        Board board = new Board();
        //TODO: implement turn
        while (true){
            System.out.println(board.printBoard());
            var hi = new Scanner(System.in);
            int ox = hi.nextInt();
            int oy = hi.nextInt();
            int nx = hi.nextInt();
            int ny = hi.nextInt();
            board.movePiece(new Coord(ox, oy), new Coord(nx, ny));
        }

    }
}

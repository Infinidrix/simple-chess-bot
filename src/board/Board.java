package board;

import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;

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
    static String representations = "RHBKQBHRPPPPPPPP";

    static final int BOARD_SIZE = 8;
    static final boolean WHITE = true;
    static final boolean BLACK = false;
    static final Coord EATEN = new Coord(-100, -100);

    ChessPiece[] WhitePieces, BlackPieces;


    public Board() {
        WhitePieces = new ChessPiece[16];
        BlackPieces = new ChessPiece[16];

        WhitePieces[0] = new Rook(0, 0, WHITE);
        WhitePieces[1] = new Knight(0, 1, WHITE);
        WhitePieces[2] = new Bishop(0, 2, WHITE);
        WhitePieces[3] = new Queen(0, 3, WHITE);
        WhitePieces[4] = new King(0, 4, WHITE);
        WhitePieces[5] = new Bishop(0, 5, WHITE);
        WhitePieces[6] = new Knight(0, 6, WHITE);
        WhitePieces[7] = new Rook(0, 7, WHITE);

        BlackPieces[0] = new Rook(7, 0, BLACK);
        BlackPieces[1] = new Knight(7, 1, BLACK);
        BlackPieces[2] = new Bishop(7, 2, BLACK);
        BlackPieces[3] = new Queen(7, 3, BLACK);
        BlackPieces[4] = new King(7, 4, BLACK);
        BlackPieces[5] = new Bishop(7, 5, BLACK);
        BlackPieces[6] = new Knight(7, 6, BLACK);
        BlackPieces[7] = new Rook(7, 7, BLACK);

        for (int i = 8; i < 16; i++){
            WhitePieces[i] = new Pawn(i / 8, i % 8, WHITE);
            BlackPieces[i] = new Pawn(7 - (i / 8), i % 8, BLACK);
        }
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
        for (int i = BOARD_SIZE - 1; i >= 0; i--){
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
            builder.append('\n');
        }
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

    public boolean movePiece(ChessPiece targetPiece, Coord newLoc){
        if (validateMove(targetPiece, newLoc)){
            var piece = findPiece(newLoc);
            if (piece != null){
                piece.coord = EATEN;
            }
            targetPiece.coord = newLoc;
            return true;
        }
        return false;
    }

    private boolean validateMove(ChessPiece targetPiece, Coord newLoc) {
        System.out.println("Got to the validating point for " + targetPiece.coord);
        System.out.println(Arrays.toString(targetPiece.generateMoves(this)));
        return Arrays.asList(targetPiece.generateMoves(this)).contains(newLoc);
    }

    public static void main(String[] args){
        Board board = new Board();
        System.out.println(board);
        System.out.println(board.movePiece(new Coord(1, 1), new Coord(2, 1)));
        System.out.println();
        System.out.println(board.printBoard());
        System.out.println(board.movePiece(new Coord(6, 2), new Coord(4, 2)));
        System.out.println();
        System.out.println(board.printBoard());
        System.out.println(board.movePiece(new Coord(2, 1), new Coord(3, 1)));
        System.out.println();
        System.out.println(board.printBoard());
        System.out.println(board.movePiece(new Coord(4, 2), new Coord(3, 1)));
        System.out.println();
        System.out.println(board.printBoard());
        System.out.println(board.movePiece(new Coord(3, 1), new Coord(2, 1)));
        System.out.println();
        System.out.println(board.printBoard());
        System.out.println(Arrays.toString(board.findPiece(new Coord(0, 1)).generateMoves(board)));
        System.out.println(board.movePiece(new Coord(0, 1), new Coord(2, 2)));
        System.out.println();
        System.out.println(board.printBoard());
        System.out.println(board.movePiece(new Coord(2, 2), new Coord(4, 3)));
        System.out.println();
        System.out.println(board.printBoard());
        System.out.println(Arrays.toString(board.findPiece(new Coord(4, 3)).generateMoves(board)));
        System.out.println(Arrays.toString(board.findPiece(new Coord(0, 2)).generateMoves(board)));
        System.out.println(Arrays.toString(board.findPiece(new Coord(0, 0)).generateMoves(board)));
    }
}

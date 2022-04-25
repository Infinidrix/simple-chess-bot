package board;

import java.io.Console;
import java.util.*;

public class Board implements Cloneable {

    // TODO: Add state representation
    // TODO: Add board generation from state representation
    // TODO: Add promotion
    // TODO: Add print state method
    // TODO: Fix update game status method

    public static final int ROOK_LEFT = 0;
    public static final int KNIGHT_LEFT = 1;
    public static final int BISHOP_LEFT = 2;
    public static final int QUEEN = 3;
    public static final int KING = 4;
    public static final int BISHOP_RIGHT = 5;
    public static final int KNIGHT_RIGHT = 6;
    public static final int ROOK_RIGHT = 7;
    public static final int PAWN_1 = 8;
    public static final int PAWN_2 = 9;
    public static final int PAWN_3 = 10;
    public static final int PAWN_4 = 11;
    public static final int PAWN_5 = 12;
    public static final int PAWN_6 = 13;
    public static final int PAWN_7 = 14;
    public static final int PAWN_8 = 15;
    public static String representations = "RHBQKBHRPPPPPPPP";

    static final int BOARD_SIZE = 8;
    public static final boolean WHITE = true;
    public static final boolean BLACK = false;
    static final Coord EATEN = new Coord(-100, -100);

    public ChessPiece[] WhitePieces, BlackPieces;

    public MoveHistory getLastMove() {
        if (gameHistory.history.empty())
            return null;
        return gameHistory.history.peek();
    }

    GameHistory gameHistory;
    public int turn;

    public enum Status {
        ONGOING,
        WHITE_WIN,
        BLACK_WIN,
        DRAW
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    Status currentStatus;


    public Board() {
        WhitePieces = new ChessPiece[16];
        BlackPieces = new ChessPiece[16];
        gameHistory = new GameHistory();
        turn = 0;
        currentStatus = Status.ONGOING;

        WhitePieces[0] = new Rook(0, 0, WHITE, ROOK_LEFT);
        WhitePieces[1] = new Knight(0, 1, WHITE, KNIGHT_LEFT);
        WhitePieces[2] = new Bishop(0, 2, WHITE, BISHOP_LEFT);
        WhitePieces[3] = new Queen(0, 3, WHITE, QUEEN);
        WhitePieces[4] = new King(0, 4, WHITE, KING);
        WhitePieces[5] = new Bishop(0, 5, WHITE, BISHOP_RIGHT);
        WhitePieces[6] = new Knight(0, 6, WHITE, KNIGHT_RIGHT);
        WhitePieces[7] = new Rook(0, 7, WHITE, ROOK_RIGHT);

        BlackPieces[0] = new Rook(7, 0, BLACK, ROOK_LEFT);
        BlackPieces[1] = new Knight(7, 1, BLACK, KNIGHT_LEFT);
        BlackPieces[2] = new Bishop(7, 2, BLACK, BISHOP_LEFT);
        BlackPieces[3] = new Queen(7, 3, BLACK, QUEEN);
        BlackPieces[4] = new King(7, 4, BLACK, KING);
        BlackPieces[5] = new Bishop(7, 5, BLACK, BISHOP_RIGHT);
        BlackPieces[6] = new Knight(7, 6, BLACK, KNIGHT_RIGHT);
        BlackPieces[7] = new Rook(7, 7, BLACK, ROOK_RIGHT);

        for (int i = 8; i < 16; i++){
            WhitePieces[i] = new Pawn(i / 8, i % 8, WHITE, PAWN_1 + i - 8);
            BlackPieces[i] = new Pawn(7 - (i / 8), i % 8, BLACK, PAWN_1 + i - 8);
        }
    }

    public ArrayList<MoveHistory> getPossibleMoves(boolean color) {
        var pieces = this.BlackPieces;
        if (color == WHITE){
            pieces = this.WhitePieces;
        }
        ArrayList<MoveHistory> possibleMoves = new ArrayList<>();
        for (var piece : pieces) {
            for (var move : piece.generateMovesValid(this)){
                possibleMoves.add(new MoveHistory(piece.coord, move, piece, this.findPiece(move)));
            }
        }
        return possibleMoves;
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

    public void updateGameStatus(){
        if (getPossibleMoves(WHITE).size() == 0){
            if (isChecked(WHITE)){
                currentStatus = Status.BLACK_WIN;
            } else {
                currentStatus = Status.DRAW;
            }
        } else if (getPossibleMoves(BLACK).size() == 0){
            if (isChecked(BLACK)){
                currentStatus = Status.WHITE_WIN;
            } else {
                currentStatus = Status.DRAW;
            }
        }
    }

    public boolean movePiece(ChessPiece targetPiece, Coord newLoc){
        if (validateMove(targetPiece, newLoc)){
            var piece = findPiece(newLoc);
            if (piece != null){
                piece.coord = EATEN;
            }
            gameHistory.add(targetPiece, newLoc, piece);
            targetPiece.coord = newLoc;
            if (isChecked(targetPiece.color)){
                gameHistory.undo();
                return false;
            }
            turn += 1;
            return true;
        }
        return false;
    }

    public void undo(){
        turn -= 1;
        gameHistory.undo();
    }

    private boolean validateMove(ChessPiece targetPiece, Coord newLoc) {
        var correctColor = (turn % 2 == 0) ? WHITE : BLACK;
        if (targetPiece.color != correctColor)
            return false;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Arrays.equals(WhitePieces, board.WhitePieces) && Arrays.equals(BlackPieces, board.BlackPieces);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(WhitePieces);
        result = 31 * result + Arrays.hashCode(BlackPieces);
        return result;
    }

    @Override
    public Board clone() {
        try {
            Board clone = (Board) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            clone.WhitePieces = this.WhitePieces.clone();
            clone.BlackPieces = this.BlackPieces.clone();

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

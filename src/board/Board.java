package board;

import javax.swing.text.AttributeSet;
import java.io.Console;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.copyOf;

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
        this("rhbqkbhrpppppppp32PPPPPPPPRHBQKBHR");
    }
    public Board(String startingBoard) {

        WhitePieces = new ChessPiece[16];
        BlackPieces = new ChessPiece[16];
        gameHistory = new GameHistory();
        turn = 0;
        currentStatus = Status.ONGOING;
        Map<Character, Queue<Integer>> whiteCharToConst = Map.of(
                'R', new LinkedList<>(List.of(ROOK_LEFT, ROOK_RIGHT)),
                'H', new LinkedList<>(List.of(KNIGHT_LEFT, KNIGHT_RIGHT)),
                'B', new LinkedList<>(List.of(BISHOP_LEFT, BISHOP_RIGHT)),
                'Q', new LinkedList<>(List.of(QUEEN)),
                'K', new LinkedList<>(List.of(KING)),
                'P', new LinkedList<>(List.of(PAWN_1, PAWN_2, PAWN_3, PAWN_4, PAWN_5, PAWN_6, PAWN_7, PAWN_8))
        );
        Map<Character, Queue<Integer>> blackCharToConst = whiteCharToConst.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new LinkedList<Integer>(e.getValue())));
        int space = 0;
        int currentLoc = 0;

        for (int i = 0; i < startingBoard.length(); i++){
            char c = startingBoard.charAt(i);
            if (Character.isDigit(c)){
                space = space * 10 + Character.getNumericValue(c);
            } else {
                if (space != 0) {
                    currentLoc += space;
                    space = 0;
                }
                var currPiece = WhitePieces;
                var currMap = whiteCharToConst;
                var isWhite = true;
                if (Character.toLowerCase(c) == c){
                    currPiece = BlackPieces;
                    currMap = blackCharToConst;
                    isWhite = false;
                }
                c = Character.toUpperCase(c);
                var index = currMap.get(c).poll();
                int x = 7 - (currentLoc / 8), y = currentLoc % 8;
                switch (c){
                    case 'K':
                        currPiece[index] = new King(x, y, isWhite ? WHITE : BLACK, index);
                        break;
                    case 'Q':
                        currPiece[index] = new Queen(x, y, isWhite ? WHITE : BLACK, index);
                        break;
                    case 'B':
                        currPiece[index] = new Bishop(x, y, isWhite ? WHITE : BLACK, index);
                        break;
                    case 'H':
                        currPiece[index] = new Knight(x, y, isWhite ? WHITE : BLACK, index);
                        break;
                    case 'R':
                        currPiece[index] = new Rook(x, y, isWhite ? WHITE : BLACK, index);
                        break;
                    case 'P':
                        currPiece[index] = new Pawn(x, y, isWhite ? WHITE : BLACK, index);
                        break;
                }
                currentLoc += 1;
            }
        }
        for (var p : whiteCharToConst.entrySet()){
            while (!p.getValue().isEmpty()){
                var index = p.getValue().poll();
                var c = p.getKey();
                var currPiece = WhitePieces;
                int x = -1, y = -1;
                switch (c){
                    case 'K':
                        currPiece[index] = new King(x, y, WHITE, index);
                        break;
                    case 'Q':
                        currPiece[index] = new Queen(x, y, WHITE, index);
                        break;
                    case 'B':
                        currPiece[index] = new Bishop(x, y, WHITE, index);
                        break;
                    case 'H':
                        currPiece[index] = new Knight(x, y, WHITE, index);
                        break;
                    case 'R':
                        currPiece[index] = new Rook(x, y, WHITE, index);
                        break;
                    case 'P':
                        currPiece[index] = new Pawn(x, y, WHITE, index);
                        break;
                }
            }
        }

        for (var p : blackCharToConst.entrySet()){
            while (!p.getValue().isEmpty()){
                var index = p.getValue().poll();
                var c = p.getKey();
                var currPiece = BlackPieces;
                int x = -1, y = -1;
                switch (c) {
                    case 'K':
                        currPiece[index] = new King(x, y, BLACK, index);
                        break;
                    case 'Q':
                        currPiece[index] = new Queen(x, y, BLACK, index);
                        break;
                    case 'B':
                        currPiece[index] = new Bishop(x, y, BLACK, index);
                        break;
                    case 'H':
                        currPiece[index] = new Knight(x, y, BLACK, index);
                        break;
                    case 'R':
                        currPiece[index] = new Rook(x, y, BLACK, index);
                        break;
                    case 'P':
                        currPiece[index] = new Pawn(x, y, BLACK, index);
                        break;
                }
            }
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

    public String getBoardRepresentation(){
        int space = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = BOARD_SIZE - 1; i >= 0; i--){
            for (int j = 0; j < BOARD_SIZE; j++){
                var piece = this.findPiece(new Coord(i, j));
                if (piece == null){
                    space += 1;
                    continue;
                }
                if (space > 0) {
                    stringBuilder.append(space);
                    space = 0;
                }
                var rep = representations.charAt(piece.type);
                if (piece.color != WHITE){
                    rep = Character.toLowerCase(rep);
                }
                stringBuilder.append(rep);
            }
        }
        return stringBuilder.toString();
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

        if (turn % 2 == 0 && getPossibleMoves(WHITE).size() == 0){
            if (isChecked(WHITE)){
                currentStatus = Status.BLACK_WIN;
            } else {
                currentStatus = Status.DRAW;
            }
        } else if (turn % 2 == 1 && getPossibleMoves(BLACK).size() == 0){
            if (isChecked(BLACK)){
                currentStatus = Status.WHITE_WIN;
            } else {
                currentStatus = Status.DRAW;
            }
        } else {
            currentStatus = Status.ONGOING;
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

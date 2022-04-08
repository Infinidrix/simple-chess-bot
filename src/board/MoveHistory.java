package board;

public class MoveHistory {
    Coord oldLoc, newLoc;

    ChessPiece piece;
    ChessPiece eatenPiece;
    public Coord getOldLoc() {
        return oldLoc;
    }

    public Coord getNewLoc() {
        return newLoc;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    @Override
    public String toString() {
        return "MoveHistory{" +
                "oldLoc=" + oldLoc +
                ", newLoc=" + newLoc +
                ", piece=" + piece +
                '}';
    }

    public MoveHistory(Coord oldLoc, Coord newLoc, ChessPiece piece, ChessPiece eatenPiece) {
        this.oldLoc = oldLoc;
        this.newLoc = newLoc;
        this.piece = piece;
        this.eatenPiece = eatenPiece;
    }
}

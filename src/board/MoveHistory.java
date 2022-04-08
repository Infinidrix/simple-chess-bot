package board;

public class MoveHistory {
    Coord oldLoc, newLoc;
    ChessPiece piece;

    @Override
    public String toString() {
        return "MoveHistory{" +
                "oldLoc=" + oldLoc +
                ", newLoc=" + newLoc +
                ", piece=" + piece +
                '}';
    }

    public MoveHistory(Coord oldLoc, Coord newLoc, ChessPiece piece) {
        this.oldLoc = oldLoc;
        this.newLoc = newLoc;
        this.piece = piece;
    }
}

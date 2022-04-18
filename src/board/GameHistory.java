package board;

import java.util.Stack;

public class GameHistory implements Cloneable {
    Stack<MoveHistory> history;

    public GameHistory() {
        this.history = new Stack<>();
    }

    public void add(ChessPiece piece, Coord newCoord, ChessPiece eatenPiece){
        history.add(new MoveHistory(piece.coord, newCoord, piece, eatenPiece));
    }

    @Override
    public String toString() {
        return "GameHistory{" +
                "history=\n\t" + history +
                '}';
    }

    public void undo(){
        if (history.size() == 0)
            return;
        MoveHistory lastMove = history.pop();
        lastMove.piece.coord = lastMove.oldLoc;
        if (lastMove.eatenPiece != null)
            lastMove.eatenPiece.coord = lastMove.newLoc;
    }

    @Override
    public GameHistory clone() {
        try {
            GameHistory clone = (GameHistory) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            clone.history = new Stack<>();
            clone.history.addAll(this.history);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

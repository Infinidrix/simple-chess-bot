package board;

import java.util.Stack;

public class GameHistory {
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
}

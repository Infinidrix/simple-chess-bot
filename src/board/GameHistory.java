package board;

import java.util.Stack;

public class GameHistory {
    Stack<MoveHistory> history;

    public GameHistory() {
        this.history = new Stack<>();
    }

    public void add(ChessPiece piece, Coord oldLoc){
        history.add(new MoveHistory(oldLoc, piece.coord, piece));
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
        lastMove.piece.coord = lastMove.newLoc;
    }
}

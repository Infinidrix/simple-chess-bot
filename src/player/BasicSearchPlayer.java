package player;

import board.Board;
import board.MoveHistory;
import gameUI.MainUI;
import player.heuristics.CurrentBoardStrength;

import java.util.AbstractMap.SimpleEntry;
import java.util.Random;

import static board.Board.BLACK;
import static board.Board.WHITE;

public class BasicSearchPlayer extends Player{
    int maxDepth, turnaround;

    BasicSearchPlayer(boolean color, int maxDepth, int turnaround){
        super(color);
        this.maxDepth = maxDepth;
        this.turnaround = turnaround;
    }


    @Override
    public void makeMove(Board board) {
        SimpleEntry<MoveHistory, Integer> bestMove;
        if (board.turn > 100) {
            bestMove = searchMove(board, color, maxDepth + 2, Integer.MIN_VALUE + 10, Integer.MAX_VALUE - 10);
        } else {
            bestMove = searchMove(board, color, maxDepth, Integer.MIN_VALUE + 10, Integer.MAX_VALUE - 10);
        }
        System.out.println("Final "+bestMove.getValue());
        board.movePiece(bestMove.getKey().getPiece(), bestMove.getKey().getNewLoc());
    }

    private SimpleEntry<MoveHistory, Integer> searchMove(Board board, boolean color, int currDepth, int alpha, int beta) {
        if (currDepth == 0){
            if (board.turn < this.turnaround)
                return new SimpleEntry<>(null, CurrentBoardStrength.pieceStrengths(board, color));
            else
                return new SimpleEntry<>(null, CurrentBoardStrength.pieceStrengths(board, color) + (100 * CurrentBoardStrength.winFactor(board, color)));
        }
        SimpleEntry<MoveHistory, Integer> bestMove = null;
        var random = new Random();
        for (var move : board.getPossibleMoves(color)){
            board.movePiece(move.getPiece(), move.getNewLoc());
            var currMove = searchMove(board, !color, currDepth - 1, -beta, -alpha);
            board.undo();
            if (currMove == null) {
                continue;
            }
            if (Math.abs(currMove.getValue()) > 10000){
                System.out.println(currMove.getValue());
            }
            currMove.setValue(-currMove.getValue());
            if (currMove.getValue() >= beta){
                return new SimpleEntry<>(move, currMove.getValue());
            }
            if (bestMove == null || bestMove.getValue() < currMove.getValue() || (bestMove.getValue().equals(currMove.getValue()) && random.nextInt(10) > 7)){
                bestMove = new SimpleEntry<>(move, currMove.getValue());
            }
            alpha = Math.max(alpha, currMove.getValue());
        }
        return bestMove;
    }

    public static void main(String[] args) {
        new MainUI(new BasicSearchPlayer(BLACK, 3, 0), new Board());
    }

}

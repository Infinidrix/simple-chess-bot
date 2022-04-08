package gameUI;

import board.Board;
import board.Coord;
import player.Player;

import java.awt.event.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

import static board.Board.BLACK;
import static board.Board.WHITE;

public class MainUI extends JFrame{
    JButton[] ChessBoard;
    Coord PrevCoord;
    Set<Coord> PotentialCoords;
    Board board;
    Player whitePlayer, blackPlayer;
    boolean isWhitesTurn;

    MainUI(Player whitePlayer, Player blackPlayer){
        super("Chess Bot");
        PotentialCoords = new HashSet<>();
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        isWhitesTurn = true;
    }

    MainUI() {
        this((Player) null, (Player) null);
    }

    MainUI(Player player){
        this((Player) null, player);
    }

    public void draw(Board board){
        this.board = board;
        ChessBoard = new JButton[64];
        for (int i = 0; i < ChessBoard.length; i++){
            ChessBoard[i] = new JButton();
            ChessBoard[i].setBackground(Color.GRAY);
            var x = i / 8;
            var y = i % 8;
            ChessBoard[i].addActionListener(e -> movePieceUI(e, board, 7-y, x));
            var piece = board.findPiece(new Coord(7-y, x));
            if (piece != null){
                ChessBoard[i].setText(Character.toString(Board.representations.charAt(piece.type)));
                if (piece.color){
                    ChessBoard[i].setForeground(Color.white);
                } else {
                    ChessBoard[i].setForeground(Color.BLACK);
                }
            }
            ChessBoard[i].setBounds(10 + (50 * (i / 8)), 10 + (50 * (i % 8)), 50, 50);
            add(ChessBoard[i]);
        }
        setLayout(null);
        setSize(440,460);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void redraw(){
        for (int i = 0; i < ChessBoard.length; i++){
            ChessBoard[i].setBackground(Color.GRAY);
            var x = i / 8;
            var y = i % 8;
            var currCoord = new Coord(7-y, x);
            var piece = board.findPiece(currCoord);
            if (PrevCoord != null && PrevCoord.equals(currCoord)){
                ChessBoard[i].setBackground(Color.green);
            }
            if (PotentialCoords.contains(currCoord)){
                ChessBoard[i].setBackground(Color.yellow);
            }
            if (piece != null){
                ChessBoard[i].setText(Character.toString(Board.representations.charAt(piece.type)));
                if (piece.color){
                    ChessBoard[i].setForeground(Color.white);
                } else {
                    ChessBoard[i].setForeground(Color.black);
                }
            } else {
                ChessBoard[i].setText("");
            }
        }
        repaint();
    }

    private void movePieceUI(ActionEvent e, Board board, int x, int y) {
        System.out.println(PrevCoord);
        var correctColor = (isWhitesTurn) ? WHITE : BLACK;
        if (PrevCoord == null) {
            PrevCoord = new Coord(x, y);
            var piece = board.findPiece(PrevCoord);
            if (piece != null) {
                PotentialCoords.clear();
                if (piece.color == correctColor)
                    PotentialCoords.addAll(java.util.List.of(piece.generateMovesValid(board)));
            }
        }
        else {
            var currentPiece = board.findPiece(PrevCoord);
            if (currentPiece == null || currentPiece.color != correctColor){
                PrevCoord = new Coord(x, y);
                var piece = board.findPiece(PrevCoord);
                PotentialCoords.clear();
                if (piece != null && piece.color == correctColor) {
                    PotentialCoords.addAll(java.util.List.of(piece.generateMovesValid(board)));
                }
            }
            else if (!board.movePiece(PrevCoord, new Coord(x, y))) {
                PrevCoord = new Coord(x, y);
                var piece = board.findPiece(PrevCoord);
                PotentialCoords.clear();
                if (piece != null && piece.color == correctColor) {
                    PotentialCoords.addAll(java.util.List.of(piece.generateMovesValid(board)));
                }
                System.out.println("Can't move");
            } else {
                isWhitesTurn = !isWhitesTurn;
                System.out.println(board.printBoard());
                PrevCoord = null;
                PotentialCoords.clear();
            }
        }
        redraw();
    }

    public static void main(String[] args) {
        var hi =  new MainUI();
        hi.draw(new Board());
    }
}

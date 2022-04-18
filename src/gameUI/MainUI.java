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
    // TODO: Make board checkered
    // TODO: Make pieces icons
    // TODO: Make move visible before bot thinks
    // TODO: Show Previous Move
    JButton[] ChessBoard;
    Coord PrevCoord;
    Set<Coord> PotentialCoords;
    Board board;
    Player whitePlayer, blackPlayer;
    boolean isWhitesTurn, keepPlaying;

    public MainUI(Player whitePlayer, Player blackPlayer, Board board){
        super("Chess Bot");
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.board = board;
        PotentialCoords = new HashSet<>();
        isWhitesTurn = true;
        keepPlaying = true;
        draw();
        if (whitePlayer != null && blackPlayer != null){
            int timeout = 10;
            int maxMoves = 1000;
            for (int i = 0; i < maxMoves; i++){
                while (!keepPlaying) {
                    try {
                        Thread.sleep(timeout);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                prepareNextMove();
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public MainUI(Board board) {
        this(null, null, board);
    }

    public MainUI(Player player, Board board){
        this(null, player, board);
    }

    public void draw(){
        ChessBoard = new JButton[64];
        for (int i = 0; i < ChessBoard.length; i++){
            ChessBoard[i] = new JButton();
            ChessBoard[i].setBackground(Color.GRAY);
            var x = i / 8;
            var y = i % 8;
            ChessBoard[i].addActionListener(e -> movePieceUI(board, 7-y, x));
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
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("Listening to " + e.getKeyChar());
                if (e.getKeyChar() == 'p'){
                    keepPlaying = !keepPlaying;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Closely Listening to " + e.getKeyChar());
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
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

    private void movePieceUI(Board board, int x, int y) {
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
            } else {
                isWhitesTurn = !isWhitesTurn;
                PrevCoord = null;
                PotentialCoords.clear();
            }
        }
        redraw();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        prepareNextMove();
    }

    private void prepareNextMove() {
        if (isWhitesTurn && whitePlayer != null) {
            isWhitesTurn = false;
            whitePlayer.makeMove(board);
            redraw();
        } else if (!isWhitesTurn && blackPlayer != null) {
            isWhitesTurn = true;
            blackPlayer.makeMove(board);
            redraw();
        }
    }

    public static void main(String[] args) {
        new MainUI(new Board());
    }
}

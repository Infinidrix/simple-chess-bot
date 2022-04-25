package gameUI;

import board.Board;
import board.Coord;
import player.Player;

import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;

import static board.Board.BLACK;
import static board.Board.WHITE;
import static java.util.Map.entry;

public class MainUI extends JFrame{
    // TODO: Make move visible before bot thinks
    // TODO: Add (key-binding) controls
    JButton[] ChessBoard;
    Coord PrevCoord;
    Set<Coord> PotentialCoords;
    Board board;
    Player whitePlayer, blackPlayer;
    boolean isWhitesTurn, keepPlaying;
    Map<Character, ImageIcon> iconMap;


    public MainUI(Player whitePlayer, Player blackPlayer, Board board){
        super("Chess Bot");
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.board = board;
        PotentialCoords = new HashSet<>();
        iconMap = Map.ofEntries(
                entry('K', new ImageIcon(
                        getScaledImage(
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/pieces/KW.png"))).getImage(), 49, 49
                        ))),
                entry('k', new ImageIcon(
                        getScaledImage(
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/pieces/K.png"))).getImage(), 49, 49
                        ))),
                entry('Q', new ImageIcon(
                        getScaledImage(
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/pieces/QW.png"))).getImage(), 49, 49
                        ))),
                entry('q', new ImageIcon(
                        getScaledImage(
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/pieces/Q.png"))).getImage(), 49, 49
                        ))),
                entry('B', new ImageIcon(
                        getScaledImage(
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/pieces/BW.png"))).getImage(), 49, 49
                        ))),
                entry('b', new ImageIcon(
                        getScaledImage(
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/pieces/B.png"))).getImage(), 49, 49
                        ))),
                entry('H', new ImageIcon(
                        getScaledImage(
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/pieces/HW.png"))).getImage(), 49, 49
                        ))),
                entry('h', new ImageIcon(
                        getScaledImage(
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/pieces/H.png"))).getImage(), 49, 49
                        ))),
                entry('R', new ImageIcon(
                        getScaledImage(
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/pieces/RW.png"))).getImage(), 49, 49
                        ))),
                entry('r', new ImageIcon(
                        getScaledImage(
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/pieces/R.png"))).getImage(), 49, 49
                        ))),
                entry('P', new ImageIcon(
                        getScaledImage(
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/pieces/PW.png"))).getImage(), 49, 49
                        ))),
                entry('p', new ImageIcon(
                        getScaledImage(
                                new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/pieces/P.png"))).getImage(), 49, 49
                        )))
        );
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
            var x = i / 8;
            var y = i % 8;
            if ((x + y) % 2 == 0){
                ChessBoard[i].setBackground(Color.LIGHT_GRAY);
            } else {
                ChessBoard[i].setBackground(Color.DARK_GRAY);
            }
            ChessBoard[i].addActionListener(e -> movePieceUI(board, 7-y, x));
            var piece = board.findPiece(new Coord(7-y, x));
            if (piece != null){
                var charRep = Board.representations.charAt(piece.type);
                if (piece.color == BLACK){
                    charRep = Character.toLowerCase(charRep);
                }
                ChessBoard[i].setIcon(iconMap.get(charRep));
            }
            ChessBoard[i].setBounds(10 + (50 * (i / 8)), 10 + (50 * (i % 8)), 50, 50);
            add(ChessBoard[i]);
        }
        setLayout(null);
        setSize(450,460);
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
        redraw();
    }

    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public void redraw(){
        var prevMove = board.getLastMove();
        for (int i = 0; i < ChessBoard.length; i++){
            var x = i / 8;
            var y = i % 8;
            if ((x + y) % 2 == 0){
                ChessBoard[i].setBackground(Color.LIGHT_GRAY);
            } else {
                ChessBoard[i].setBackground(Color.DARK_GRAY);
            }
            var currCoord = new Coord(7-y, x);
            var piece = board.findPiece(currCoord);
            if (PrevCoord != null && PrevCoord.equals(currCoord)){
                ChessBoard[i].setBackground(Color.green);
            }
            if (PotentialCoords.contains(currCoord)){
                ChessBoard[i].setBackground(Color.yellow);
            } else if (prevMove != null && (currCoord.equals(prevMove.getNewLoc()) || currCoord.equals(prevMove.getOldLoc()))){
                ChessBoard[i].setBackground(Color.cyan);
            }
            if (piece != null){
                var charRep = Board.representations.charAt(piece.type);
                if (piece.color == BLACK){
                    charRep = Character.toLowerCase(charRep);
                }
                ChessBoard[i].setIcon(iconMap.get(charRep));
            } else {
                ChessBoard[i].setIcon(null);
            }
            ChessBoard[i].repaint(1);
        }
        repaint(1);
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
        repaint();
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

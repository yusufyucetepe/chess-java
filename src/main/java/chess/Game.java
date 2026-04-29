package chess;

import javax.swing.JFrame;

public class Game extends JFrame {
    public Game() {
        super("Chess Game");
        ChessBoard chessBoard = new ChessBoard();
        BoardPanel boardPanel = new BoardPanel(chessBoard);
        add(boardPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }
}

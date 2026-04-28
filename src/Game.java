import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    private ChessBoard board;
    private BoardPanel boardPanel;
    
    public Game() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Initialize game components
        board = new ChessBoard();
        boardPanel = new BoardPanel(board);
        
        add(boardPanel);
        pack();
        setLocationRelativeTo(null);
    }
}
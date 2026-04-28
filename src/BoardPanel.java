import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {
    private static final int SQUARE_SIZE = 80;
    private static final Color LIGHT_SQUARE = new Color(238, 238, 210);
    private static final Color DARK_SQUARE = new Color(118, 150, 86);
    private static final Color SELECTED_COLOR = new Color(186, 202, 68, 128);
    
    private ChessBoard board;
    
    public BoardPanel(ChessBoard board) {
        this.board = board;
        setPreferredSize(new Dimension(SQUARE_SIZE * 8, SQUARE_SIZE * 8));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / SQUARE_SIZE;
                int col = e.getX() / SQUARE_SIZE;
                board.handleClick(row, col);
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawBoard(g2d);
        drawHighlight(g2d);
        drawPieces(g2d);
    }
    
    private void drawBoard(Graphics2D g) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Color color = (row + col) % 2 == 0 ? LIGHT_SQUARE : DARK_SQUARE;
                g.setColor(color);
                g.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, 
                          SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
    
    private void drawHighlight(Graphics2D g) {
        int selectedRow = board.getSelectedRow();
        int selectedCol = board.getSelectedCol();
        
        if (selectedRow != -1 && selectedCol != -1) {
            g.setColor(SELECTED_COLOR);
            g.fillRect(selectedCol * SQUARE_SIZE, selectedRow * SQUARE_SIZE, 
                      SQUARE_SIZE, SQUARE_SIZE);
        }
    }
    
    private void drawPieces(Graphics2D g) {
        Font font = new Font("Sans-Serif", Font.PLAIN, 60);
        g.setFont(font);
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.getPiece(row, col);
                if (piece != null) {
                    g.setColor(piece.isWhite() ? Color.WHITE : Color.BLACK);
                    
                    String symbol = piece.getSymbol();
                    FontMetrics fm = g.getFontMetrics();
                    int x = col * SQUARE_SIZE + (SQUARE_SIZE - fm.stringWidth(symbol)) / 2;
                    int y = row * SQUARE_SIZE + (SQUARE_SIZE + fm.getAscent()) / 2 - 5;
                    
                    g.drawString(symbol, x, y);
                }
            }
        }
    }
}
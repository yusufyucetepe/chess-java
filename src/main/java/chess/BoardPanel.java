package chess;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;

public class BoardPanel extends JPanel {
    private static final int SQUARE_SIZE = 80;
    private static final Color LIGHT_SQUARE = new Color(238, 238, 210);
    private static final Color DARK_SQUARE = new Color(118, 150, 86);
    private static final Color SELECTED_COLOR = new Color(186, 202, 68, 128);
    private static final Color VALID_MOVE_COLOR = new Color(130, 151, 105);
    private static final Color STATUS_BG = new Color(220, 20, 20);
    
    private ChessBoard chessBoard;
    
    // Piece symbols mapping
    private static final String[][] PIECE_SYMBOLS = {
        {"♔", "♕", "♖", "♗", "♘", "♙"},  // White pieces
        {"♚", "♛", "♜", "♝", "♞", "♟"}   // Black pieces
    };
    
    public BoardPanel(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
        setPreferredSize(new Dimension(SQUARE_SIZE * 8, SQUARE_SIZE * 8 + 30));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / SQUARE_SIZE;
                int col = e.getX() / SQUARE_SIZE;
                if (row < 8 && col < 8) {
                    chessBoard.handleClick(row, col);
                    repaint();
                }
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
        drawHighlights(g2d);
        drawPieces(g2d);
        drawStatus(g2d);
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
    
    private void drawHighlights(Graphics2D g) {
        // Highlight selected square
        Square selected = chessBoard.getSelectedSquare();
        if (selected != null) {
            int[] coords = squareToCoords(selected);
            g.setColor(SELECTED_COLOR);
            g.fillRect(coords[1] * SQUARE_SIZE, coords[0] * SQUARE_SIZE, 
                      SQUARE_SIZE, SQUARE_SIZE);
        }
        
        // Draw valid move indicators
        for (Square square : chessBoard.getValidMoves()) {
            int[] coords = squareToCoords(square);
            int centerX = coords[1] * SQUARE_SIZE + SQUARE_SIZE / 2;
            int centerY = coords[0] * SQUARE_SIZE + SQUARE_SIZE / 2;
            
            g.setColor(VALID_MOVE_COLOR);
            g.fillOval(centerX - 12, centerY - 12, 24, 24);
        }
    }
    
    private void drawPieces(Graphics2D g) {
        Font font = new Font("Sans-Serif", Font.PLAIN, 60);
        g.setFont(font);
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = getSquareFromCoords(row, col);
                Piece piece = chessBoard.getBoard().getPiece(square);
                
                if (piece != Piece.NONE) {
                    String symbol = getPieceSymbol(piece);
                    Color color = piece.getPieceSide().name().equals("WHITE") 
                                 ? Color.WHITE : Color.BLACK;
                    
                    g.setColor(color);
                    FontMetrics fm = g.getFontMetrics();
                    int x = col * SQUARE_SIZE + (SQUARE_SIZE - fm.stringWidth(symbol)) / 2;
                    int y = row * SQUARE_SIZE + (SQUARE_SIZE + fm.getAscent()) / 2 - 5;
                    
                    g.drawString(symbol, x, y);
                }
            }
        }
    }
    
    private void drawStatus(Graphics2D g) {
        String status = chessBoard.getGameStatus();
        
        if (status != null) {
            g.setColor(STATUS_BG);
            g.fillRect(0, 0, SQUARE_SIZE * 8, 30);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Sans-Serif", Font.BOLD, 20));
            FontMetrics fm = g.getFontMetrics();
            int x = (SQUARE_SIZE * 8 - fm.stringWidth(status)) / 2;
            g.drawString(status, x, 20);
        }
    }
    
    private String getPieceSymbol(Piece piece) {
        int colorIndex = piece.getPieceSide().name().equals("WHITE") ? 0 : 1;
        
        switch (piece.getPieceType()) {
            case KING: return PIECE_SYMBOLS[colorIndex][0];
            case QUEEN: return PIECE_SYMBOLS[colorIndex][1];
            case ROOK: return PIECE_SYMBOLS[colorIndex][2];
            case BISHOP: return PIECE_SYMBOLS[colorIndex][3];
            case KNIGHT: return PIECE_SYMBOLS[colorIndex][4];
            case PAWN: return PIECE_SYMBOLS[colorIndex][5];
            default: return "";
        }
    }
    
    private Square getSquareFromCoords(int row, int col) {
        int fileIndex = col;
        int rankIndex = 7 - row;
        return Square.squareAt(fileIndex + rankIndex * 8);
    }
    
    private int[] squareToCoords(Square square) {
        int index = square.ordinal();
        int file = index % 8;
        int rank = index / 8;
        int row = 7 - rank;
        int col = file;
        return new int[]{row, col};
    }
}

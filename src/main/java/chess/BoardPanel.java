package chess;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.io.File;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;

public class BoardPanel extends JPanel {
    private static final int SQUARE_SIZE = 80;
    private static final Color LIGHT_SQUARE = new Color(230, 210, 181);
    private static final Color DARK_SQUARE = new Color(128, 100, 63);
    private static final Color SELECTED_COLOR = new Color(186, 202, 68, 128);
    private static final Color VALID_MOVE_COLOR = new Color(144, 131, 112);
    private static final Color STATUS_BG = new Color(255, 77, 6);
    
    private ChessBoard chessBoard;
    private Map<Piece, BufferedImage> pieceImages;
    
    public BoardPanel(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
        this.pieceImages = new HashMap<>();
        loadPieceImages();
        
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
    
    private void loadPieceImages() {
        // Array of pieces to load
        Piece[] piecesToLoad = {
            Piece.WHITE_KING, Piece.WHITE_QUEEN, Piece.WHITE_ROOK,
            Piece.WHITE_BISHOP, Piece.WHITE_KNIGHT, Piece.WHITE_PAWN,
            Piece.BLACK_KING, Piece.BLACK_QUEEN, Piece.BLACK_ROOK,
            Piece.BLACK_BISHOP, Piece.BLACK_KNIGHT, Piece.BLACK_PAWN
        };
        
        for (Piece piece : piecesToLoad) {
            String filename = getImageFilename(piece);
            String path = "assets/" + filename;
            
            try {
                BufferedImage originalImage = ImageIO.read(new File(path));
                // Scale image to fit within square (leave small margin)
                int imageSize = SQUARE_SIZE - 10; // 70x70 in an 80x80 square
                Image scaledImage = originalImage.getScaledInstance(
                    imageSize, imageSize, Image.SCALE_SMOOTH
                );
                
                // Convert back to BufferedImage for better performance
                BufferedImage bufferedScaled = new BufferedImage(
                    imageSize, imageSize, BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g2d = bufferedScaled.createGraphics();
                g2d.drawImage(scaledImage, 0, 0, null);
                g2d.dispose();
                
                pieceImages.put(piece, bufferedScaled);
                
            } catch (Exception e) {
                System.err.println("Error loading image: " + path);
                e.printStackTrace();
            }
        }
    }
    
    private String getImageFilename(Piece piece) {
        String color = piece.getPieceSide().name().toLowerCase(); // "white" or "black"
        String type = piece.getPieceType().name().toLowerCase();  // "king", "queen", etc.
        return color + "_" + type + ".png";
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
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
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = getSquareFromCoords(row, col);
                Piece piece = chessBoard.getBoard().getPiece(square);
                
                if (piece != Piece.NONE) {
                    BufferedImage image = pieceImages.get(piece);
                    if (image != null) {
                        // Center the image in the square
                        int imageSize = image.getWidth();
                        int offset = (SQUARE_SIZE - imageSize) / 2;
                        int x = col * SQUARE_SIZE + offset;
                        int y = row * SQUARE_SIZE + offset;
                        
                        g.drawImage(image, x, y, null);
                    }
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
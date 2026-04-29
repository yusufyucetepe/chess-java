package chess;
import javax.swing.ImageIcon;
import java.awt.Image;

public class ChessPiece {
    private PieceType type;
    private boolean isWhite;
    private ImageIcon imageIcon;
    
    public ChessPiece(PieceType type, boolean isWhite) {
        this.type = type;
        this.isWhite = isWhite;
        loadImage();
    }
    
    private void loadImage() {
        String color = isWhite ? "white" : "black";
        String pieceName = type.name().toLowerCase();
        String imagePath = "assets/" + color + "_" + pieceName + ".png";
        
        try {
            ImageIcon original = new ImageIcon(imagePath);
            // Scale image to fit square size (adjust 60x60 to your board square size)
            Image scaledImage = original.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            this.imageIcon = new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
        }
    }

    public PieceType getType() {
        return type;
    }
    
    public boolean isWhite() {
        return isWhite;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }
    
    public String getSymbol() {
        switch (type) {
            case KING: return isWhite ? "♔" : "♚";
            case QUEEN: return isWhite ? "♕" : "♛";
            case ROOK: return isWhite ? "♖" : "♜";
            case BISHOP: return isWhite ? "♗" : "♝";
            case KNIGHT: return isWhite ? "♘" : "♞";
            case PAWN: return isWhite ? "♙" : "♟";
            default: return "";
        }
    }
}

enum PieceType {
    KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
}

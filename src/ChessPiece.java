public class ChessPiece {
    private PieceType type;
    private boolean isWhite;
    
    public ChessPiece(PieceType type, boolean isWhite) {
        this.type = type;
        this.isWhite = isWhite;
    }
    
    public PieceType getType() {
        return type;
    }
    
    public boolean isWhite() {
        return isWhite;
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
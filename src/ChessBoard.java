public class ChessBoard {
    private ChessPiece[][] squares;
    private boolean whiteTurn;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean gameOver = false;
    
    public ChessBoard() {
        squares = new ChessPiece[8][8];
        whiteTurn = true;
        initializeBoard();
    }
    
    private void initializeBoard() {
        // Place black pieces
        squares[0][0] = new ChessPiece(PieceType.ROOK, false);
        squares[0][1] = new ChessPiece(PieceType.KNIGHT, false);
        squares[0][2] = new ChessPiece(PieceType.BISHOP, false);
        squares[0][3] = new ChessPiece(PieceType.QUEEN, false);
        squares[0][4] = new ChessPiece(PieceType.KING, false);
        squares[0][5] = new ChessPiece(PieceType.BISHOP, false);
        squares[0][6] = new ChessPiece(PieceType.KNIGHT, false);
        squares[0][7] = new ChessPiece(PieceType.ROOK, false);
        
        for (int col = 0; col < 8; col++) {
            squares[1][col] = new ChessPiece(PieceType.PAWN, false);
        }
        
        // Empty squares
        for (int row = 2; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = null;
            }
        }
        
        // Place white pieces
        for (int col = 0; col < 8; col++) {
            squares[6][col] = new ChessPiece(PieceType.PAWN, true);
        }
        
        squares[7][0] = new ChessPiece(PieceType.ROOK, true);
        squares[7][1] = new ChessPiece(PieceType.KNIGHT, true);
        squares[7][2] = new ChessPiece(PieceType.BISHOP, true);
        squares[7][3] = new ChessPiece(PieceType.QUEEN, true);
        squares[7][4] = new ChessPiece(PieceType.KING, true);
        squares[7][5] = new ChessPiece(PieceType.BISHOP, true);
        squares[7][6] = new ChessPiece(PieceType.KNIGHT, true);
        squares[7][7] = new ChessPiece(PieceType.ROOK, true);
    }
    
    public ChessPiece getPiece(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return null;
        }
        return squares[row][col];
    }
    
    public void handleClick(int row, int col) {
        if (gameOver) return;
        
        // First click - select piece
        if (selectedRow == -1) {
            ChessPiece piece = getPiece(row, col);
            if (piece != null && piece.isWhite() == whiteTurn) {
                selectedRow = row;
                selectedCol = col;
            }
        } else {
            // Second click - try to move
            if (isValidMove(selectedRow, selectedCol, row, col)) {
                // Make the move
                squares[row][col] = squares[selectedRow][selectedCol];
                squares[selectedRow][selectedCol] = null;
                
                // Pawn promotion
                if (squares[row][col].getType() == PieceType.PAWN) {
                    if (row == 0 || row == 7) {
                        squares[row][col] = new ChessPiece(PieceType.QUEEN, 
                                                          squares[row][col].isWhite());
                    }
                }
                
                whiteTurn = !whiteTurn;
            }
            
            selectedRow = -1;
            selectedCol = -1;
        }
    }
    
    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        ChessPiece piece = getPiece(fromRow, fromCol);
        ChessPiece target = getPiece(toRow, toCol);
        
        if (piece == null) return false;
        if (target != null && target.isWhite() == piece.isWhite()) return false;
        
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        
        switch (piece.getType()) {
            case PAWN:
                return isValidPawnMove(piece, fromRow, fromCol, toRow, toCol, target);
            case ROOK:
                return (rowDiff == 0 || colDiff == 0) && isPathClear(fromRow, fromCol, toRow, toCol);
            case KNIGHT:
                return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
            case BISHOP:
                return rowDiff == colDiff && isPathClear(fromRow, fromCol, toRow, toCol);
            case QUEEN:
                return (rowDiff == colDiff || rowDiff == 0 || colDiff == 0) 
                       && isPathClear(fromRow, fromCol, toRow, toCol);
            case KING:
                return rowDiff <= 1 && colDiff <= 1;
        }
        
        return false;
    }
    
    private boolean isValidPawnMove(ChessPiece piece, int fromRow, int fromCol, 
                                   int toRow, int toCol, ChessPiece target) {
        int direction = piece.isWhite() ? -1 : 1;
        int startRow = piece.isWhite() ? 6 : 1;
        
        // Move forward
        if (fromCol == toCol && target == null) {
            if (toRow == fromRow + direction) return true;
            if (fromRow == startRow && toRow == fromRow + 2 * direction 
                && getPiece(fromRow + direction, fromCol) == null) return true;
        }
        
        // Capture diagonally
        if (Math.abs(toCol - fromCol) == 1 && toRow == fromRow + direction && target != null) {
            return true;
        }
        
        return false;
    }
    
    private boolean isPathClear(int fromRow, int fromCol, int toRow, int toCol) {
        int rowStep = Integer.compare(toRow, fromRow);
        int colStep = Integer.compare(toCol, fromCol);
        
        int currentRow = fromRow + rowStep;
        int currentCol = fromCol + colStep;
        
        while (currentRow != toRow || currentCol != toCol) {
            if (getPiece(currentRow, currentCol) != null) {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        
        return true;
    }
    
    public boolean isWhiteTurn() {
        return whiteTurn;
    }
    
    public int getSelectedRow() {
        return selectedRow;
    }
    
    public int getSelectedCol() {
        return selectedCol;
    }
}
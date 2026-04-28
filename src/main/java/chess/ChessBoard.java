package chess;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    private Board board;
    private Square selectedSquare;
    private List<Square> validMoves;
    
    public ChessBoard() {
        board = new Board();
        selectedSquare = null;
        validMoves = new ArrayList<>();
    }
    
    public Board getBoard() {
        return board;
    }
    
    public void handleClick(int row, int col) {
        if (board.isMated() || board.isDraw()) {
            return;
        }
        
        Square clickedSquare = getSquareFromCoords(row, col);
        
        // First click - select piece
        if (selectedSquare == null) {
            Piece piece = board.getPiece(clickedSquare);
            if (piece != Piece.NONE && isPieceCurrentPlayer(piece)) {
                selectedSquare = clickedSquare;
                validMoves = getValidMovesForSquare(clickedSquare);
            }
        } else {
            // Second click - try to move
            Move move = new Move(selectedSquare, clickedSquare);
            
            // Check if this is a valid move (including promotion)
            boolean moveExecuted = false;
            for (Move legalMove : board.legalMoves()) {
                if (legalMove.getFrom() == selectedSquare && legalMove.getTo() == clickedSquare) {
                    board.doMove(legalMove);
                    moveExecuted = true;
                    break;
                }
            }
            
            // Deselect regardless of whether move was successful
            selectedSquare = null;
            validMoves.clear();
        }
    }
    
    private boolean isPieceCurrentPlayer(Piece piece) {
        if (piece == Piece.NONE) return false;
        return (board.getSideToMove().name().equals("WHITE") && piece.getPieceSide().name().equals("WHITE"))
            || (board.getSideToMove().name().equals("BLACK") && piece.getPieceSide().name().equals("BLACK"));
    }
    
    private List<Square> getValidMovesForSquare(Square square) {
        List<Square> moves = new ArrayList<>();
        for (Move move : board.legalMoves()) {
            if (move.getFrom() == square) {
                moves.add(move.getTo());
            }
        }
        return moves;
    }
    
    private Square getSquareFromCoords(int row, int col) {
        // Convert board coordinates to Square enum
        // Row 0 = Rank 8, Row 7 = Rank 1
        // Col 0 = File A, Col 7 = File H
        int fileIndex = col;
        int rankIndex = 7 - row;
        return Square.squareAt(fileIndex + rankIndex * 8);
    }
    
    public Square getSelectedSquare() {
        return selectedSquare;
    }
    
    public List<Square> getValidMoves() {
        return validMoves;
    }
    
    public boolean isGameOver() {
        return board.isMated() || board.isDraw();
    }
    
    public String getGameStatus() {
        if (board.isMated()) {
            String winner = board.getSideToMove().name().equals("WHITE") ? "Black" : "White";
            return "Checkmate! " + winner + " wins!";
        } else if (board.isDraw()) {
            return "Draw!";
        } else if (board.isKingAttacked()) {
            return "Check!";
        }
        return null;
    }
}
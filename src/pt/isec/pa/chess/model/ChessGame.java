package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.pieces.*;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ChessGame implements Serializable {
    @Serial
    static final long serialVersionUID = 1L;

    Board board;
    PieceColor cCurrentPlayer = PieceColor.WHITE;
    PieceColor cWinner = null;
    String sPlayer1, sPlayer2;

    public ChessGame() {
        board = new Board();
        board.clearBoard();
        board.initializeBoard();
    }

    public void setPlayerName(int player, String name) {
        if (player < 1 || player > 2) {
            throw new RuntimeException("tried to set invalid player");
            //System.out.println("Invalid player number");
            //return;
        }
        if (player == 1) {
            sPlayer1 = name;
        } else {
            sPlayer2 = name;
        }
    }

    public String debug() {
        //
        StringBuilder sb = new StringBuilder();
        sb.append("\n--------------GAME DEBUG INFO------------------\n");
        sb.append("Current player: " + cCurrentPlayer);
        sb.append("\n");
        sb.append(board.debug());
        sb.append("\n");


        sb.append("Winner: " + cWinner);
        sb.append("\n");
        return sb.toString();
    }

    public void start() {
        board.clearBoard();
        board.initializeBoard();
    }

    public String printPieces() {
        return board.printPieces();
    }

    public PieceColor getcCurrentPlayer() {
        return cCurrentPlayer;
    }

    public PieceColor getcOpponent() {
        return cCurrentPlayer == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
    }

    public PieceColor getWinner() {
        return cWinner;
    }

    public boolean isGameOver() {
        boolean bWhiteKing = false;
        boolean bBlackKing = false;

        for (Piece p : board.getPieces()) {
            if (Character.toUpperCase(p.getDefinition()) == 'K') {
                if (p.getColor() == PieceColor.WHITE) {
                    bWhiteKing = true;
                } else if (p.getColor() == PieceColor.BLACK) {
                    bBlackKing = true;
                }
            }
        }

        if (!bWhiteKing) {
            cWinner = PieceColor.BLACK;
            return true;
        } else if (!bBlackKing) {
            cWinner = PieceColor.WHITE;
            return true;
        }
        return false;
    }

    public void resetGame(){
        cCurrentPlayer = PieceColor.WHITE;
        board = new Board();
        board.initializeBoard();
    }

    public boolean bIsCheckmate(PieceColor cPieceColor) {
        Set<Position> sPositions = new HashSet<>();
        Position kingPos = null;
        Piece kingPiece = null;

        for (Piece piece : board.getAllPieces()) {
            if (Character.toUpperCase(piece.getDefinition()) == 'K' && piece.getColor() == cPieceColor) {
                kingPos = piece.getPosition();
                kingPiece = piece;
            }
            if (piece.getColor() != cPieceColor) {
                sPositions.addAll(piece.getPossibleMoves());
            }
        }

        if (kingPos == null) {
            return false;
        }

        if (!sPositions.contains(kingPos)) {
            return false;
        }

        for (Position move : kingPiece.getPossibleMoves()) {
            if (!sPositions.contains(move)) {
                return false;
            }
        }
        return true;
    }

    //PAWN PROMOTION
    public void promotePawn(Position position, char pieceType) {
        Piece pawn = board.getPiece(position);
        if (pawn == null || !(pawn instanceof Pawn)) return;

        PieceColor color = pawn.getColor();
        board.removePiece(position);

        char normalizedType = Character.toUpperCase(pieceType);
        Piece newPiece = switch (normalizedType) {
            case 'Q' -> PieceFactory.QUEEN.createPiece(color, board, position.getColumn(), position.getRow());
            case 'R' -> PieceFactory.ROOK.createPiece(color, board, position.getColumn(), position.getRow());
            case 'B' -> PieceFactory.BISHOP.createPiece(color, board, position.getColumn(), position.getRow());
            case 'N' -> PieceFactory.KNIGHT.createPiece(color, board, position.getColumn(), position.getRow());
            default -> throw new IllegalArgumentException("Peça inválida: " + pieceType);
        };

        board.addPiece(position, newPiece);
    }

}
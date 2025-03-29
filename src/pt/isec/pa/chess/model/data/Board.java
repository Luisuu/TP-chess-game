package pt.isec.pa.chess.model.data;

import pt.isec.pa.chess.model.data.pieces.Piece;
import pt.isec.pa.chess.model.data.pieces.PieceFactory;
import pt.isec.pa.chess.model.data.pieces.Color;
import pt.isec.pa.chess.model.data.pieces.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Board {
    private Map<String, Piece> mBoard;

    public Board() {
        mBoard = new HashMap<>();
    }

    public void initializeBoard() {
        for (char cCol = 'a'; cCol <= 'h'; cCol++) {
            mBoard.put("" + cCol + 2, PieceFactory.PAWN.createPiece(Color.WHITE, this, cCol, 2));
            mBoard.put("" + cCol + 7, PieceFactory.PAWN.createPiece(Color.BLACK, this, cCol, 7));
        }

        mBoard.put("a1", PieceFactory.ROOK.createPiece(Color.WHITE, this, 'a', 1));
        mBoard.put("b1", PieceFactory.KNIGHT.createPiece(Color.WHITE, this, 'b', 1));
        mBoard.put("c1", PieceFactory.BISHOP.createPiece(Color.WHITE, this, 'c', 1));
        mBoard.put("d1", PieceFactory.QUEEN.createPiece(Color.WHITE, this, 'd', 1));
        mBoard.put("e1", PieceFactory.KING.createPiece(Color.WHITE, this, 'e', 1));
        mBoard.put("f1", PieceFactory.BISHOP.createPiece(Color.WHITE, this, 'f', 1));
        mBoard.put("g1", PieceFactory.KNIGHT.createPiece(Color.WHITE, this, 'g', 1));
        mBoard.put("h1", PieceFactory.ROOK.createPiece(Color.WHITE, this, 'h', 1));

        mBoard.put("a8", PieceFactory.ROOK.createPiece(Color.BLACK, this, 'a', 8));
        mBoard.put("b8", PieceFactory.KNIGHT.createPiece(Color.BLACK, this, 'b', 8));
        mBoard.put("c8", PieceFactory.BISHOP.createPiece(Color.BLACK, this, 'c', 8));
        mBoard.put("d8", PieceFactory.QUEEN.createPiece(Color.BLACK, this, 'd', 8));
        mBoard.put("e8", PieceFactory.KING.createPiece(Color.BLACK, this, 'e', 8));
        mBoard.put("f8", PieceFactory.BISHOP.createPiece(Color.BLACK, this, 'f', 8));
        mBoard.put("g8", PieceFactory.KNIGHT.createPiece(Color.BLACK, this, 'g', 8));
        mBoard.put("h8", PieceFactory.ROOK.createPiece(Color.BLACK, this, 'h', 8));
    }

    public void addPiece(String position, Piece piece) {
        mBoard.put(position, piece);
    }

    public void removePiece(String position) {
        mBoard.remove(position);
    }

    public Piece getPiece(String position) {
        return mBoard.get(position);
    }

    public String printPieces() {
        StringBuilder sbStringBuilder = new StringBuilder();

        for (Piece piece : mBoard.values()) {
            if (piece != null) {
                sbStringBuilder.append(piece).append(",");
            }
        }

        if (!sbStringBuilder.isEmpty() && sbStringBuilder.charAt(sbStringBuilder.length() - 1) == ',') {
            sbStringBuilder.deleteCharAt(sbStringBuilder.length() - 1);
        }

        return sbStringBuilder.toString();
    }

    public String debug() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n----------------DEBUG INFO-------------------\n");

        for (Piece piece : mBoard.values()) {
            if (piece != null) {
                sb.append(piece).append("\n");

                Set<Position> moves = piece.getPossibleMoves();
                for (Position move : moves) {
                    sb.append(move.toString()).append(" ");
                }

                sb.append("\n");
            }
        }

        sb.append("--------------------------------------------\n");

        return sb.toString();
    }
}
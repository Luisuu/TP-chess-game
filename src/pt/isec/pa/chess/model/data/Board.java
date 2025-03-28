package pt.isec.pa.chess.model.data;

import pt.isec.pa.chess.model.data.pieces.Piece;
import pt.isec.pa.chess.model.data.pieces.PieceFactory;
import pt.isec.pa.chess.model.data.pieces.Color;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<String, Piece> mBoard;

    public Board() {
        mBoard = new HashMap<>();
        initializeBoard();
    }

    private void initializeBoard() {
        for (char cCol = 'a'; cCol <= 'h'; cCol++) {
            mBoard.put("" + cCol + 2, PieceFactory.PAWN.createPiece(Color.WHITE, cCol, 2));
            mBoard.put("" + cCol + 7, PieceFactory.PAWN.createPiece(Color.BLACK, cCol, 7));
        }

        mBoard.put("a1", PieceFactory.ROOK.createPiece(Color.WHITE, 'a', 1));
        mBoard.put("b1", PieceFactory.KNIGHT.createPiece(Color.WHITE, 'b', 1));
        mBoard.put("c1", PieceFactory.BISHOP.createPiece(Color.WHITE, 'c', 1));
        mBoard.put("d1", PieceFactory.QUEEN.createPiece(Color.WHITE, 'd', 1));
        mBoard.put("e1", PieceFactory.KING.createPiece(Color.WHITE, 'e', 1));
        mBoard.put("f1", PieceFactory.BISHOP.createPiece(Color.WHITE, 'f', 1));
        mBoard.put("g1", PieceFactory.KNIGHT.createPiece(Color.WHITE, 'g', 1));
        mBoard.put("h1", PieceFactory.ROOK.createPiece(Color.WHITE, 'h', 1));

        mBoard.put("a8", PieceFactory.ROOK.createPiece(Color.BLACK, 'a', 8));
        mBoard.put("b8", PieceFactory.KNIGHT.createPiece(Color.BLACK, 'b', 8));
        mBoard.put("c8", PieceFactory.BISHOP.createPiece(Color.BLACK, 'c', 8));
        mBoard.put("d8", PieceFactory.QUEEN.createPiece(Color.BLACK, 'd', 8));
        mBoard.put("e8", PieceFactory.KING.createPiece(Color.BLACK, 'e', 8));
        mBoard.put("f8", PieceFactory.BISHOP.createPiece(Color.BLACK, 'f', 8));
        mBoard.put("g8", PieceFactory.KNIGHT.createPiece(Color.BLACK, 'g', 8));
        mBoard.put("h8", PieceFactory.ROOK.createPiece(Color.BLACK, 'h', 8));
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
                sbStringBuilder.append(piece.toString()).append(",");
            }
        }

        if (sbStringBuilder.length() > 0 && sbStringBuilder.charAt(sbStringBuilder.length() - 1) == ',') {
            sbStringBuilder.deleteCharAt(sbStringBuilder.length() - 1);
        }

        return sbStringBuilder.toString();
    }
}
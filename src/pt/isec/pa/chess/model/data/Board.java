package pt.isec.pa.chess.model.data;

import pt.isec.pa.chess.model.data.pieces.Piece;
import pt.isec.pa.chess.model.data.pieces.PieceFactory;
import pt.isec.pa.chess.model.data.pieces.Color;
import pt.isec.pa.chess.model.data.pieces.Position;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board implements Serializable {
    @Serial
    static final long serialVersionUID = 1L;

    private Map<Position, Piece> mBoard;

    public Board() {
        mBoard = new HashMap<>();
    }

    public void clearBoard() {
        mBoard.clear();
    }

    public void initializeBoard() {
        for (char cCol = 'a'; cCol <= 'h'; cCol++) {
            mBoard.put(new Position(cCol, 2), PieceFactory.PAWN.createPiece(Color.WHITE, this, cCol, 2));
            mBoard.put(new Position(cCol, 7), PieceFactory.PAWN.createPiece(Color.BLACK, this, cCol, 7));
        }

        mBoard.put(new Position('a', 1), PieceFactory.ROOK.createPiece(Color.WHITE, this, 'a', 1));
        mBoard.put(new Position('b', 1), PieceFactory.KNIGHT.createPiece(Color.WHITE, this, 'b', 1));
        mBoard.put(new Position('c', 1), PieceFactory.BISHOP.createPiece(Color.WHITE, this, 'c', 1));
        mBoard.put(new Position('d', 1), PieceFactory.QUEEN.createPiece(Color.WHITE, this, 'd', 1));
        mBoard.put(new Position('e', 1), PieceFactory.KING.createPiece(Color.WHITE, this, 'e', 1));
        mBoard.put(new Position('f', 1), PieceFactory.BISHOP.createPiece(Color.WHITE, this, 'f', 1));
        mBoard.put(new Position('g', 1), PieceFactory.KNIGHT.createPiece(Color.WHITE, this, 'g', 1));
        mBoard.put(new Position('h', 1), PieceFactory.ROOK.createPiece(Color.WHITE, this, 'h', 1));

        mBoard.put(new Position('a', 8), PieceFactory.ROOK.createPiece(Color.BLACK, this, 'a', 8));
        mBoard.put(new Position('b', 8), PieceFactory.KNIGHT.createPiece(Color.BLACK, this, 'b', 8));
        mBoard.put(new Position('c', 8), PieceFactory.BISHOP.createPiece(Color.BLACK, this, 'c', 8));
        mBoard.put(new Position('d', 8), PieceFactory.QUEEN.createPiece(Color.BLACK, this, 'd', 8));
        mBoard.put(new Position('e', 8), PieceFactory.KING.createPiece(Color.BLACK, this, 'e', 8));
        mBoard.put(new Position('f', 8), PieceFactory.BISHOP.createPiece(Color.BLACK, this, 'f', 8));
        mBoard.put(new Position('g', 8), PieceFactory.KNIGHT.createPiece(Color.BLACK, this, 'g', 8));
        mBoard.put(new Position('h', 8), PieceFactory.ROOK.createPiece(Color.BLACK, this, 'h', 8));
    }

    public void initializeBoard(String[] sJogo) {
        for (String p : sJogo) {
            Piece piece = PieceFactory.PieceFactoryFromText(p, this);
            if (piece != null) {
                mBoard.put(piece.getPosition(), piece);
            }
        }
    }

    public Set<Piece> getPieces() {
        return new HashSet<>(mBoard.values());
    }

    public void addPiece(Position position, Piece piece) {
        mBoard.put(position, piece);
    }

    public void removePiece(Position position) {
        mBoard.remove(position);
    }

    public boolean isEmpty(Position position) {
        for (Piece piece : mBoard.values()) {
            if (piece.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    public Piece getPiece(Position position) {
        return mBoard.get(position);
    }

    public boolean bIsCheckmate(Color cColor) {
        Set<Position> sPositions = new HashSet<>();
        Position kingPos = null;
        Piece kingPiece = null;

        for (Piece piece : mBoard.values()) {
            if (Character.toUpperCase(piece.getDefinition()) == 'K' && piece.getColor() == cColor) {
                kingPos = piece.getPosition();
                kingPiece = piece;
            }
            if (piece.getColor() != cColor) {
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

        sb.append("\n----------------BOARD DEBUG INFO-------------------\n");

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
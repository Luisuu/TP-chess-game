package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;

import java.util.HashSet;
import java.util.Set;

public class Rook extends Piece {
    private boolean bHasMoved;

    public Rook(Color color, Board board, char cColumn, int iRow) {
        super('R', board, color, cColumn, iRow);
        this.bHasMoved = false;
    }

    public Set<Position> getPossibleMoves() {
        Set<Position> possibleMoves = new HashSet<>();

        for (int row = pPos.getRow(); row >= 1; row--) {
            Position newPos = new Position(pPos.getColumn(), row);
            Piece pieceAtNewPos = board.getPiece(newPos);
            if (row != pPos.getRow()) {
                if (pieceAtNewPos != null) {
                    if (pieceAtNewPos.getColor() == this.eColor) {
                        break;
                    } else {
                        possibleMoves.add(newPos);
                        break;
                    }
                }
                possibleMoves.add(newPos);
            }
        }

        for (int row = pPos.getRow(); row <= 8; row++) {
            Position newPos = new Position(pPos.getColumn(), row);
            Piece pieceAtNewPos = board.getPiece(newPos);
            if (row != pPos.getRow()) {
                if (pieceAtNewPos != null) {
                    if (pieceAtNewPos.getColor() == this.eColor) {
                        break;
                    } else {
                        possibleMoves.add(newPos);
                        break;
                    }
                }
                possibleMoves.add(newPos);
            }
        }

        for (char col = pPos.getColumn(); col >= 'a'; col--) {
            Position newPos = new Position(col, pPos.getRow());
            Piece pieceAtNewPos = board.getPiece(newPos);
            if (col != pPos.getColumn()) {
                if (pieceAtNewPos != null) {
                    if (pieceAtNewPos.getColor() == this.eColor) {
                        break;
                    } else {
                        possibleMoves.add(newPos);
                        break;
                    }
                }
                possibleMoves.add(newPos);
            }
        }

        for (char col = pPos.getColumn(); col <= 'h'; col++) {
            Position newPos = new Position(col, pPos.getRow());
            Piece pieceAtNewPos = board.getPiece(newPos);
            if (col != pPos.getColumn()) {
                if (pieceAtNewPos != null) {
                    if (pieceAtNewPos.getColor() == this.eColor) {
                        break;
                    } else {
                        possibleMoves.add(newPos);
                        break;
                    }
                }
                possibleMoves.add(newPos);
            }
        }

        return possibleMoves;
    }

    public boolean hasMoved() {
        return bHasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.bHasMoved = hasMoved;
    }

    @Override
    public String toString() {
        return super.toString() + (bHasMoved ? "" : "*");
    }
}
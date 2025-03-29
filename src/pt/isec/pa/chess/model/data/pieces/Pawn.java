package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;

import java.util.HashSet;
import java.util.Set;

public class Pawn extends Piece {
    private boolean bHasMoved;

    public Pawn(Color color, Board board, char cColumn, int iRow) {
        super('P', board, color, cColumn, iRow);
        this.bHasMoved = false;
    }

    public Set<Position> getPossibleMoves() {
        Set<Position> possibleMoves = new HashSet<>();

        int direction = (eColor == Color.WHITE) ? 1 : -1;
        char col = pPos.getColumn();
        int row = pPos.getRow();

        Position oneStep = new Position(col, row + direction);
        if (isPossibleMove(oneStep) && board.getPiece(oneStep) == null) {
            possibleMoves.add(oneStep);

            Position twoSteps = new Position(col, row + 2 * direction);
            if (!bHasMoved && isPossibleMove(twoSteps) && board.getPiece(twoSteps) == null) {
                possibleMoves.add(twoSteps);
            }
        }

        char colLeft = (char) (col - 1);
        if (colLeft >= 'a') {
            Position diagLeft = new Position(colLeft, row + direction);
            if (isPossibleMove(diagLeft)) {
                Piece targetLeft = board.getPiece(diagLeft);
                if (targetLeft != null && targetLeft.getColor() != this.eColor) {
                    possibleMoves.add(diagLeft);
                }
            }
        }

        char colRight = (char) (col + 1);
        if (colRight <= 'h') {
            Position diagRight = new Position(colRight, row + direction);
            if (isPossibleMove(diagRight)) {
                Piece targetRight = board.getPiece(diagRight);
                if (targetRight != null && targetRight.getColor() != this.eColor) {
                    possibleMoves.add(diagRight);
                }
            }
        }

        /*
        if (this.eColor == Color.BLACK) {
            char cColDL = (char) (pPos.getColumn() - 1);
            int iRowDL = pPos.getRow() - 1;
            char cColDR = (char) (pPos.getColumn() + 1);
            int iRowDR = pPos.getRow() - 1;

            int iRow1 = pPos.getRow() - 1;
            int iRow2 = pPos.getRow() - 2;

            if (cColDL >= 'a' && cColDL <= 'h' && iRowDL >= 1 && iRowDL <= 8) {
                Piece newPiece = board.getPiece(new Position(cColDL, iRowDL));
                if (newPiece != null && newPiece.getColor() != this.eColor) {
                    possibleMoves.add(new Position(cColDL, iRowDL));
                }
            }

            if (cColDR >= 'a' && cColDR <= 'h' && iRowDR >= 1 && iRowDR <= 8) {
                Piece newPiece = board.getPiece(new Position(cColDR, iRowDR));
                if (newPiece != null && newPiece.getColor() != this.eColor) {
                    possibleMoves.add(new Position(cColDR, iRowDR));
                }
            }

            if (this.getPosition().getColumn() >= 'a' && this.getPosition().getColumn() <= 'h' && iRow1 >= 1 && iRow1 <= 8) {
                Piece newPiece = board.getPiece(new Position(this.getPosition().getColumn(), iRow1));
                if (newPiece == null) {
                    possibleMoves.add(new Position(this.getPosition().getColumn(), iRow1));
                    if (!bHasMoved) {
                        Piece newPiece2 = board.getPiece(new Position(this.getPosition().getColumn(), iRow2));
                        if (newPiece2 == null) {
                            possibleMoves.add(new Position(this.getPosition().getColumn(), iRow2));
                        }
                    }
                }
            }
        } */

        return possibleMoves;
    }
}

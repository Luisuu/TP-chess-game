package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;

import java.util.HashSet;
import java.util.Set;

public class Queen extends Piece {
    public Queen(Color color, Board board, char cColumn, int iRow) {
        super('Q', board, color, cColumn, iRow);
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

        int[][] moves = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};

        for (int[] move : moves) {
            for (int i = 1; i <= 8; i++) {
                char cCol = (char) (pPos.getColumn() + move[0] * i);
                int iRow = pPos.getRow() + move[1] * i;

                if (cCol < 'a' || cCol > 'h' || iRow < 1 || iRow > 8) {
                    break;
                }

                Position newPos = new Position(cCol, iRow);
                Piece pieceAtNewPos = board.getPiece(newPos);

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
}
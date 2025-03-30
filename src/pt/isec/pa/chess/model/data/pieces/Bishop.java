package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;


import java.util.HashSet;
import java.util.Set;

public class Bishop extends Piece {
    public Bishop(Color color, Board board, char cColumn, int iRow) {
        super('B', board, color, cColumn, iRow);
    }

    public Set<Position> getPossibleMoves() {
        Set<Position> possibleMoves = new HashSet<>();

        for (int d = 0; d < 4; d++) {

            if (d == 0) {
                for (int i = 1; i <= 8; i++) {
                    char newCol = (char) (pPos.getColumn() + i);
                    int newRow = pPos.getRow() + i;

                    if (newCol >= 'a' && newCol <= 'h' && newRow >= 1 && newRow <= 8) {
                        Position newPos = new Position(newCol, newRow);
                        Piece pieceAtNewPos = board.getPiece(newPos);

                        if (pieceAtNewPos != null) {
                            if (pieceAtNewPos.getColor() != this.getColor()) {
                                possibleMoves.add(newPos);
                            }
                            break;
                        }
                        possibleMoves.add(newPos);
                    } else {
                        break;
                    }
                }
            }

            if (d == 1) {
                for (int i = 1; i <= 8; i++) {
                    char newCol = (char) (pPos.getColumn() - i);
                    int newRow = pPos.getRow() + i;

                    if (newCol >= 'a' && newCol <= 'h' && newRow >= 1 && newRow <= 8) {
                        Position newPos = new Position(newCol, newRow);
                        Piece pieceAtNewPos = board.getPiece(newPos);

                        if (pieceAtNewPos != null) {
                            if (pieceAtNewPos.getColor() != this.getColor()) {
                                possibleMoves.add(newPos);
                            }
                            break;
                        }
                        possibleMoves.add(newPos);
                    } else {
                        break;
                    }
                }
            }

            if (d == 2) {
                for (int i = 1; i <= 8; i++) {
                    char newCol = (char) (pPos.getColumn() + i);
                    int newRow = pPos.getRow() - i;

                    if (newCol >= 'a' && newCol <= 'h' && newRow >= 1 && newRow <= 8) {
                        Position newPos = new Position(newCol, newRow);
                        Piece pieceAtNewPos = board.getPiece(newPos);

                        if (pieceAtNewPos != null) {
                            if (pieceAtNewPos.getColor() != this.getColor()) {
                                possibleMoves.add(newPos);
                            }
                            break;
                        }
                        possibleMoves.add(newPos);
                    } else {
                        break;
                    }
                }
            }

            if (d == 3) {
                for (int i = 1; i <= 8; i++) {
                    char newCol = (char) (pPos.getColumn() - i);
                    int newRow = pPos.getRow() - i;

                    if (newCol >= 'a' && newCol <= 'h' && newRow >= 1 && newRow <= 8) {
                        Position newPos = new Position(newCol, newRow);
                        Piece pieceAtNewPos = board.getPiece(newPos);

                        if (pieceAtNewPos != null) {
                            if (pieceAtNewPos.getColor() != this.getColor()) {
                                possibleMoves.add(newPos);
                            }
                            break;
                        }
                        possibleMoves.add(newPos);
                    } else {
                        break;
                    }
                }
            }
        }

        return possibleMoves;
    }
}
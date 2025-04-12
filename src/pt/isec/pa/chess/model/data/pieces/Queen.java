package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;

import java.util.HashSet;
import java.util.Set;

public class Queen extends Piece {
    public Queen(PieceColor PieceColor, Board board, char cColumn, int iRow) {
        super('Q', board, PieceColor, cColumn, iRow);
    }

    public Set<Position> getPossibleMoves() {
        Set<Position> possibleMoves = new HashSet<>();


        int[][] moves = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};

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
                    if (pieceAtNewPos.getColor() == this.PieceColor) {
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
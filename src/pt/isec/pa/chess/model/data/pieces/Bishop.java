package pt.isec.pa.chess.model.data.pieces;

import java.util.HashSet;
import java.util.Set;

public class Bishop extends Piece {
    public Bishop(Color color, char cColumn, int iRow) {
        super('B', color, cColumn, iRow);
    }

    public Set<Position> getPossibleMoves() {
        Set<Position> possibleMoves = new HashSet<>();

        for (int i = 1; i <= 8; i++) {
            char col1 = (char) (pPos.getColumn() + i);
            char col2 = (char) (pPos.getColumn() - i);
            int row1 = pPos.getRow() + i;
            int row2 = pPos.getRow() - i;

            if (col1 >= 'a' && col1 <= 'h' && row1 >= 1 && row1 <= 8) {
                possibleMoves.add(new Position(col1, row1));
            }

            if (col2 >= 'a' && col2 <= 'h' && row1 >= 1 && row1 <= 8) {
                possibleMoves.add(new Position(col2, row1));
            }

            if (col1 >= 'a' && col1 <= 'h' && row2 >= 1 && row2 <= 8) {
                possibleMoves.add(new Position(col1, row2));
            }

            if (col2 >= 'a' && col2 <= 'h' && row2 >= 1 && row2 <= 8) {
                possibleMoves.add(new Position(col2, row2));
            }
        }

        return possibleMoves;
    }

}
package pt.isec.pa.chess.model.data.pieces;

import java.util.HashSet;
import java.util.Set;

public class Knight extends Piece {
    public Knight(Color color, char cColumn, int iRow) {
        super('N', color, cColumn, iRow);
    }

    public Set<Position> getPossibleMoves() {
        Set<Position> possibleMoves = new HashSet<>();

        int[][] moves = {
                {-1, 2}, {-1, -2}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {2, 1}, {2, -1}
        };

        for (int[] move : moves) {
            char newCol = (char) (pPos.getColumn() + move[0]);
            int newRow = pPos.getRow() + move[1];

            if (newCol >= 'a' && newCol <= 'h' && newRow >= 1 && newRow <= 8) {
                possibleMoves.add(new Position(newCol, newRow));
            }
        }

        return possibleMoves;
    }
}

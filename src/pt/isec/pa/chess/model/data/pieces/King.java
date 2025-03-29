package pt.isec.pa.chess.model.data.pieces;

import java.util.HashSet;
import java.util.Set;

public class King extends Piece {
    private boolean bHasMoved;

    public King(Color color, char cColumn, int iRow) {
        super('K', color, cColumn, iRow);
        this.bHasMoved = false;
    }

    public Set<Position> getPossibleMoves() {
        Set<Position> possibleMoves = new HashSet<>();

        int[][] moves = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0},
                {1, 1}, {-1, -1}, {1, -1}, {-1, 1}
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
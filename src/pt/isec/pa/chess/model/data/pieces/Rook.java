package pt.isec.pa.chess.model.data.pieces;

import java.util.HashSet;
import java.util.Set;

public class Rook extends Piece {
    private boolean bHasMoved;

    public Rook(Color color, char cColumn, int iRow) {
        super('R', color, cColumn, iRow);
        this.bHasMoved = false;
    }

    public Set<Position> getPossibleMoves() {
        Set<Position> possibleMoves = new HashSet<>();

        for (int row = 1; row <= 8; row++) {
            if (row != pPos.getRow()) {
                possibleMoves.add(new Position(pPos.getColumn(), row));
            }
        }

        for (char col = 'a'; col <= 'h'; col++) {
            if (col != pPos.getColumn()) {
                possibleMoves.add(new Position(col, pPos.getRow()));
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

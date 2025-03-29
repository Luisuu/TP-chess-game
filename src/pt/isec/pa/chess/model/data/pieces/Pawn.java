package pt.isec.pa.chess.model.data.pieces;

import java.util.HashSet;
import java.util.Set;

public class Pawn extends Piece {
    private boolean bHasMoved;

    public Pawn(Color color, char cColumn, int iRow) {
        super('P', color, cColumn, iRow);
        this.bHasMoved = false;
    }

    public Set<Position> getPossibleMoves() {
        Set<Position> possibleMoves = new HashSet<>();

        if (this.eColor == Color.WHITE) {
            possibleMoves.add(new Position(pPos.getColumn(), pPos.getRow() + 1));
            if (!bHasMoved) {
                possibleMoves.add(new Position(pPos.getColumn(), pPos.getRow() + 2));
            }
        }

        if (this.eColor == Color.BLACK) {
            possibleMoves.add(new Position(pPos.getColumn(), pPos.getRow() - 1));
            if (!bHasMoved) {
                possibleMoves.add(new Position(pPos.getColumn(), pPos.getRow() - 2));
            }
        }

        return possibleMoves;
    }
}

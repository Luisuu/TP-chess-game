package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;

import java.util.HashSet;
import java.util.Set;

public class Rook extends Piece {
    private boolean bHasMoved;

    public Rook(PieceColor PieceColor, Board board, char cColumn, int iRow) {
        super('R', board, PieceColor, cColumn, iRow);
        this.bHasMoved = (PieceColor == PieceColor.WHITE && iRow == 1 && (cColumn == 'a' || cColumn == 'h')) || (PieceColor == PieceColor.BLACK && iRow == 8 && (cColumn == 'a' || cColumn == 'h')) ? false : true;
    }

    public Set<Position> getPossibleMoves() {
        Set<Position> possibleMoves = new HashSet<>();

        int[][] moves = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        return getLinearMoves(moves);
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
package pt.isec.pa.chess.model.data.pieces;

public class Rook extends Piece {
    private boolean bHasMoved;

    public Rook(Color color, char cColumn, int iRow) {
        super('R', color, cColumn, iRow);
        this.bHasMoved = false;
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

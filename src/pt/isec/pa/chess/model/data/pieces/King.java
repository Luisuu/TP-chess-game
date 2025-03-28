package pt.isec.pa.chess.model.data.pieces;

public class King extends Piece {
    private boolean bHasMoved;

    public King(Color color, char cColumn, int iRow) {
        super('K', color, cColumn, iRow);
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
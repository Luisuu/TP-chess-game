package pt.isec.pa.chess.model.data.pieces;

public class Position {
    private char cColumn;
    private int iRow;

    public Position(char cColumn, int iRow) {
        if (cColumn < 'a' || cColumn > 'h' || iRow < 1 || iRow > 8) {
            throw new IllegalArgumentException("Invalid position: " + cColumn + iRow);
            //return;
            // VERIFICAR NO FUTURO
        }
        this.cColumn = cColumn;
        this.iRow = iRow;
    }

    public void setColumn(char cColumn) {
        if (cColumn < 'a' || cColumn > 'h') {
            throw new IllegalArgumentException("Invalid column: " + cColumn);
        }
        this.cColumn = cColumn;
    }

    public void setRow(int iRow) {
        if (iRow < 1 || iRow > 8) {
            throw new IllegalArgumentException("Invalid row: " + iRow);
        }
        this.iRow = iRow;
    }

    public char getColumn() {
        return cColumn;
    }

    public int getRow() {
        return iRow;
    }

    @Override
    public String toString() {
        return "(" + cColumn + "," + iRow + ")";
    }
}

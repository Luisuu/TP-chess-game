package pt.isec.pa.chess.model.data.pieces;

class Position {
    private char cColumn;
    private int iRow;

    public Position(char cColumn, int iRow) {
        if (cColumn < 'a' || cColumn > 'h' || iRow < 1 || iRow > 8) {
            throw new IllegalArgumentException("Invalid position: " + cColumn + iRow);
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
}

abstract public class Piece {
    protected char cDefinition;
    protected Color eColor;
    protected Position pPos;

    Piece(char cDefinition, Color eColor, char cColumn, int iRow) {
        this.cDefinition = (eColor == Color.WHITE) ? Character.toUpperCase(cDefinition) : Character.toLowerCase(cDefinition);
        this.eColor = eColor;
        this.pPos = new Position(cColumn, iRow);
    }

    public char getDefinition() {
        return cDefinition;
    }

    public Color getColor() {
        return eColor;
    }

    public Position getPosition() {
        return pPos;
    }

    @Override
    public String toString() {
        return this.getDefinition() + String.valueOf(pPos.getColumn()) + pPos.getRow();
    }
}
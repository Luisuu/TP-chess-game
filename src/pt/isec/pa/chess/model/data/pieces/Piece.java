package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;

import java.util.Set;

abstract public class Piece {
    protected char cDefinition;
    protected Color eColor;
    protected Position pPos;
    protected Board board;

    Piece(char cDefinition, Board board, Color eColor, char cColumn, int iRow) {
        this.cDefinition = (eColor == Color.WHITE) ? Character.toUpperCase(cDefinition) : Character.toLowerCase(cDefinition);
        this.eColor = eColor;
        this.pPos = new Position(cColumn, iRow);
        this.board = board;
    }

    abstract public Set<Position> getPossibleMoves();

    public void setPosition(Position pos) {
        this.pPos = pos;
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

    protected boolean isPossibleMove(Position pos) {
        return pos.getColumn() >= 'a' && pos.getColumn() <= 'h' && pos.getRow() >= 1 && pos.getRow() <= 8;
    }
}
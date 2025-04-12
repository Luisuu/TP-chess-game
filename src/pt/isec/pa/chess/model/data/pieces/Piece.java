package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;

import java.io.Serializable;
import java.util.Set;

abstract public class Piece implements Serializable {
    static final long serialVersionUID = 1L;
    protected char cDefinition;
    protected PieceColor PieceColor;
    protected Position pPos;
    protected Board board;

    Piece(char cDefinition, Board board, PieceColor PieceColor, char cColumn, int iRow) {
        this.cDefinition = (PieceColor == PieceColor.WHITE) ? Character.toUpperCase(cDefinition) : Character.toLowerCase(cDefinition);
        this.PieceColor = PieceColor;
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

    public PieceColor getColor() {
        return PieceColor;
    }

    public Position getPosition() {
        return pPos;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return this.getDefinition() + String.valueOf(pPos.getColumn()) + pPos.getRow();
    }

    protected boolean isPossibleMove(Position pos) {
        return pos.getColumn() >= 'a' && pos.getColumn() <= 'h' && pos.getRow() >= 1 && pos.getRow() <= 8;
    }
}
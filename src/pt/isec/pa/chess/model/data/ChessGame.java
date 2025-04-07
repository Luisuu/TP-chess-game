package pt.isec.pa.chess.model.data;

import pt.isec.pa.chess.model.data.pieces.Color;
import pt.isec.pa.chess.model.data.pieces.Piece;
import pt.isec.pa.chess.model.data.pieces.Position;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

public class ChessGame implements Serializable {
    @Serial
    static final long serialVersionUID = 1L;

    Board board;
    Color cCurrentPlayer = Color.WHITE;
    Color cWinner = null;

    public String debug(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n--------------GAME DEBUG INFO------------------\n");
        sb.append("Current player: " + cCurrentPlayer);
        sb.append("\n");
        sb.append(board.debug());
        sb.append("\n");
        sb.append("Winner: " + cWinner);
        sb.append("\n");
        return sb.toString();
    }

    public void start() {
        board = new Board();
        board.initializeBoard();
    }

    public String printPieces() {
        return board.printPieces();
    }

    public Color getcCurrentPlayer() {
        return cCurrentPlayer;
    }

    public Color getcOpponent() {
        return cCurrentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    public void movePiece(Position position, Position newPosition) {
        if (isGameOver()) return;
        Piece pMover = board.getPiece(position);
        if (pMover != null && pMover.getPossibleMoves().contains(newPosition) && pMover.getColor() == getcCurrentPlayer()) {
            pMover.setPosition(newPosition);
            cCurrentPlayer = getcOpponent();
            isGameOver();
        }
    }

    public Color getWinner() {
        return cWinner;
    }

    public boolean isGameOver() {
        boolean bWhiteKing = false;
        boolean bBlackKing = false;

        for (Piece p : board.getPieces()) {
            if (Character.toUpperCase(p.getDefinition()) == 'K') {
                if (p.getColor() == Color.WHITE) {
                    bWhiteKing = true;
                } else if (p.getColor() == Color.BLACK) {
                    bBlackKing = true;
                }
            }
        }

        if (!bWhiteKing) {
            cWinner = Color.BLACK;
            return true;
        } else if (!bBlackKing) {
            cWinner = Color.WHITE;
            return true;
        }
        return false;
    }
}
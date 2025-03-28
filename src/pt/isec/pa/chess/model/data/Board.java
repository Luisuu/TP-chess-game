package pt.isec.pa.chess.model.data;
import pt.isec.pa.chess.model.data.pieces.Piece;

import java.util.HashMap;
import java.util.Map;


public class Board {
    private Map<String, Piece> mBoard;

    public Board() {
        mBoard = new HashMap<>();
        initiazeBoard();
    }

    private void initiazeBoard() {
        for (char col = 'a'; col <= 'h'; col++) {
            for (int row = 1; row <= 8; row++) {
                String pos = "" + col + row;
                mBoard.put(pos, null);
            }
        }
    }

    public void addPiece(String position, Piece piece) {
        mBoard.put(position, piece);
    }

    public void removePiece(String position) {
        mBoard.remove(position);
    }

    public Piece getPiece(String position) {
        return mBoard.get(position);
    }

}

package pt.isec.pa.chess.model.memento;

import pt.isec.pa.chess.model.data.pieces.PieceColor;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class MyMemento implements IMemento {
    private final String boardState;
    private final PieceColor currentPlayer;
    private byte[] snapshot;

    MyMemento(MyOriginator base) {
        this.boardState = base.data.getBoardPiecesString();
        this.currentPlayer = base.data.getcCurrentPlayer();
    }

    public String getBoardState() {
        return boardState;
    }

    public PieceColor getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public Object getSnapshot() {
        if (snapshot == null) return null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(snapshot); ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

}




package pt.isec.pa.chess.model.memento;

import pt.isec.pa.chess.model.ChessGame;
import pt.isec.pa.chess.model.data.pieces.PieceColor;

import java.io.Serializable;

public class MyOriginator implements Serializable, IOriginator {
    private ChessGame data;

    public MyOriginator(ChessGame data) {
        this.data = data;
    }

    private static class MyMemento implements IMemento {
        private final String boardState;
        private final PieceColor currentPlayer;

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
    }

    @Override
    public IMemento save() {
        return new MyMemento(this);
    }

    @Override
    public void restore(IMemento memento) {
        if (memento instanceof MyMemento m) {
            data.loadBoardStateFromString(m.getBoardState(), m.getCurrentPlayer());
        }
    }
}
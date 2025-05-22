package pt.isec.pa.chess.model.memento;

import pt.isec.pa.chess.model.ChessGame;

import java.io.Serializable;

public class MyOriginator implements Serializable, IOriginator {
    public ChessGame data;

    public MyOriginator(ChessGame data) {
        this.data = data;
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
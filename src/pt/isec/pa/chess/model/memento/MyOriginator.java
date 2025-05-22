package pt.isec.pa.chess.model.memento;

import pt.isec.pa.chess.model.ChessGame;

import java.io.Serializable;

class MyOriginator implements IOriginator {
    ChessGame data;

    private static class MyMemento implements IMemento {
        ChessGame data;

        MyMemento(MyOriginator base) throws CloneNotSupportedException {
            this.data = (ChessGame) base.data.clone();
        }
    }

    @Override
    public IMemento save() throws CloneNotSupportedException {
        return new MyMemento(this);
    }

    @Override
    public void restore(IMemento memento) {
        if (memento instanceof MyMemento m) data = m.data;
    }
}

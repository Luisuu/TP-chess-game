package pt.isec.pa.chess.model.memento;

import java.io.Serializable;

class MyOriginator implements Serializable, IOriginator {
    MyObject data;

    @Override
    public IMemento save() {
        return new Memento(this);
    }

    @Override
    public void restore(IMemento memento) {
        Object obj = memento.getSnapshot();
        if (obj instanceof MyOriginator m) data = m.data;
    }
}
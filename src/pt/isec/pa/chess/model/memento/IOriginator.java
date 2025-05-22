package pt.isec.pa.chess.model.memento;

public interface IOriginator {
    IMemento save() throws CloneNotSupportedException;

    void restore(IMemento memento);
}
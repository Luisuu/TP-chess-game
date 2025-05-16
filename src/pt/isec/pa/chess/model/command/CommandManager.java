package pt.isec.pa.chess.model.command;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {
    private final Deque<ICommand> history = new ArrayDeque<>();
    private final Deque<ICommand> redoCmds = new ArrayDeque<>();

    public boolean invokeCommand(ICommand cmd) {
        redoCmds.clear();
        if (cmd.execute()) {
            history.push(cmd);
            return true;
        }
        history.clear();
        return false;
    }

    public boolean undo() {
        if (history.isEmpty())
            return false;
        ICommand cmd = history.pop();
        cmd.undo();
        redoCmds.push(cmd);
        return true;
    }

    public boolean redo() {
        if (redoCmds.isEmpty())
            return false;
        ICommand cmd = redoCmds.pop();
        cmd.execute();
        history.push(cmd);
        return true;
    }

    public boolean hasUndo() {
        return !history.isEmpty();
    }

    public boolean hasRedo() {
        return !redoCmds.isEmpty();
    }
}

package pt.isec.pa.chess.model.command;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {
    private Deque<ICommand> history;
    private Deque<ICommand> redoCmds;

    public CommandManager(){
        history = new ArrayDeque<>();
        redoCmds = new ArrayDeque<>();
    }

    public boolean invokeCommand(ICommand cmd) {
        redoCmds.clear();
        if(cmd.execute()){
            System.out.println("@1");
            history.push(cmd);
            return true;
        }
        System.out.println("@2");
        history.clear();
        return false;
    }

    public boolean undo(){
        System.out.println("asdsaasd");
        if(history.isEmpty()){
            return false;
        }
        ICommand cmd = history.pop();
        cmd.undo();
        redoCmds.push(cmd);
        return true;
    }

    public boolean redo(){
        if(redoCmds.isEmpty()){
            return false;
        }
        ICommand cmd = redoCmds.pop();
        cmd.execute();
        history.push(cmd);
        return true;
    }

    public boolean hasUndo(){
        return history.size() > 0;
    }

    public boolean hasRedo(){
        return redoCmds.size() > 0;
    }
}

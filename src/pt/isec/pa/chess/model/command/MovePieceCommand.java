package pt.isec.pa.chess.model.command;

import pt.isec.pa.chess.model.ChessGame;

public abstract class MovePieceCommand extends AbstractCommand {
    private final String from, to;

    public MovePieceCommand(ChessGame receiver, String from, String to) {
        super(receiver);
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean execute() {
        return receiver.movePiece(from, to);
    }

    @Override
    public boolean undo() {
        return receiver.movePiece(to, from);
    }
}

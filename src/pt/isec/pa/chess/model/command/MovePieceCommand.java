package pt.isec.pa.chess.model.command;

import pt.isec.pa.chess.model.ChessGame;

public class MovePieceCommand extends AbstractCommand {
    private final String from, to;

    public MovePieceCommand(ChessGame receiver, String from, String to) {
        super(receiver);
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean execute() {
        receiver.movePiece(from, to);
        return true;
    }

    @Override
    public boolean undo() {
        System.out.println(receiver.getBoardPiecesString());
        receiver.movePiece(to, from);
        return true;
    }
}

package pt.isec.pa.chess.model.data;

public class ChessGame {
    Board board;
    public void start(){
        board = new Board();
        board.initializeBoard();
    }

    public String printPieces(){
        return board.printPieces();
    }
}

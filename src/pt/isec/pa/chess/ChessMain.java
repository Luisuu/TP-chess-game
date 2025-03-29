package pt.isec.pa.chess;

import pt.isec.pa.chess.model.data.Board;

public class ChessMain {
    public static void main(String[] args) {
        System.out.println("PA Chess Game");
        Board b = new Board();
        b.initializeBoard();
        System.out.println(b.printPieces());
        System.out.println(b.debug());

        /*System.out.println((char)('h'-3));
        System.out.println((int)'d'-96);*/
    }
}

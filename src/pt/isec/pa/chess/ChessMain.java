package pt.isec.pa.chess;

import pt.isec.pa.chess.model.data.ChessGame;
import pt.isec.pa.chess.model.data.ChessGameSerialization;

public class ChessMain {
    public static void main(String[] args) {

        System.out.println("PA Chess Game");

        ChessGame game = new ChessGame();
        game.start();

        System.out.println(game.printPieces());
        System.out.println(game.debug());

        game.exporta();

        //ver se uma determinada casa esta em check
        //o chessgame depois pode saber onde esta o rei e ir pedir ao board para verificar se aquela casa esta em check
        //otimizar pecas e remover cenas repetidas

        //System.out.println(b.debug());

        /*System.out.println((char)('h'-3));
        System.out.println((int)'d'-96);*/
    }
}
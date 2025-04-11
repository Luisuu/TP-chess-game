package pt.isec.pa.chess;

import javafx.application.Application;
import pt.isec.pa.chess.model.data.ChessGame;
import pt.isec.pa.chess.model.data.ChessGameSerialization;
import pt.isec.pa.chess.model.data.pieces.Position;
import pt.isec.pa.chess.ui.MainJFX;

public class ChessMain {
    public static void main(String[] args) {

        Application.launch(MainJFX.class,args);

        System.out.println("PA Chess Game");

        ChessGame game = new ChessGame();
        game.start();

        System.out.println(game.printPieces());
        System.out.println(game.debug());

        /*game.exporta("Test.txt");

        game.importa("test.txt");
        System.out.println(game.debug());

        /*TESTES, SETA PLAYER INICIAL PARA PRETO
        System.out.println("ESTADO INICIAL:");

        game.importa("chessGame.txt"); //BLACK,Pc2,pg7,Ra1*,Pa2,ng8,pe7,ke8*,pc7,bc8,pa7,ra8*,Rh1*,Ph2,Bf1,Pf2,Qd1,Pd2,ph7,Nb1,Pb2,rh8*,pf7,bf8,pd7,qd8,pb7,nb8,Ng1,Pg2,Ke1*,Pe2,Bc1

        System.out.println(game.printPieces());
        System.out.println(game.debug());

        System.out.println("TENTA MOVER PECA BRANCA");
        game.movePiece(new Position('c', 2), new Position('c', 3));

        System.out.println(game.printPieces());
        System.out.println(game.debug());

        System.out.println("TENTA MOVER PECA PRETA");
        game.movePiece(new Position('g', 7), new Position('g', 5));

        System.out.println("ESTADO FINAL:");

        System.out.println(game.printPieces());
        System.out.println(game.debug());

        game.exporta("Test.txt");

        //ver se uma determinada casa esta em check
        //o chessgame depois pode saber onde esta o rei e ir pedir ao board para verificar se aquela casa esta em check
        //otimizar pecas e remover cenas repetidas

        //System.out.println(b.debug());

        /*System.out.println((char)('h'-3));
        System.out.println((int)'d'-96);*/
    }
}
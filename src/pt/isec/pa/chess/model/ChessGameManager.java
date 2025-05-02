package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.pieces.*;

import java.io.*;
import java.util.Arrays;

public class ChessGameManager {
    ChessGame cgGame;


    //listener pawnpromotion
    public interface PromotionListener {
        void onPromote(Position position);
    }

    private transient PromotionListener promotionListener;

    public ChessGameManager() {
        cgGame = new ChessGame();
        //System.out.println("ChessGameManager");
    }

    public void setPromotionListener(PromotionListener listener) {
        this.promotionListener = listener;
    }

    public ChessGame getChessGame() {
        return cgGame;
    }

    public void save(String sFileName) {
        ChessGameSerialization.save(sFileName, cgGame);
    }

    public void importa(String sFileName) {
        cgGame.board.clearBoard();
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(sFileName)))) {
            byte[] data = dis.readAllBytes();

            String line = new String(data).trim();

            if (line.isEmpty()) { //fich vazio
                return;
            }

            String[] tokens = line.split(",");
            if (tokens.length < 2) {//invalido
                return;
            }

            PieceColor Color;
            try {
                Color = PieceColor.valueOf(tokens[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("cor " + tokens[0] + " invalida");
            }

            for (int i = 1; i < tokens.length; i++) {
                String token = tokens[i].trim();

                if (!token.matches("(?i)[kqrbnp][a-h][1-8]\\*?")) {
                    throw new RuntimeException("peca " + token + " invalida");
                }
            }

            cgGame.cCurrentPlayer = Color;
            cgGame.board = new Board();

            cgGame.board.initializeBoard(Arrays.copyOfRange(tokens, 1, tokens.length));

            for (Piece piece : cgGame.board.getPieces()) {
                piece.setBoard(cgGame.board);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exporta(String sFileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(cgGame.cCurrentPlayer).append(",");
        sb.append(cgGame.printPieces());

        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(sFileName)))) {
            dos.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open(String sFileName) {
        ChessGame loaded = ChessGameSerialization.load(sFileName);

        if (loaded != null) {
            cgGame.board = loaded.board;
            cgGame.cCurrentPlayer = loaded.cCurrentPlayer;
            cgGame.cWinner = loaded.cWinner;

            for (Piece piece : cgGame.board.getPieces()) {
                piece.setBoard(cgGame.board);
            }

            //System.out.println("Partie restaurer");
        } else {
            cgGame.board = new Board();
            cgGame.board.initializeBoard();

            for (Piece piece : cgGame.board.getPieces()) {
                piece.setBoard(cgGame.board);
            }

            cgGame.cCurrentPlayer = PieceColor.WHITE;
            cgGame.cWinner = null;

            //System.out.println("STARTTTT");
        }
    }

    public void restart() {
        cgGame.resetGame();
    }

    public String debug() {
        StringBuilder sb = new StringBuilder();
        sb.append(cgGame.sPlayer1).append(",");
        sb.append(cgGame.sPlayer2).append(",");
        sb.append(cgGame.cCurrentPlayer).append(",");
        sb.append(cgGame.printPieces());
        return sb.toString();
    }

    public void setPlayerName(int player, String name) {
        cgGame.setPlayerName(player, name);
    }

    private void removePiece(Position pos) {
        cgGame.board.removePiece(pos);
    }

    public void movePiece(Position oldPos, Position newPos) {
        if (cgGame.isGameOver()) return;

        Board board = cgGame.board;
        Piece pMover = cgGame.board.getPiece(oldPos);

        if (pMover != null && pMover.getPossibleMoves().contains(newPos) && pMover.getColor() == cgGame.getcCurrentPlayer()) {
            pMover.setPosition(newPos);

            Piece target = board.getPiece(newPos);
            if (target != null && target.getColor() != pMover.getColor()) {
                removePiece(newPos);
            }

            removePiece(oldPos);
            pMover.setPosition(newPos);
            board.addPiece(newPos, pMover);

            // TEMOS DE TIRAR INSTANCEOF URGENTE
            // IMPORTANTE

            //verificacao pawnpromotion
            if (pMover instanceof Pawn pawn && pawn.isPromotionPosition()) {
                if (promotionListener != null) {
                    promotionListener.onPromote(newPos); // Notifica a UI
                }
            }

            if (pMover instanceof Rook) {
                ((Rook) pMover).setHasMoved(true);
            } else if (pMover instanceof King) {
                ((King) pMover).setHasMoved(true);
            } else if (pMover instanceof Pawn) {
                ((Pawn) pMover).setHasMoved(true);
            }

            cgGame.cCurrentPlayer = cgGame.getcOpponent();
            cgGame.isGameOver();
        }
    }


    public void promotePawn(Position position, char pieceType) {
        cgGame.promotePawn(position, pieceType);
    }
}

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


    public void save(String sFileName) {
        try {
            cgGame.save(sFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importa(String sFileName) {
        try {
            cgGame.importa(sFileName);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void exporta(String sFileName) {
        try {
            cgGame.exporta(sFileName);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void open(String sFileName) {
        try {
            cgGame.open(sFileName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            cgGame.restart();
        }
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

    public void restart() {
        cgGame.restart();
    }

    public void setPlayerName(int player, String name) {
        cgGame.setPlayerName(player, name);
    }


    public String getPossibleMovesFromPieceAt(Position pos) {
        Piece pPiece = cgGame.board.getPiece(pos);
        if (pPiece != null) {
            return pPiece.getPossibleMoves().toString();
        }
        return null;
    }

    public String getBoardPiecesString() {
        return cgGame.printPieces();
    }

    public String getCurrentPlayerName() {
        return cgGame.getcCurrentPlayer().toString();
    }

    public String debug() {
        StringBuilder sb = new StringBuilder();
        sb.append(cgGame.sPlayer1).append(",");
        sb.append(cgGame.sPlayer2).append(",");
        sb.append(cgGame.cCurrentPlayer).append(",");
        sb.append(cgGame.printPieces());
        return sb.toString();
    }

    public int getBoardCols() {
        try {
            return cgGame.getBoardSize();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getBoardRows() {
        try {
            return cgGame.getBoardSize();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return -1;
    }


    private void removePiece(Position pos) {
        cgGame.board.removePiece(pos);
    }


}

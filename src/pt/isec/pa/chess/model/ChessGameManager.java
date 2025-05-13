package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.pieces.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.Set;

public class ChessGameManager {
    ChessGame cgGame;
    private PropertyChangeSupport pcs;

    //listener pawnpromotion
    public interface PromotionListener {
        void onPromote(Position position);
    }

    private transient PromotionListener promotionListener;

    public ChessGameManager() {
        cgGame = new ChessGame();
        pcs = new PropertyChangeSupport(this);
        //System.out.println("ChessGameManager");
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public void addProperyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
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
        try {
            boolean needsPromotion = cgGame.movePiece(oldPos, newPos);
            if (needsPromotion && promotionListener != null) promotionListener.onPromote(newPos);
        } catch (Exception e) {
            System.err.println("Error moving a piece: " + e.getMessage());
        }
    }

    public void promotePawn(Position position, char pieceType) {
        try {
            cgGame.promotePawn(position, pieceType);
        } catch (IllegalArgumentException e) {
            System.err.println("Error on promotion: " + e.getMessage());
        }
    }

    public void restart() {
        try {
            cgGame.restart();
        } catch (Exception e) {
            System.err.println("Error restarting the game: " + e.getMessage());
        }
    }

    public void setPlayerName(int player, String name) {
        try {
            cgGame.setPlayerName(player, name);
        } catch (IllegalArgumentException e) {
            System.err.println("Error setting a name: " + e.getMessage());
        }
    }

    public String getPlayerName(PieceColor eColor) {
        try {
            return cgGame.getPlayerName(eColor);
        } catch (IllegalArgumentException e) {
            System.err.println("Error getting a name: " + e.getMessage());
        }
        return null;
    }

    public String getPossibleMovesFromPieceAt(Position pos) {
        try {
            Set<Position> moves = cgGame.getPossibleMovesFromPieceAt(pos);
            return moves.toString();
        } catch (IllegalArgumentException e) {
            System.err.println("Error getting movements: " + e.getMessage());
            return "[]";
        }
    }

    public String getBoardPiecesString() {
        try {
            return cgGame.getBoardPiecesString();
        } catch (Exception e) {
            System.err.println("Error getting pieces: " + e.getMessage());
            return "";
        }
    }

    public PieceColor getcCurrentPlayer() {
        try {
            return cgGame.getcCurrentPlayer();
        } catch (Exception e) {
            System.err.println("Error getting current player: " + e.getMessage());
            return null;
        }
    }
    
    public boolean bIsCheckmate(PieceColor color) {
        try {
            return cgGame.bIsCheckmate(color);
        } catch (Exception e) {
            System.err.println("Error checking checkmate: " + e.getMessage());
            return false;
        }
    }

    public PieceColor getWinner() {
        try {
            return cgGame.getWinner();
        } catch (Exception e) {
            System.err.println("Error getting winner: " + e.getMessage());
            return null;
        }
    }

    public boolean isGameOver() {
        try {
            return cgGame.isGameOver();
        } catch (Exception e) {
            System.err.println("Error checking game over status: " + e.getMessage());
            return false;
        }
    }


    public String debug() {
        StringBuilder sb = new StringBuilder();
        sb.append(cgGame.sPlayer1).append(",");
        sb.append(cgGame.sPlayer2).append(",");
        sb.append(cgGame.cCurrentPlayer).append(",");
        sb.append(cgGame.getBoardPiecesString());
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
        try {
            cgGame.removePiece(pos);
        } catch (IllegalArgumentException e) {
            System.err.println("Error removing pieces: " + e.getMessage());
        }
    }
}

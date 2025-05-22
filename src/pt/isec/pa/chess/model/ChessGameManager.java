package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.pieces.PieceColor;
import pt.isec.pa.chess.model.memento.CareTaker;
import pt.isec.pa.chess.model.memento.MyOriginator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.Set;

public class ChessGameManager {
    public static final String PROP_BOARD_UPDATE = "boardUpdate";
    public static final String PROP_GAME_STATE = "gameState";
    private String oldGameState = null;

    ChessGame cgGame;
    private CareTaker careTaker;
    private PropertyChangeSupport pcs;

    public interface PromotionListener {
        void onPromote(String position);
    }

    private transient PromotionListener promotionListener;

    public ChessGameManager() {
        cgGame = new ChessGame();
        careTaker = new CareTaker(new MyOriginator(cgGame));
        pcs = new PropertyChangeSupport(this);
        //System.out.println("ChessGameManager");
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public void save(String sFileName) {
        try {
            cgGame.save(sFileName);
        } catch (IOException e) {
            ModelLog.getInstance().log("Error saving game: " + e.getMessage());
        }
    }

    public void importa(String sFileName) {
        try {
            cgGame.importa(sFileName);
            oldGameState = cgGame.getBoardPiecesString();
            pcs.firePropertyChange(PROP_BOARD_UPDATE, oldGameState, cgGame.getBoardPiecesString());
            pcs.firePropertyChange(PROP_GAME_STATE, null, null);
            careTaker.save();
        } catch (IOException | RuntimeException e) {
            ModelLog.getInstance().log("Error importing game: " + e.getMessage());
        }
    }

    public void exporta(String sFileName) {
        try {
            cgGame.exporta(sFileName);
        } catch (IOException | RuntimeException e) {
            ModelLog.getInstance().log("Error exporting game: " + e.getMessage());
        }
    }

    public void open(String sFileName) {
        try {
            cgGame.open(sFileName);
            oldGameState = cgGame.getBoardPiecesString();
            pcs.firePropertyChange(PROP_BOARD_UPDATE, oldGameState, cgGame.getBoardPiecesString());
            pcs.firePropertyChange(PROP_GAME_STATE, null, null);
            careTaker.save();
        } catch (IOException | ClassNotFoundException e) {
            ModelLog.getInstance().log("Error opening game: " + e.getMessage());
            cgGame.restart();
            pcs.firePropertyChange(PROP_BOARD_UPDATE, oldGameState, cgGame.getBoardPiecesString());
            pcs.firePropertyChange(PROP_GAME_STATE, null, null);
        }
    }

    public boolean movePiece(String SoldPos, String SnewPos) {
        try {
            oldGameState = cgGame.getBoardPiecesString();
            boolean needsPromotion = cgGame.movePiece(SoldPos, SnewPos);
            // fixme
            //if (needsPromotion && promotionListener != null) promotionListener.onPromote(SnewPos);
            pcs.firePropertyChange(PROP_BOARD_UPDATE, oldGameState, cgGame.getBoardPiecesString());
            pcs.firePropertyChange(PROP_GAME_STATE, null, null);
            careTaker.save();
            return needsPromotion;
        } catch (IllegalArgumentException ex) {
            ModelLog.getInstance().log("Error: " + ex.getMessage());
            return false;
        }
    }

    public void promotePawn(String position, char pieceType) {
        try {
            oldGameState = cgGame.getBoardPiecesString();
            cgGame.promotePawn(position, pieceType);
            pcs.firePropertyChange(PROP_BOARD_UPDATE, oldGameState, cgGame.getBoardPiecesString());
            pcs.firePropertyChange(PROP_GAME_STATE, null, null);
            careTaker.save();
        } catch (IllegalArgumentException ex) {
            ModelLog.getInstance().log("Error promoting pawn: " + ex.getMessage());
        }
    }

    public void restart() {
        try {
            cgGame.restart();
            careTaker.reset();
            pcs.firePropertyChange(PROP_BOARD_UPDATE, null, cgGame.getBoardPiecesString());
            pcs.firePropertyChange(PROP_GAME_STATE, null, null);
        } catch (Exception ex) {
            ModelLog.getInstance().log("Error restarting game: " + ex.getMessage());
        }
    }

    public void setPlayerName(int player, String name) {
        try {
            cgGame.setPlayerName(player, name);
            pcs.firePropertyChange(PROP_GAME_STATE, null, null);
        } catch (IllegalArgumentException ex) {
            ModelLog.getInstance().log("Error setting player name:: " + ex.getMessage());
        }
    }

    public String getPlayerName(PieceColor eColor) {
        try {
            return cgGame.getPlayerName(eColor);
        } catch (IllegalArgumentException ex) {
            ModelLog.getInstance().log("Error getting player name: " + ex.getMessage());
        }
        return null;
    }

    public String getPossibleMovesFromPieceAt(String pos) {
        try {
            Set<String> moves = cgGame.getPossibleMovesFromPieceAt(pos);
            return moves.toString();
        } catch (IllegalArgumentException ex) {
            ModelLog.getInstance().log("Error getting possible moves: " + ex.getMessage());
            return "[]";
        }
    }

    public String getBoardPiecesString() {
        try {
            return cgGame.getBoardPiecesString();
        } catch (Exception ex) {
            ModelLog.getInstance().log("Error getting board pieces: " + ex.getMessage());
            return "";
        }
    }

    public PieceColor getcCurrentPlayer() {
        try {
            return cgGame.getcCurrentPlayer();
        } catch (Exception ex) {
            ModelLog.getInstance().log("Error getting current player: " + ex.getMessage());
            return null;
        }
    }

    public boolean bIsCheckmate(PieceColor color) {
        try {
            return cgGame.bIsCheckmate(color);
        } catch (Exception ex) {
            ModelLog.getInstance().log("Error checking checkmate: " + ex.getMessage());
            return false;
        }
    }

    public PieceColor getWinner() {
        try {
            return cgGame.getWinner();
        } catch (Exception ex) {
            ModelLog.getInstance().log("Error getting winner: " + ex.getMessage());
            return null;
        }
    }

    public boolean isGameOver() {
        try {
            return cgGame.isGameOver();
        } catch (Exception ex) {
            ModelLog.getInstance().log("Error checking game over: " + ex.getMessage());
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
            ModelLog.getInstance().log("Error getting board columns: " + e.getMessage());
        }
        return -1;
    }

    public int getBoardRows() {
        try {
            return cgGame.getBoardSize();
        } catch (RuntimeException e) {
            ModelLog.getInstance().log("Error getting board rows: " + e.getMessage());
        }
        return -1;
    }

    public boolean undo() {
        careTaker.undo();
        pcs.firePropertyChange(PROP_BOARD_UPDATE, null, cgGame.getBoardPiecesString());
        pcs.firePropertyChange(PROP_GAME_STATE, null, null);
        return true;
    }

    public boolean redo() {
        careTaker.redo();
        pcs.firePropertyChange(PROP_BOARD_UPDATE, null, cgGame.getBoardPiecesString());
        pcs.firePropertyChange(PROP_GAME_STATE, null, null);
        return true;
    }

    public boolean hasUndo() {
        return careTaker.hasUndo();
    }

    public boolean hasRedo() {
        return careTaker.hasRedo();
    }
}

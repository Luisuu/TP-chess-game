package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.command.CommandManager;
import pt.isec.pa.chess.model.command.MovePieceCommand;
import pt.isec.pa.chess.model.data.pieces.PieceColor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.Set;

public class ChessGameManager {
    public static final String PROP_BOARD_UPDATE = "boardUpdate";
    public static final String PROP_GAME_STATE = "gameState";
    private String oldGameState = null;

    ChessGame cgGame;
    CommandManager cm;
    private PropertyChangeSupport pcs;

    public interface PromotionListener {
        void onPromote(String position);
    }

    private transient PromotionListener promotionListener;

    public ChessGameManager() {
        cgGame = new ChessGame();
        cm = new CommandManager();
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
            e.printStackTrace();
        }
    }

    public void importa(String sFileName) {
        try {
            cgGame.importa(sFileName);
            oldGameState = cgGame.getBoardPiecesString();
            pcs.firePropertyChange(PROP_BOARD_UPDATE, oldGameState, cgGame.getBoardPiecesString());
            pcs.firePropertyChange(PROP_GAME_STATE, null, null);
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
            oldGameState = cgGame.getBoardPiecesString();
            pcs.firePropertyChange(PROP_BOARD_UPDATE, oldGameState, cgGame.getBoardPiecesString());
            pcs.firePropertyChange(PROP_GAME_STATE, null, null);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            cgGame.restart();
            pcs.firePropertyChange(PROP_BOARD_UPDATE, oldGameState, cgGame.getBoardPiecesString());
            pcs.firePropertyChange(PROP_GAME_STATE, null, null);
        }
    }

    public boolean movePiece(String SoldPos, String SnewPos) {
        try {
            oldGameState = cgGame.getBoardPiecesString();
            //boolean needsPromotion = cgGame.movePiece(SoldPos, SnewPos);
            boolean xd = cm.invokeCommand(new MovePieceCommand(cgGame, SoldPos, SnewPos));
            // fixme
            //if (needsPromotion && promotionListener != null) promotionListener.onPromote(SnewPos);
            pcs.firePropertyChange(PROP_BOARD_UPDATE, oldGameState, cgGame.getBoardPiecesString());
            pcs.firePropertyChange(PROP_GAME_STATE, null, null);
            return xd;
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
        } catch (IllegalArgumentException ex) {
            ModelLog.getInstance().log("Error: " + ex.getMessage());
        }
    }

    public void restart() {
        try {
            cgGame.restart();
            pcs.firePropertyChange(PROP_BOARD_UPDATE, null, cgGame.getBoardPiecesString());
            pcs.firePropertyChange(PROP_GAME_STATE, null, null);
        } catch (Exception ex) {
            ModelLog.getInstance().log("Error: " + ex.getMessage());
        }
    }

    public void setPlayerName(int player, String name) {
        try {
            cgGame.setPlayerName(player, name);
            pcs.firePropertyChange(PROP_GAME_STATE, null, null);
        } catch (IllegalArgumentException ex) {
            ModelLog.getInstance().log("Error: " + ex.getMessage());
        }
    }

    public String getPlayerName(PieceColor eColor) {
        try {
            return cgGame.getPlayerName(eColor);
        } catch (IllegalArgumentException ex) {
            ModelLog.getInstance().log("Error: " + ex.getMessage());
        }
        return null;
    }

    public String getPossibleMovesFromPieceAt(String pos) {
        try {
            Set<String> moves = cgGame.getPossibleMovesFromPieceAt(pos);
            return moves.toString();
        } catch (IllegalArgumentException ex) {
            ModelLog.getInstance().log("Error: " + ex.getMessage());
            return "[]";
        }
    }

    public String getBoardPiecesString() {
        try {
            return cgGame.getBoardPiecesString();
        } catch (Exception ex) {
            ModelLog.getInstance().log("Error: " + ex.getMessage());
            return "";
        }
    }

    public PieceColor getcCurrentPlayer() {
        try {
            return cgGame.getcCurrentPlayer();
        } catch (Exception ex) {
            ModelLog.getInstance().log("Error: " + ex.getMessage());
            return null;
        }
    }

    public boolean bIsCheckmate(PieceColor color) {
        try {
            return cgGame.bIsCheckmate(color);
        } catch (Exception ex) {
            ModelLog.getInstance().log("Error: " + ex.getMessage());
            return false;
        }
    }

    public PieceColor getWinner() {
        try {
            return cgGame.getWinner();
        } catch (Exception ex) {
            ModelLog.getInstance().log("Error: " + ex.getMessage());
            return null;
        }
    }

    public boolean isGameOver() {
        try {
            return cgGame.isGameOver();
        } catch (Exception ex) {
            ModelLog.getInstance().log("Error: " + ex.getMessage());
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

    public boolean undo() {
        boolean ok = cm.undo();
        if (ok) pcs.firePropertyChange(PROP_BOARD_UPDATE, null, cgGame.getBoardPiecesString());
        return ok;
    }

    public boolean redo() {
        boolean ok = cm.redo();
        if (ok) pcs.firePropertyChange(PROP_BOARD_UPDATE, null, cgGame.getBoardPiecesString());
        return ok;
    }

    public boolean hasUndo() {
        return cm.hasUndo();
    }

    public boolean hasRedo() {
        return cm.hasRedo();
    }
}

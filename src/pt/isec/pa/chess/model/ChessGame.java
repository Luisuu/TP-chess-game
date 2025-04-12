package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.pieces.*;

import java.io.*;
import java.util.Arrays;

public class ChessGame implements Serializable {
    @Serial
    static final long serialVersionUID = 1L;

    Board board;
    Color cCurrentPlayer = Color.WHITE;
    Color cWinner = null;

    public String debug() {
        //
        StringBuilder sb = new StringBuilder();
        sb.append("\n--------------GAME DEBUG INFO------------------\n");
        sb.append("Current player: " + cCurrentPlayer);
        sb.append("\n");
        sb.append(board.debug());
        sb.append("\n");
        sb.append("Winner: " + cWinner);
        sb.append("\n");
        return sb.toString();
    }

    public void start() {
        board = new Board();
        board.clearBoard();
        board.initializeBoard();
    }

    public void save(String sFileName) {
        ChessGameSerialization.save(sFileName, this);
    }

    public void importa(String sFileName) {
        board.clearBoard();
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(sFileName)))) {
            byte[] data = dis.readAllBytes();

            String line = new String(data).trim();

            if (line.isEmpty()) {
                //ficheiro vazio
                return;
            }

            String[] tokens = line.split(",");
            if (tokens.length < 2) {
                // formato inmvalido
                return;
            }

            Color color;
            try {
                color = Color.valueOf(tokens[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("cor " + tokens[0] + " invalida");
            }

            for (int i = 1; i < tokens.length; i++) {
                String token = tokens[i].trim();

                if (!token.matches("(?i)[kqrbnp][a-h][1-8]\\*?")) {
                    throw new RuntimeException("peca " + token + " invalida");
                }
            }

            this.cCurrentPlayer = color;
            this.board = new Board();

            board.initializeBoard(Arrays.copyOfRange(tokens, 1, tokens.length));

            for (Piece piece : board.getPieces()) {
                piece.setBoard(board);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exporta(String sFileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(cCurrentPlayer).append(",");
        sb.append(printPieces());

        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(sFileName)))) {
            dos.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open(String sFileName) {
        //FALTA GENERALIZAR O PATH
        ChessGame loaded = ChessGameSerialization.load(sFileName);

        if (loaded != null) {
            this.board = loaded.board;
            this.cCurrentPlayer = loaded.cCurrentPlayer;
            this.cWinner = loaded.cWinner;

            for (Piece piece : board.getPieces()) {
                piece.setBoard(board);
            }

            //System.out.println("Partie restaurer");
        } else {
            this.board = new Board();
            board.initializeBoard();

            for (Piece piece : board.getPieces()) {
                piece.setBoard(board);
            }

            this.cCurrentPlayer = Color.WHITE;
            this.cWinner = null;

            //System.out.println("STARTTTT");
        }
    }


    public String printPieces() {
        return board.printPieces();
    }

    public Color getcCurrentPlayer() {
        return cCurrentPlayer;
    }

    public Color getcOpponent() {
        return cCurrentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    public void movePiece(Position position, Position newPosition) {
        if (isGameOver()) return;
        Piece pMover = board.getPiece(position);
        if (pMover != null && pMover.getPossibleMoves().contains(newPosition) && pMover.getColor() == getcCurrentPlayer()) {
            pMover.setPosition(newPosition);

            if (pMover instanceof Rook) {
                ((Rook) pMover).setHasMoved(true);
            } else if (pMover instanceof King) {
                ((King) pMover).setHasMoved(true);
            } else if (pMover instanceof Pawn) {
                ((Pawn) pMover).setHasMoved(true);
            }

            cCurrentPlayer = getcOpponent();
            isGameOver();
        }
    }

    public Color getWinner() {
        return cWinner;
    }

    public boolean isGameOver() {
        boolean bWhiteKing = false;
        boolean bBlackKing = false;

        for (Piece p : board.getPieces()) {
            if (Character.toUpperCase(p.getDefinition()) == 'K') {
                if (p.getColor() == Color.WHITE) {
                    bWhiteKing = true;
                } else if (p.getColor() == Color.BLACK) {
                    bBlackKing = true;
                }
            }
        }

        if (!bWhiteKing) {
            cWinner = Color.BLACK;
            return true;
        } else if (!bBlackKing) {
            cWinner = Color.WHITE;
            return true;
        }
        return false;
    }
}
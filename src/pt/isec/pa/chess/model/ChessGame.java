package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.pieces.*;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ChessGame implements Serializable {
    @Serial
    static final long serialVersionUID = 1L;

    Board board;
    PieceColor cCurrentPlayer = PieceColor.WHITE;
    PieceColor cWinner = null;
    String sPlayer1 = null, sPlayer2 = null;

    public ChessGame() {
        board = new Board();
        board.clearBoard();
        board.initializeBoard();
    }

    public int getBoardSize() {
        return board.getBoardSize();
    }

    public void setPlayerName(int player, String name) {
        if (player < 1 || player > 2) {
            throw new RuntimeException("tried to set invalid player");
            //System.out.println("Invalid player number");
            //return;
        }
        if (player == 1) {
            sPlayer1 = name;
        } else {
            sPlayer2 = name;
        }
    }

    public String getPlayerName(PieceColor eColor) {
        if (eColor == PieceColor.WHITE) {
            return sPlayer1;
        } else {
            return sPlayer2;
        }
    }

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
        board.clearBoard();
        board.initializeBoard();
    }

    public String getBoardPiecesString() {
        return board.printPieces();
    }

    public PieceColor getcCurrentPlayer() {
        return cCurrentPlayer;
    }


    public PieceColor getcOpponent() {
        return cCurrentPlayer == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
    }


    public Set<Position> getPossibleMovesFromPieceAt(Position pos) {
        if (pos == null) throw new IllegalArgumentException("Position can't be null");

        Piece piece = board.getPiece(pos);
        if (piece == null) return new HashSet<>();

        return piece.getPossibleMoves();
    }


    public PieceColor getWinner() {
        return cWinner;
    }

    public boolean isGameOver() {
        boolean bWhiteKing = false;
        boolean bBlackKing = false;

        for (Piece p : board.getPieces()) {
            if (Character.toUpperCase(p.getDefinition()) == 'K') {
                if (p.getColor() == PieceColor.WHITE) {
                    bWhiteKing = true;
                } else if (p.getColor() == PieceColor.BLACK) {
                    bBlackKing = true;
                }
            }
        }
        if (!bWhiteKing) {
            cWinner = PieceColor.BLACK;
            return true;
        } else if (!bBlackKing) {
            cWinner = PieceColor.WHITE;
            return true;
        }

        // checkmate
        if (bIsCheckmate(PieceColor.WHITE)) {
            cWinner = PieceColor.BLACK;
            return true;
        } else if (bIsCheckmate(PieceColor.BLACK)) {
            cWinner = PieceColor.WHITE;
            return true;
        }

        return false;
    }


    public void resetGame() {
        cCurrentPlayer = PieceColor.WHITE;
        board = new Board();
        board.initializeBoard();
    }



    public boolean bIsCheckmate(PieceColor cPieceColor) {

        Set<Position> sPositions = new HashSet<>();
        Position kingPos = null;
        Piece kingPiece = null;

        for (Piece piece : board.getAllPieces()) {
            if (Character.toUpperCase(piece.getDefinition()) == 'K' && piece.getColor() == cPieceColor) {
                kingPos = piece.getPosition();
                kingPiece = piece;
            }
            if (piece.getColor() != cPieceColor) {
                sPositions.addAll(piece.getPossibleMoves());
            }
        }
        if (kingPos == null) {
            return false;
        }

        if (!sPositions.contains(kingPos)) {
            return false;
        }
        for (Position move : kingPiece.getPossibleMoves()) {
            if (!sPositions.contains(move)) {
                return false;
            }
        }
        return true;
    }



    //PAWN PROMOTION
    public void promotePawn(Position position, char pieceType) {
        Piece pawn = board.getPiece(position);
        if (pawn == null || !(pawn instanceof Pawn)) return;

        PieceColor color = pawn.getColor();
        board.removePiece(position);

        char normalizedType = Character.toUpperCase(pieceType);
        Piece newPiece = switch (normalizedType) {
            case 'Q' -> PieceFactory.QUEEN.createPiece(color, board, position.getColumn(), position.getRow());
            case 'R' -> PieceFactory.ROOK.createPiece(color, board, position.getColumn(), position.getRow());
            case 'B' -> PieceFactory.BISHOP.createPiece(color, board, position.getColumn(), position.getRow());
            case 'N' -> PieceFactory.KNIGHT.createPiece(color, board, position.getColumn(), position.getRow());
            default -> throw new IllegalArgumentException("Invalid piece: " + pieceType);
        };

        board.addPiece(position, newPiece);
    }


    public boolean movePiece(Position oldPos, Position newPos) {
        if (isGameOver()) return false;

        Piece pMover = board.getPiece(oldPos);
        if (pMover == null || !pMover.getPossibleMoves().contains(newPos) || pMover.getColor() != cCurrentPlayer)
            return false;

        // Remove peça capturada
        Piece target = board.getPiece(newPos);
        if (target != null && target.getColor() != pMover.getColor()) board.removePiece(newPos);


        board.removePiece(oldPos);
        pMover.setPosition(newPos);
        board.addPiece(newPos, pMover);

        pMover.setHasMoved(true);

        // Verifica se pawnpromotion
        boolean needsPromotion = (pMover instanceof Pawn pawn && pawn.isPromotionPosition());

        cCurrentPlayer = getcOpponent();
        isGameOver();

        return needsPromotion;
    }


    public void removePiece(Position pos) {
        if (pos == null) throw new IllegalArgumentException("Position can't be null");
        board.removePiece(pos);
    }


    public void importa(String sFileName) throws IOException {
        board.clearBoard();
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
                throw new RuntimeException("color " + tokens[0] + " invalid");
            }

            for (int i = 1; i < tokens.length; i++) {
                String token = tokens[i].trim();

                if (!token.matches("(?i)[kqrbnp][a-h][1-8]\\*?")) {
                    throw new RuntimeException("piece " + token + " invalid");
                }
            }

            cCurrentPlayer = Color;
            board = new Board();

            board.initializeBoard(Arrays.copyOfRange(tokens, 1, tokens.length));

            for (Piece piece : board.getPieces()) {
                piece.setBoard(board);
            }
        }
    }

    public void exporta(String sFileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(cCurrentPlayer).append(",");
        sb.append(getBoardPiecesString());

        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(sFileName)))) {
            dos.write(sb.toString().getBytes());
        }
    }

    public void open(String sFileName) throws IOException, ClassNotFoundException {

        ChessGame loaded = ChessGameSerialization.load(sFileName);

        if (loaded != null) {
            this.board = loaded.board;
            this.cCurrentPlayer = loaded.cCurrentPlayer;
            this.cWinner = loaded.cWinner;
            this.sPlayer1 = loaded.sPlayer1;
            this.sPlayer2 = loaded.sPlayer2;

            for (Piece piece : board.getPieces()) {
                piece.setBoard(board);
            }

        } else {
            board = new Board();
            board.initializeBoard();

            for (Piece piece : board.getPieces()) {
                piece.setBoard(board);
            }

            cCurrentPlayer = PieceColor.WHITE;
            cWinner = null;
        }
    }


    public void save(String sFileName) throws IOException {
        ChessGameSerialization.save(sFileName, this);
    }

    public void restart() {
        cCurrentPlayer = PieceColor.WHITE;
        board = new Board();
        board.initializeBoard();
    }

}
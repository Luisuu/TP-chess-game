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


    public Set<String> getPossibleMovesFromPieceAt(String pos) {
        if (pos == null) throw new IllegalArgumentException("Position can't be null");

        char[] split = pos.toCharArray();

        if (split[0] < 'a' || split[0] > 'h' || (int) split[1] < 1 || (int) split[1] > 8) {
            throw new IllegalArgumentException("Invalid position: " + split[0] + (int) split[1]);
        }

        Position p = new Position(pos);

        Piece piece = board.getPiece(p);
        if (piece == null) return new HashSet<>();

        Set<Position> possibleMoves = piece.getPossibleMoves();
        Set<String> sb = new HashSet<>();
        for (Position move : possibleMoves) {
            if (move.getColumn() < 'a' || move.getColumn() > 'h' || move.getRow() < 1 || move.getRow() > 8) {
                String moveStr = String.valueOf(move.getColumn()) + move.getRow();
                sb.add(moveStr);
            }
        }

        return sb;
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
    public void promotePawn(String position, char pieceType) {

        if (position == null || position.length() != 2) return;

        char column = position.charAt(0);
        int row = Character.getNumericValue(position.charAt(1));

        if (column < 'a' || column > 'h' || row < 1 || row > 8) return;

        Position pos = new Position(column, row);

        Piece pawn = board.getPiece(pos);
        if (pawn == null || !(pawn instanceof Pawn)) return;

        PieceColor color = pawn.getColor();
        board.removePiece(pos);

        char normalizedType = Character.toUpperCase(pieceType);
        Piece newPiece = switch (normalizedType) {
            case 'Q' -> PieceFactory.QUEEN.createPiece(color, board, column, row);
            case 'R' -> PieceFactory.ROOK.createPiece(color, board, column, row);
            case 'B' -> PieceFactory.BISHOP.createPiece(color, board, column, row);
            case 'N' -> PieceFactory.KNIGHT.createPiece(color, board, column, row);
            default -> throw new IllegalArgumentException("Invalid piece: " + pieceType);
        };

        board.addPiece(pos, newPiece);
    }


    public boolean movePiece(String SoldPos, String SnewPos) {
        if (isGameOver()) return false;

        Position newPos = new Position(SnewPos);
        Position oldPos = new Position(SoldPos);

        Piece pMover = board.getPiece(oldPos);
        if (pMover == null || !pMover.getPossibleMoves().contains(newPos) || pMover.getColor() != cCurrentPlayer)
            return false;

        ModelLog.getInstance().log(cCurrentPlayer + " moved piece to: " + newPos);

        // Remove peça capturada
        Piece target = board.getPiece(newPos);
        if (target != null && target.getColor() != pMover.getColor()) board.removePiece(newPos);


        board.removePiece(oldPos);

        boolean lolxd = false;
        if (pMover.getColor() == PieceColor.BLACK) {
            if (pMover.getDefinition() == 'P') {
                if (newPos.getRow() == 1) {
                    board.addPiece(new Position(pMover.getPosition().getColumn(), 1), new Queen(PieceColor.BLACK, this.board, pMover.getPosition().getColumn(), 1));
                    lolxd = true;
                }
            }
        } else {
            if (pMover.getDefinition() == 'P') {
                if (newPos.getRow() == 8) {
                    board.addPiece(new Position(pMover.getPosition().getColumn(), 8), new Queen(PieceColor.WHITE, this.board, pMover.getPosition().getColumn(), 8));
                    lolxd = true;
                }
            }
        }

        if (!lolxd) {
            pMover.setPosition(newPos);
            board.addPiece(newPos, pMover);
        }

        pMover.setHasMoved(true);

        // Verifica se pawnpromotion
        boolean needsPromotion = (pMover instanceof Pawn pawn && pawn.isPromotionPosition());

        cCurrentPlayer = getcOpponent();
        isGameOver();

        return needsPromotion;
    }


    public void removePiece(String pos) {
        if (pos == null) throw new IllegalArgumentException("Position can't be null");

        char[] split = pos.toCharArray();


        if (split[0] < 'a' || split[0] > 'h' || (int) split[1] < 1 || (int) split[1] > 8) {
            throw new IllegalArgumentException("Invalid position: " + split[0] + (int) split[1]);
        }

        Position p = new Position(pos);

        board.removePiece(p);
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
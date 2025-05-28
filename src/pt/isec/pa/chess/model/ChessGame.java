package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.pieces.*;

import java.io.*;
import java.util.*;

/**
 * Core logic of the chess game. Maintains the board state, current player,
 * game rules, checkmate detection, and player management.
 * Handles moves, pawn promotion, game loading/saving, and turn control.
 *
 * Serializable for saving/loading full game state.
 *
 * @see Board
 * @see Piece
 * @see PieceColor
 * @see PieceFactory
 * @see ModelLog
 */


public class ChessGame implements Serializable {
    @Serial
    static final long serialVersionUID = 1L;

    Board board;
    PieceColor cCurrentPlayer = PieceColor.WHITE;
    PieceColor cWinner = null;
    String sPlayer1 = null, sPlayer2 = null;

    /**
     * Constructs a new ChessGame instance with a standard 8x8 chessboard.
     * Initializes the board and sets the current player to white.
     */
    public ChessGame() {
        board = new Board();
        board.clearBoard();
        board.initializeBoard();
    }

    /**
     * Returns the size of the board (number of rows/columns).
     *
     * @return The board size
     */
    public int getBoardSize() {
        return board.getBoardSize();
    }

    /**
     * Sets the name of the specified player.
     *
     * @param player The player number (1 or 2)
     * @param name   The name to assign to the player
     * @throws RuntimeException if the player number is invalid
     */
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

    /**
     * Returns the name of the player associated with the specified color.
     *
     * @param eColor The color of the player (WHITE or BLACK)
     * @return The name of the player
     */
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

    /**
     * Starts a new game with initial board setup.
     */
    public void start() {
        board.clearBoard();
        board.initializeBoard();
    }

    /**
     * Returns the current board pieces as a string.
     *
     * @return Board state string
     */
    public String getBoardPiecesString() {
        return board.printPieces();
    }

    /**
     * Gets the current player's color.
     *
     * @return Current player
     */
    public PieceColor getcCurrentPlayer() {
        return cCurrentPlayer;
    }

    /**
     * Returns the opposing player's color.
     *
     * @return Opponent player
     */
    public PieceColor getcOpponent() {
        return cCurrentPlayer == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
    }

    /**
     * Gets valid moves for a piece at the given position.
     *
     * @param pos Position on board
     * @return Set of valid move strings
     */
    public Set<String> getPossibleMovesFromPieceAt(String pos) {
        if (pos == null)
            throw new IllegalArgumentException("Position can't be null");

        Position p = new Position(pos);
        Piece piece = board.getPiece(p);
        if (piece == null || piece.getColor() != cCurrentPlayer)
            return new HashSet<>();

        Set<String> result = new HashSet<>();
        Position originalPos = piece.getPosition();

        for (Position move : piece.getPossibleMoves()) {
            Piece captured = board.getPiece(move);
            Piece backup = captured;

            // Simulate move
            board.removePiece(originalPos);
            if (captured != null)
                board.removePiece(move);

            piece.setPosition(move);
            board.addPiece(move, piece);

            // Check if king is still in check
            if (!isInCheck(cCurrentPlayer)) {
                result.add("" + move.getColumn() + move.getRow());
            }

            // Undo
            board.removePiece(move);
            piece.setPosition(originalPos);
            board.addPiece(originalPos, piece);
            if (backup != null)
                board.addPiece(move, backup);
        }

        return result;
    }


    /**
     * Returns the winner of the game, if determined.
     *
     * @return The color of the winning player, or null if the game is not over
     */
    public PieceColor getWinner() {
        return cWinner;
    }

    /**
     * Checks if the game has ended (checkmate or no king).
     *
     * @return true if game is over
     */
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

    /**
     * Resets the game to its initial state.
     */
    public void resetGame() {
        cCurrentPlayer = PieceColor.WHITE;
        board = new Board();
        board.initializeBoard();
    }

    /**
     * Determines if the specified player's king is currently under threat.
     * This means at least one enemy piece has a legal move that could capture the king.
     *
     * @param color The color of the player to check (WHITE or BLACK)
     * @return true if the player's king is in check, false otherwise
     */
    private boolean isInCheck(PieceColor color) {
        Position kingPos = null;

        for (Piece piece : new ArrayList<>(board.getAllPieces())) {
            if (Character.toUpperCase(piece.getDefinition()) == 'K' && piece.getColor() == color) {
                kingPos = piece.getPosition();
                break;
            }
        }

        if (kingPos == null)
            return false; // king already captured

        for (Piece enemy : new ArrayList<>(board.getAllPieces())) {
            if (enemy.getColor() != color && enemy.getPossibleMoves().contains(kingPos)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a player is in checkmate.
     *
     * @param color Player's color
     * @return true if in checkmate
     */
    public boolean bIsCheckmate(PieceColor color) {
        if (!isInCheck(color))
            return false;

        List<Piece> piecesCopy = new ArrayList<>(board.getAllPieces());

        for (Piece piece : piecesCopy) {
            if (piece.getColor() != color)
                continue;

            Set<Position> moves = piece.getPossibleMoves();
            Position originalPos = piece.getPosition();

            for (Position move : moves) {
                Piece captured = board.getPiece(move);
                Piece backup = captured;

                board.removePiece(originalPos);
                if (captured != null)
                    board.removePiece(move);

                piece.setPosition(move);
                board.addPiece(move, piece);

                boolean stillInCheck = isInCheck(color);

                // Undo the move
                board.removePiece(move);
                piece.setPosition(originalPos);
                board.addPiece(originalPos, piece);
                if (backup != null)
                    board.addPiece(move, backup);

                if (!stillInCheck)
                    return false;
            }
        }

        return true;
    }

    /**
     * Promotes a pawn to a new piece at a specific position.
     *
     * @param position Position of the pawn
     * @param pieceType New piece type (Q, R, B, N)
     */
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

    /**
     * Moves a piece from one position to another.
     *
     * @param SoldPos Starting position
     * @param SnewPos Ending position
     * @return true if move was valid
     */
    public boolean movePiece(String SoldPos, String SnewPos) {
        if (isGameOver()) return false;

        Position newPos = new Position(SnewPos);
        Position oldPos = new Position(SoldPos);

        Piece pMover = board.getPiece(oldPos);
        if (pMover == null || pMover.getColor() != cCurrentPlayer)
            return false;

        Set<String> legalMoves = getPossibleMovesFromPieceAt(SoldPos);
        if (!legalMoves.contains(SnewPos))
            return false;

        ModelLog.getInstance().log(cCurrentPlayer + " moved piece to: " + newPos);

        // Remove peça capturada
        Piece target = board.getPiece(newPos);
        if (target != null && target.getColor() != pMover.getColor()) board.removePiece(newPos);


        board.removePiece(oldPos);


        // AHAH XDDDD
        // Temporary fix !!!!
        // FIXME
        boolean lolxd = false;
        if (pMover.getColor() == PieceColor.BLACK) {
            if (pMover.getDefinition() == 'p') {
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

        cCurrentPlayer = getcOpponent();
        isGameOver();

        return true;

        // Verifica se pawnpromotion
        //boolean needsPromotion = (pMover instanceof Pawn pawn && pawn.isPromotionPosition());
        //return needsPromotion;
    }

    /**
     * Removes a piece from the board.
     *
     * @param pos Position of the piece to remove
     */
    public void removePiece(String pos) {
        if (pos == null) throw new IllegalArgumentException("Position can't be null");

        char[] split = pos.toCharArray();


        if (split[0] < 'a' || split[0] > 'h' || (int) split[1] < 1 || (int) split[1] > 8) {
            throw new IllegalArgumentException("Invalid position: " + split[0] + (int) split[1]);
        }

        Position p = new Position(pos);

        board.removePiece(p);
    }


    /**
     * Loads a board state from string representation.
     *
     * @param state Board data
     * @param currentPlayer Current player
     */
    public void loadBoardStateFromString(String state, PieceColor currentPlayer) {
        if (state == null || state.isEmpty()) {
            board = new Board();
            board.initializeBoard();
            cCurrentPlayer = PieceColor.WHITE;
            return;
        }

        String[] tokens = state.split(",");
        if (tokens.length == 0) {
            board = new Board();
            board.initializeBoard();
            cCurrentPlayer = currentPlayer;
            return;
        }

        board = new Board();
        board.initializeBoard(tokens);
        cCurrentPlayer = currentPlayer;

        for (Piece piece : board.getPieces()) {
            piece.setBoard(board);
        }
    }

    /**
     * Imports a partial game from a file.
     *
     * @param sFileName File to import
     */
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

    /**
     * Exports the current game to a file.
     *
     * @param sFileName File to export to
     */
    public void exporta(String sFileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(cCurrentPlayer).append(",");
        sb.append(getBoardPiecesString());

        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(sFileName)))) {
            dos.write(sb.toString().getBytes());
        }
    }

    /**
     * Opens a full game state from a serialized file.
     *
     * @param sFileName File to open
     */
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


    /**
     * Saves the current game state to a file.
     *
     * @param sFileName Destination file
     */
    public void save(String sFileName) throws IOException {
        ChessGameSerialization.save(sFileName, this);
    }

    /**
     * Restarts the game, reinitializing the board and players.
     */
    public void restart() {
        cCurrentPlayer = PieceColor.WHITE;
        cWinner = null;
        board = new Board();
        board.initializeBoard();
    }
}
package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.Board;
import pt.isec.pa.chess.model.data.pieces.*;
import pt.isec.pa.chess.model.memento.IMemento;
import pt.isec.pa.chess.model.memento.IOriginator;


import java.io.*;
import java.util.*;

/**
 * Core logic of the chess game. Maintains the board state, current player,
 * game rules, checkmate detection, and player management.
 * Handles moves, pawn promotion, game loading/saving, and turn control.
 * <p>
 * Serializable for saving/loading full game state.
 *
 * @see Board
 * @see Piece
 * @see PieceColor
 * @see PieceFactory
 * @see ModelLog
 */


public class ChessGame implements Serializable, IOriginator {
    @Serial
    static final long serialVersionUID = 1L;

    Piece lastMovedPiece;
    Position lastMoveFrom;
    Position lastMoveTo;
    Board board;
    PieceColor cCurrentPlayer = PieceColor.WHITE;
    PieceColor cWinner = null;
    String sPlayer1 = null, sPlayer2 = null;

    private static class MyMemento implements IMemento, Serializable {
        private final String boardState;
        private final PieceColor currentPlayer;
        private final byte[] snapshot;

        MyMemento(ChessGame base) {
            this.boardState = base.getBoardPiecesString();
            this.currentPlayer = base.getcCurrentPlayer();
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(base);
                this.snapshot = baos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create memento snapshot", e);
            }
        }

        public String getBoardState() {
            return boardState;
        }

        public PieceColor getCurrentPlayer() {
            return currentPlayer;
        }

        @Override
        public Object getSnapshot() {
            if (snapshot == null) return null;
            try (ByteArrayInputStream bais = new ByteArrayInputStream(snapshot); ObjectInputStream ois = new ObjectInputStream(bais)) {
                return ois.readObject();
            } catch (Exception e) {
                return null;
            }
        }
    }

    @Override
    public IMemento save() {
        return new MyMemento(this);
    }

    @Override
    public void restore(IMemento memento) {
        if (memento instanceof MyMemento m) {
            loadBoardStateFromString(m.getBoardState(), m.getCurrentPlayer());
            cWinner = null;
            lastMovedPiece = null;
            lastMoveFrom = null;
            lastMoveTo = null;
            for (Piece piece : board.getPieces()) {
                piece.setBoard(board);
            }
        }
    }

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
        if (pos == null) throw new IllegalArgumentException("Position can't be null");

        Position p = new Position(pos);
        Piece piece = board.getPiece(p);
        if (piece == null || piece.getColor() != cCurrentPlayer) return new HashSet<>();

        Set<String> result = new HashSet<>();
        Position originalPos = piece.getPosition();

        for (Position move : piece.getPossibleMoves()) {

            // movimentos castling King
            boolean isCastling = piece instanceof King && Math.abs(move.getColumn() - originalPos.getColumn()) == 2;

            if (isCastling) {
                if (!isValidCastling(piece, originalPos, move)) {
                    continue;
                }
            }


            Piece captured = board.getPiece(move);
            Piece backup = captured;

            // Simulate move
            board.removePiece(originalPos);
            if (captured != null) board.removePiece(move);

            piece.setPosition(move);
            board.addPiece(move, piece);


            if (!isCastling && !isInCheck(cCurrentPlayer)) {
                result.add("" + move.getColumn() + move.getRow());
            } else if (isCastling) {
                result.add("" + move.getColumn() + move.getRow());
            }

            // Undo
            board.removePiece(move);
            piece.setPosition(originalPos);
            board.addPiece(originalPos, piece);
            if (backup != null) board.addPiece(move, backup);
        }

        // EN PASSANT
        if (piece instanceof Pawn && lastMovedPiece instanceof Pawn && lastMoveFrom != null && lastMoveTo != null) {
            int dir = piece.getColor() == PieceColor.WHITE ? 1 : -1;

            if (Math.abs(lastMoveTo.getRow() - lastMoveFrom.getRow()) == 2 && lastMovedPiece.getColor() != piece.getColor() && lastMoveTo.getRow() == piece.getPosition().getRow() && Math.abs(lastMoveTo.getColumn() - piece.getPosition().getColumn()) == 1) {

                Position enPassantTarget = new Position(lastMoveTo.getColumn(), lastMoveTo.getRow() + dir);

                board.removePiece(originalPos);
                board.removePiece(lastMoveTo);
                piece.setPosition(enPassantTarget);
                board.addPiece(enPassantTarget, piece);

                if (!isInCheck(cCurrentPlayer)) {
                    result.add("" + enPassantTarget.getColumn() + enPassantTarget.getRow());
                }

                board.removePiece(enPassantTarget);
                piece.setPosition(originalPos);
                board.addPiece(originalPos, piece);
                board.addPiece(lastMoveTo, lastMovedPiece);
            }
        }


        return result;
    }


    private boolean isValidCastling(Piece king, Position from, Position to) {
        if (!(king instanceof King) || king.hasMoved() || isInCheck(king.getColor())) {
            return false;
        }

        boolean isKingside = to.getColumn() > from.getColumn();
        char rookCol = isKingside ? 'h' : 'a';
        Position rookPos = new Position(rookCol, from.getRow());
        Piece rook = board.getPiece(rookPos);

        if (rook == null || !(rook instanceof Rook) || rook.getColor() != king.getColor() || rook.hasMoved()) {
            return false;
        }

        int colStep = isKingside ? 1 : -1;
        char startFreeCol = (char) (from.getColumn() + colStep);
        char endFreeCol = isKingside ? 'g' : 'b';
        for (char col = startFreeCol; col != endFreeCol + colStep; col += colStep) {
            if (!board.isEmpty(new Position(col, from.getRow()))) {
                return false;
            }
        }
        char kingEndCol = isKingside ? 'g' : 'c';
        for (char col = from.getColumn(); col != kingEndCol + (isKingside ? 1 : -1); col += colStep) {
            Position pos = new Position(col, from.getRow());
            for (Piece enemy : board.getAllPieces()) {
                if (enemy.getColor() != king.getColor() && enemy.getPossibleMoves().contains(pos)) {
                    return false;
                }
            }
        }

        return true;
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

        // Check for stalemate
        if (!isInCheck(cCurrentPlayer) && !hasLegalMoves(cCurrentPlayer)) {
            cWinner = null; // Null indicates a draw
            return true;
        }

        return false;
    }

    private boolean hasLegalMoves(PieceColor color) {
        for (Piece piece : board.getAllPieces()) {
            if (piece.getColor() == color) {
                String pos = "" + piece.getPosition().getColumn() + piece.getPosition().getRow();
                if (!getPossibleMovesFromPieceAt(pos).isEmpty()) {
                    return true;
                }
            }
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

        if (kingPos == null) return false; // king already captured

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
        if (!isInCheck(color)) return false;

        List<Piece> piecesCopy = new ArrayList<>(board.getAllPieces());

        for (Piece piece : piecesCopy) {
            if (piece.getColor() != color) continue;

            Set<Position> moves = piece.getPossibleMoves();
            Position originalPos = piece.getPosition();

            for (Position move : moves) {
                Piece captured = board.getPiece(move);
                Piece backup = captured;

                board.removePiece(originalPos);
                if (captured != null) board.removePiece(move);

                piece.setPosition(move);
                board.addPiece(move, piece);

                boolean stillInCheck = isInCheck(color);

                // Undo the move
                board.removePiece(move);
                piece.setPosition(originalPos);
                board.addPiece(originalPos, piece);
                if (backup != null) board.addPiece(move, backup);

                if (!stillInCheck) return false;
            }
        }

        return true;
    }

    /**
     * Promotes a pawn to a new piece at a specific position.
     *
     * @param position  Position of the pawn
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
        if (pMover == null || pMover.getColor() != cCurrentPlayer) return false;

        Set<String> legalMoves = getPossibleMovesFromPieceAt(SoldPos);
        if (!legalMoves.contains(SnewPos)) return false;

        ModelLog.getInstance().log(cCurrentPlayer + " moved piece to: " + newPos);
        boolean isCastling = false;


        // castling
        Position rookOldPos = null, rookNewPos = null;
        if (pMover instanceof King && Math.abs(newPos.getColumn() - oldPos.getColumn()) == 2) {
            isCastling = true;
            if (newPos.getColumn() > oldPos.getColumn()) {
                rookOldPos = new Position('h', oldPos.getRow());
                rookNewPos = new Position('f', oldPos.getRow());
            } else {
                rookOldPos = new Position('a', oldPos.getRow());
                rookNewPos = new Position('d', oldPos.getRow());
            }
        }


        // Remove peça capturada
        Piece target = board.getPiece(newPos);
        if (target != null && target.getColor() != pMover.getColor()) {
            board.removePiece(newPos);
        }

        // EN PASSANT
        if (pMover instanceof Pawn && lastMovedPiece instanceof Pawn) {
            if (Math.abs(newPos.getColumn() - oldPos.getColumn()) == 1 && target == null) {
                if (pMover.getColor() == PieceColor.WHITE && oldPos.getRow() == 5 && newPos.getRow() == 6) {
                    Position capturedPos = new Position(newPos.getColumn(), 5);
                    Piece maybeCaptured = board.getPiece(capturedPos);
                    if (maybeCaptured == lastMovedPiece && maybeCaptured.getColor() != pMover.getColor()) {
                        board.removePiece(capturedPos);
                    }
                } else if (pMover.getColor() == PieceColor.BLACK && oldPos.getRow() == 4 && newPos.getRow() == 3) {
                    Position capturedPos = new Position(newPos.getColumn(), 4);
                    Piece maybeCaptured = board.getPiece(capturedPos);
                    if (maybeCaptured == lastMovedPiece && maybeCaptured.getColor() != pMover.getColor()) {
                        board.removePiece(capturedPos);
                    }
                }
            }
        }

        board.removePiece(oldPos);

        /*
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
        }*/

        pMover.setPosition(newPos);
        board.addPiece(newPos, pMover);

        // Check for pawn promotion


        // Mover rook castling
        if (isCastling) {
            Piece rook = board.getPiece(rookOldPos);
            if (rook != null && rook instanceof Rook) {
                board.removePiece(rookOldPos);
                rook.setPosition(rookNewPos);
                board.addPiece(rookNewPos, rook);
                rook.setHasMoved(true);
            }
        }


        pMover.setHasMoved(true);
        cCurrentPlayer = getcOpponent();
        lastMovedPiece = pMover;
        lastMoveFrom = new Position(oldPos.getColumn(), oldPos.getRow());
        lastMoveTo = new Position(newPos.getColumn(), newPos.getRow());
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
     * @param state         Board data
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

            String line = new String(data).replaceAll("\\R", "");
            line = line.replaceAll("\\s", "");
            line = line.trim();

            //System.out.println(line);

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
package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;

public enum PieceFactory {
    KING {
        @Override
        public Piece createPiece(Color color, Board board, char column, int row) {
            if (column < 'a' || column > 'h' || row < 1 || row > 8) {
                return null;
            }
            return new King(color, board, column, row);
        }
    }, QUEEN {
        @Override
        public Piece createPiece(Color color, Board board, char column, int row) {
            if (column < 'a' || column > 'h' || row < 1 || row > 8) {
                return null;
            }
            return new Queen(color, board, column, row);
        }
    }, BISHOP {
        @Override
        public Piece createPiece(Color color, Board board, char column, int row) {
            if (column < 'a' || column > 'h' || row < 1 || row > 8) {
                return null;
            }
            return new Bishop(color, board, column, row);
        }
    }, KNIGHT {
        @Override
        public Piece createPiece(Color color, Board board, char column, int row) {
            if (column < 'a' || column > 'h' || row < 1 || row > 8) {
                return null;
            }
            return new Knight(color, board, column, row);
        }
    }, ROOK {
        @Override
        public Piece createPiece(Color color, Board board, char column, int row) {
            if (column < 'a' || column > 'h' || row < 1 || row > 8) {
                return null;
            }
            return new Rook(color, board, column, row);
        }
    }, PAWN {
        @Override
        public Piece createPiece(Color color, Board board, char column, int row) {
            if (column < 'a' || column > 'h' || row < 1 || row > 8) {
                return null;
            }
            return new Pawn(color, board, column, row);
        }
    };

    public abstract Piece createPiece(Color color, Board board, char column, int row);



    //segundo metodo


    public static Piece PieceFactoryFromText(String textRepresentation, Board board) {
        if (textRepresentation == null || textRepresentation.isEmpty()) {
            return null;
        }

        boolean hasNotMoved = false;
        String pieceData = textRepresentation;

        if (textRepresentation.endsWith("*")) {
            hasNotMoved = true;
            pieceData = textRepresentation.substring(0, textRepresentation.length() - 1);
        }

        char pieceType = pieceData.charAt(0);
        char column = pieceData.charAt(1);
        int row;

        try {
            row = Integer.parseInt(pieceData.substring(2));
        } catch (NumberFormatException e) {
            return null;
        }

        // Determine piece color
        Color color = Character.isUpperCase(pieceType) ? Color.WHITE : Color.BLACK;

        // Normalize piece type to uppercase for switch statement
        char normalizedType = Character.toUpperCase(pieceType);

        Piece piece;

        // Create the piece based on the type
        switch (normalizedType) {
            case 'K':
                piece = KING.createPiece(color, board, column, row);
                if (piece != null && !hasNotMoved) {
                    ((King) piece).setHasMoved(true); // King starts with hasMoved=false by default
                }
                break;
            case 'Q':
                piece = QUEEN.createPiece(color, board, column, row);
                break;
            case 'R':
                piece = ROOK.createPiece(color, board, column, row);
                if (piece != null && !hasNotMoved) {
                    ((Rook) piece).setHasMoved(true); // Set hasMoved based on asterisk
                }                break;
            case 'B':
                piece = BISHOP.createPiece(color, board, column, row);
                break;
            case 'N':
                piece = KNIGHT.createPiece(color, board, column, row);
                break;
            case 'P':
                piece = PAWN.createPiece(color, board, column, row);
                break;
            default:
                return null;
        }

        return piece;
    }
}
package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.Board;

public enum PieceFactory {
    KING {
        @Override
        public Piece createPiece(PieceColor PieceColor, Board board, char column, int row) {
            if (column < 'a' || column > 'h' || row < 1 || row > 8) {
                return null;
            }
            return new King(PieceColor, board, column, row);
        }
    }, QUEEN {
        @Override
        public Piece createPiece(PieceColor PieceColor, Board board, char column, int row) {
            if (column < 'a' || column > 'h' || row < 1 || row > 8) {
                return null;
            }
            return new Queen(PieceColor, board, column, row);
        }
    }, BISHOP {
        @Override
        public Piece createPiece(PieceColor PieceColor, Board board, char column, int row) {
            if (column < 'a' || column > 'h' || row < 1 || row > 8) {
                return null;
            }
            return new Bishop(PieceColor, board, column, row);
        }
    }, KNIGHT {
        @Override
        public Piece createPiece(PieceColor PieceColor, Board board, char column, int row) {
            if (column < 'a' || column > 'h' || row < 1 || row > 8) {
                return null;
            }
            return new Knight(PieceColor, board, column, row);
        }
    }, ROOK {
        @Override
        public Piece createPiece(PieceColor PieceColor, Board board, char column, int row) {
            if (column < 'a' || column > 'h' || row < 1 || row > 8) {
                return null;
            }
            return new Rook(PieceColor, board, column, row);
        }
    }, PAWN {
        @Override
        public Piece createPiece(PieceColor PieceColor, Board board, char column, int row) {
            if (column < 'a' || column > 'h' || row < 1 || row > 8) {
                return null;
            }
            return new Pawn(PieceColor, board, column, row);
        }
    };

    public abstract Piece createPiece(PieceColor PieceColor, Board board, char column, int row);


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

        //ve cor peça
        PieceColor ePieceColor = Character.isUpperCase(pieceType) ? PieceColor.WHITE : PieceColor.BLACK;

        char normalizedType = Character.toUpperCase(pieceType);

        Piece piece;

        // Cria peça tendo em conta o tipo
        switch (normalizedType) {
            case 'K':
                piece = KING.createPiece(ePieceColor, board, column, row);
                if (piece != null && !hasNotMoved) {
                    ((King) piece).setHasMoved(true); //hasmoved=false por default
                }
                break;
            case 'Q':
                piece = QUEEN.createPiece(ePieceColor, board, column, row);
                break;
            case 'R':
                piece = ROOK.createPiece(ePieceColor, board, column, row);
                if (piece != null && !hasNotMoved) {
                    ((Rook) piece).setHasMoved(true); //hasmoved=false por default
                }
                break;
            case 'B':
                piece = BISHOP.createPiece(ePieceColor, board, column, row);
                break;
            case 'N':
                piece = KNIGHT.createPiece(ePieceColor, board, column, row);
                break;
            case 'P':
                piece = PAWN.createPiece(ePieceColor, board, column, row);
                break;
            default:
                return null;
        }

        return piece;
    }
}
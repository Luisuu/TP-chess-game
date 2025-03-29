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
}

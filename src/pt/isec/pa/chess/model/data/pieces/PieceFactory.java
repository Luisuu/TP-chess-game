package pt.isec.pa.chess.model.data.pieces;

public enum PieceFactory {
    KING {
        @Override
        public Piece createPiece(Color color, char column, int row) {
            return new King(color, column, row);
        }
    },
    QUEEN {
        @Override
        public Piece createPiece(Color color, char column, int row) {
            return new Queen(color, column, row);
        }
    },
    BISHOP {
        @Override
        public Piece createPiece(Color color, char column, int row) {
            return new Bishop(color, column, row);
        }
    },
    KNIGHT {
        @Override
        public Piece createPiece(Color color, char column, int row) {
            return new Knight(color, column, row);
        }
    },
    ROOK {
        @Override
        public Piece createPiece(Color color, char column, int row) {
            return new Rook(color, column, row);
        }
    },
    PAWN {
        @Override
        public Piece createPiece(Color color, char column, int row) {
            return new Pawn(color, column, row);
        }
    };

    public abstract Piece createPiece(Color color, char column, int row);
}

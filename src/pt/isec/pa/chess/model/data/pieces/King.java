package pt.isec.pa.chess.model.data.pieces;

import java.util.*;

public class King extends Piece{
    private static final Map<Position, Set<Position>> possibleMovesMap = new HashMap<>();
    private boolean bHasMoved;

    public King(boolean bIsWhite, char cColumn, int iRow){
        super('k', bIsWhite, cColumn, iRow);
        this.bHasMoved = false;
        initializePossibleMoves();
    }

    void initializePossibleMoves(){

    }
}

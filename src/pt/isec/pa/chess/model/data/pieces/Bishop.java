package pt.isec.pa.chess.model.data.pieces;

import java.util.*;

public class Bishop extends Piece{
    private static final Map<Position, Set<Position>> possibleMovesMap = new HashMap<>();

    public Bishop(char cDefinition, boolean bIsWhite, char cColumn, int iRow) {
        super(cDefinition, bIsWhite, cColumn, iRow);
        initializePossibleMoves();
    }

   void initializePossibleMoves(){
       if (!possibleMovesMap.containsKey(pPos)) {
           Set<Position> possibleMoves = new HashSet<>();
           for(int i = 1; i < 8; i++){
               possibleMoves.add(new Position((char) (pPos.cColumn +i), pPos.iRow +i)); //diag direita cima
               possibleMoves.add(new Position((char) (pPos.cColumn +i), pPos.iRow -i)); //diag direita baixo
               possibleMoves.add(new Position((char) (pPos.cColumn -i), pPos.iRow +i)); //diag esquerda cima
               possibleMoves.add(new Position((char) (pPos.cColumn -i), pPos.iRow -i)); //diag esquerda baixo
           }
           possibleMovesMap.put(pPos, possibleMoves);
       }
   }

    //@Override
    public Set<Position> getPossibleMoves() {
        return possibleMovesMap.getOrDefault(pPos, new HashSet<>());
    }

    @Override
    public String toString() {
        return (bIsWhite ? "B" : "b") + pPos.cColumn + pPos.iRow;
    }

}


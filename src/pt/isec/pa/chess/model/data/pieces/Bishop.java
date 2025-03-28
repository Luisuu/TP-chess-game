package pt.isec.pa.chess.model.data.pieces;

import java.util.*;

public class Bishop extends Piece {
    private static final Map<Position, Set<Position>> possibleMovesMap = new HashMap<>();

    public Bishop(Color color, char cColumn, int iRow) {
        super('B', color, cColumn, iRow);
        initializePossibleMoves();
    }

    void initializePossibleMoves() {
        if (!possibleMovesMap.containsKey(pPos)) {
            Set<Position> possibleMoves = new HashSet<>();
            for (int i = 1; i < 8; i++) {
                try {
                    possibleMoves.add(new Position((char) (pPos.getColumn() + i), pPos.getRow() + i));
                } catch (IllegalArgumentException ignored) {
                }
                try {
                    possibleMoves.add(new Position((char) (pPos.getColumn() + i), pPos.getRow() - i));
                } catch (IllegalArgumentException ignored) {
                }
                try {
                    possibleMoves.add(new Position((char) (pPos.getColumn() - i), pPos.getRow() + i));
                } catch (IllegalArgumentException ignored) {
                }
                try {
                    possibleMoves.add(new Position((char) (pPos.getColumn() - i), pPos.getRow() - i));
                } catch (IllegalArgumentException ignored) {
                }
            }
            possibleMovesMap.put(pPos, possibleMoves);
        }
    }

    public Set<Position> getPossibleMoves() {
        return possibleMovesMap.getOrDefault(pPos, new HashSet<>());
    }
}
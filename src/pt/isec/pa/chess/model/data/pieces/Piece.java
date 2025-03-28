package pt.isec.pa.chess.model.data.pieces;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class Position{
    Set<Character> sPossibleColumns = new HashSet<>(Arrays.asList('a','b','c','d','e','f','g','h'));
    Set<Integer> sPossibleRows = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9));

    char cColumn;
    int iRow;

    public Position(char cColumn, int iRow){
        if(!this.sPossibleColumns.contains(cColumn) || !this.sPossibleRows.contains(iRow)){
            throw new IllegalArgumentException("Invalid row or column");
        }
        this.cColumn = cColumn;
        this.iRow = iRow;
    }

    void setcColumn(char cColumn){
        if(!this.sPossibleColumns.contains(cColumn)){
            throw new IllegalArgumentException("Invalid column");
        }
        this.cColumn = cColumn;
    }

    void setiRow(int iRow){
        if(!this.sPossibleRows.contains(iRow)){
            throw new IllegalArgumentException("Invalid row");
        }
        this.iRow = iRow;
    }

    char getcColumn(){
        return this.cColumn;
    }

    int getiRow(){
        return this.iRow;
    }
}

abstract public class Piece {

    char cDefinition;
    boolean bIsWhite;
    Position pPos;

    Piece(char cDefinition, boolean bIsWhite, char cColumn, int iRow){
        char cDef = cDefinition;
        if(bIsWhite){
            this.cDefinition = Character.toUpperCase(cDef);
        }else{
            this.cDefinition = Character.toLowerCase(cDef);
        }
        this.bIsWhite = bIsWhite;
        this.pPos.setcColumn(cColumn);
        this.pPos.setiRow(iRow);
    }

}

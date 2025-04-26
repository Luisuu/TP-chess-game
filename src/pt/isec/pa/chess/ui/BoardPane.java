package pt.isec.pa.chess.ui;

import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

public class BoardPane extends Pane {
    TilePane boardTiles;

    public BoardPane() {
        createBoard();
    }

    private void createBoard() {
        boardTiles = new TilePane();
        boardTiles.setPrefColumns(8);
        boardTiles.setPrefRows(8);
        boardTiles.setPrefTileWidth(70);
        boardTiles.setPrefTileHeight(70);
        boardTiles.setHgap(0);
        boardTiles.setVgap(0);

        createBoardTiles();

        this.getChildren().add(boardTiles);
    }

    private void createBoardTiles() {
        boolean isWhite = true;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Pane tile = new Pane();
                tile.setPrefSize(70, 70);
                tile.setStyle("-fx-background-color: " + (isWhite ? "#ffffff" : "#888888") + ";");

                boardTiles.getChildren().add(tile);

                isWhite = !isWhite;
            }
            isWhite = !isWhite;
        }
    }
}


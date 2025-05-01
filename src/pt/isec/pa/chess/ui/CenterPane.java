package pt.isec.pa.chess.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import pt.isec.pa.chess.model.ChessGameManager;

public class CenterPane extends BorderPane {
    private static final int NUM_ROWS = 8;
    private static final int NUM_COLS = 8;

    private final GridPane boardGrid = new GridPane();
    private final ChessGameManager data;
    private final Label topLabel = new Label("Player: White!");

    public CenterPane(ChessGameManager data) {
        this.data = data;

        createTopLabel();
        createBoardGrid();

        StackPane centerStack = new StackPane(boardGrid) {
            @Override
            protected void layoutChildren() {
                double size = Math.min(getWidth(), getHeight());
                double x = (getWidth() - size) / 2;
                double y = (getHeight() - size) / 2;
                boardGrid.resizeRelocate(x, y, size, size);
            }
        };

        centerStack.setStyle("-fx-background-color: #f0f0f0;");

        setTop(topLabel);
        setCenter(centerStack);

        centerStack.widthProperty().addListener((obs, oldVal, newVal) -> resize(centerStack));
        centerStack.heightProperty().addListener((obs, oldVal, newVal) -> resize(centerStack));
    }

    private void createTopLabel() {
        topLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        BorderPane.setAlignment(topLabel, Pos.CENTER);
        setTop(topLabel);
    }

    private void resize(StackPane centerStack) {
        double size = Math.min(centerStack.getWidth(), centerStack.getHeight());
        boardGrid.setPrefSize(size, size);
    }

    private void createBoardGrid() {
        boardGrid.setGridLinesVisible(true);
        boardGrid.setAlignment(Pos.CENTER);

        for (int i = 0; i < NUM_COLS + 2; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / (NUM_COLS + 2));
            boardGrid.getColumnConstraints().add(col);
        }

        for (int i = 0; i < NUM_ROWS + 2; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / (NUM_ROWS + 2));
            boardGrid.getRowConstraints().add(row);
        }

        for (int col = 0; col < NUM_COLS; col++) {
            Label labelTop = makeLabel(String.valueOf((char) ('a' + col)));
            Label labelBottom = makeLabel(String.valueOf((char) ('a' + col)));

            boardGrid.add(labelTop, col + 1, 0);
            boardGrid.add(labelBottom, col + 1, NUM_ROWS + 1);
        }

        for (int row = 0; row < NUM_ROWS; row++) {
            String text = String.valueOf(NUM_ROWS - row);
            Label labelLeft = makeLabel(text);
            Label labelRight = makeLabel(text);

            boardGrid.add(labelLeft, 0, row + 1);
            boardGrid.add(labelRight, NUM_COLS + 1, row + 1);
        }

        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                StackPane cell = new StackPane();
                String color = (row + col) % 2 == 0 ? "#ffffff" : "#888888";
                cell.setStyle("-fx-background-color: " + color + ";");
                cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                boardGrid.add(cell, col + 1, row + 1);
            }
        }
    }

    private Label makeLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        label.setStyle("-fx-font-size: 14px;");
        return label;
    }
}
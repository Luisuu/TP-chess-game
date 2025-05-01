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
        boardGrid.setGridLinesVisible(false);
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
            labelTop.setStyle("""
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-text-fill: brown;
                -fx-background-color: #f0f0f0;
                -fx-border-color: transparent transparent brown transparent;
                -fx-border-width: 0 0 1 0;
            """);

            Label labelBottom = makeLabel(String.valueOf((char) ('a' + col)));
            labelBottom.setStyle("""
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-text-fill: brown;
                -fx-background-color: #f0f0f0;
                -fx-border-color: brown transparent transparent transparent;
                -fx-border-width: 1 0 0 0;
            """);

            boardGrid.add(labelTop, col + 1, 0);
            boardGrid.add(labelBottom, col + 1, NUM_ROWS + 1);
        }

        for (int row = 0; row < NUM_ROWS; row++) {
            String text = String.valueOf(NUM_ROWS - row);

            Label labelLeft = makeLabel(text);
            labelLeft.setStyle("""
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-text-fill: brown;
                -fx-background-color: #f0f0f0;
                -fx-border-color: transparent brown transparent transparent;
                -fx-border-width: 0 1 0 0;
            """);

            Label labelRight = makeLabel(text);
            labelRight.setStyle("""
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-text-fill: brown;
                -fx-background-color: #f0f0f0;
                -fx-border-color: transparent transparent transparent brown;
                -fx-border-width: 0 0 0 1;
            """);

            boardGrid.add(labelLeft, 0, row + 1);
            boardGrid.add(labelRight, NUM_COLS + 1, row + 1);
        }

        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                int finalRow = row;
                int finalCol = col;

                StackPane cell = new StackPane();
                String color = (finalRow + finalCol) % 2 == 0 ? "#fceee9" : "#944b45";
                cell.setStyle("-fx-background-color: " + color + ";");

                //DEBUGGGG
                cell.setOnMouseClicked(event -> {
                    char columnLetter = (char) ('a' + finalCol);
                    int chessRow = 8 - finalRow;
                    System.out.println("Cell clicked: " + columnLetter + chessRow);
                });

                boardGrid.add(cell, finalCol + 1, finalRow + 1);

            }
        }
    }

    private Label makeLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return label;
    }
}

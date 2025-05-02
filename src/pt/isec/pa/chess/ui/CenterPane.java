package pt.isec.pa.chess.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.ui.res.ImageManager;

public class CenterPane extends BorderPane {
    private static final int NUM_ROWS = 8;
    private static final int NUM_COLS = 8;

    private final GridPane boardGrid = new GridPane();
    private final ChessGameManager data;
    private final Label topLabel = new Label("Player: White!");
    private final StackPane[][] cells = new StackPane[NUM_ROWS][NUM_COLS];

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
        centerStack.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        boardGrid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        setTop(topLabel);
        setCenter(centerStack);

        centerStack.widthProperty().addListener((obs, oldVal, newVal) -> resize(centerStack));
        centerStack.heightProperty().addListener((obs, oldVal, newVal) -> resize(centerStack));
    }

    private void createTopLabel() {
        topLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        BorderPane.setAlignment(topLabel, Pos.CENTER);
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
            col.setMinWidth(0);
            boardGrid.getColumnConstraints().add(col);
        }

        for (int i = 0; i < NUM_ROWS + 2; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / (NUM_ROWS + 2));
            row.setMinHeight(0);
            boardGrid.getRowConstraints().add(row);
        }

        for (int col = 0; col < NUM_COLS; col++) {
            String colLabel = String.valueOf((char) ('a' + col));
            Label labelTop = makeBorderLabel(colLabel, "-fx-border-color: transparent transparent brown transparent;");
            Label labelBottom = makeBorderLabel(colLabel, "-fx-border-color: brown transparent transparent transparent;");
            boardGrid.add(labelTop, col + 1, 0);
            boardGrid.add(labelBottom, col + 1, NUM_ROWS + 1);
        }

        for (int row = 0; row < NUM_ROWS; row++) {
            String rowLabel = String.valueOf(NUM_ROWS - row);
            Label labelLeft = makeBorderLabel(rowLabel, "-fx-border-color: transparent brown transparent transparent;");
            Label labelRight = makeBorderLabel(rowLabel, "-fx-border-color: transparent transparent transparent brown;");
            boardGrid.add(labelLeft, 0, row + 1);
            boardGrid.add(labelRight, NUM_COLS + 1, row + 1);
        }

        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                StackPane cell = new StackPane();
                cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                GridPane.setHgrow(cell, Priority.ALWAYS);
                GridPane.setVgrow(cell, Priority.ALWAYS);

                String color = (row + col) % 2 == 0 ? "#fceee9" : "#944b45";
                cell.setStyle("-fx-background-color: " + color + ";");

                int finalRow = row;
                int finalCol = col;
                cell.setOnMouseClicked(event -> {
                    char columnLetter = (char) ('a' + finalCol);
                    int chessRow = 8 - finalRow;
                    System.out.println("Cell clicked: " + columnLetter + chessRow);
                });

                cells[row][col] = cell;
                boardGrid.add(cell, col + 1, row + 1);
            }
        }

        renderPiecesFromModel();
    }

    private void renderPiecesFromModel() {
        for (StackPane[] row : cells)
            for (StackPane cell : row)
                cell.getChildren().clear();

        String pieceData = data.getBoardPiecesString();
        String[] tokens = pieceData.split(",");

        for (String token : tokens) {
            token = token.trim();
            if (token.length() < 3) continue;

            char type = token.charAt(0);
            boolean isWhite = Character.isUpperCase(type);
            String position = token.substring(1, 3).toLowerCase();
            int col = position.charAt(0) - 'a';
            int row = 8 - Character.getNumericValue(position.charAt(1));

            if (row < 0 || row >= 8 || col < 0 || col >= 8) continue;

            String pieceName = switch (Character.toUpperCase(type)) {
                case 'K' -> "king";
                case 'Q' -> "queen";
                case 'B' -> "bishop";
                case 'N' -> "knight";
                case 'R' -> "rook";
                case 'P' -> "pawn";
                default -> null;
            };

            if (pieceName != null) {
                String filename = "pieces/" + pieceName + (isWhite ? "W" : "B") + ".png";
                placePiece(cells[row][col], filename);
            }
        }
    }

    private void placePiece(StackPane cell, String filename) {
        Image img = ImageManager.getImage(filename);
        if (img != null) {
            ImageView view = new ImageView(img);
            view.setPreserveRatio(true);
            view.fitWidthProperty().bind(cell.widthProperty().multiply(0.8));
            view.fitHeightProperty().bind(cell.heightProperty().multiply(0.8));
            cell.getChildren().add(view);
        }
    }

    private Label makeBorderLabel(String text, String borderStyle) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        label.setStyle("""
                    -fx-font-size: 14px;
                    -fx-font-weight: bold;
                    -fx-text-fill: brown;
                    -fx-background-color: #f0f0f0;
                """ + borderStyle);
        return label;
    }

    public void update(){
        renderPiecesFromModel();
    }
}

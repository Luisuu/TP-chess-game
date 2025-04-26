package pt.isec.pa.chess.ui;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import pt.isec.pa.chess.model.ChessGameManager;

public class CenterPane extends BorderPane {
    ChessGameManager data;
    Label topLabel;
    GridPane board;
    HBox top, bottom;
    VBox left, right;
    StackPane centerPane;

    public static final int NUM_ROWS = 8;
    public static final int NUM_COLS = 8;

    public CenterPane(ChessGameManager data) {
        this.data = data;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        top = new HBox();
        top.setPrefHeight(50);
        top.setAlignment(Pos.CENTER);
        top.setStyle("-fx-background-color: #80c0ff;");

        topLabel = new Label("Player: White !");
        topLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        top.getChildren().add(topLabel);


        bottom = new HBox();
        bottom.setPrefHeight(50);
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.setSpacing(5);
        bottom.setStyle("-fx-background-color: #80c0ff;");


        left = new VBox();
        left.setPrefWidth(50);
        left.setAlignment(Pos.CENTER);
        left.setSpacing(5);
        left.setStyle("-fx-background-color: #80ffc0;");


        right = new VBox();
        right.setPrefWidth(50);
        right.setAlignment(Pos.CENTER);
        right.setSpacing(5);
        right.setStyle("-fx-background-color: #80ffc0;");


        centerPane = new StackPane();
        board = new GridPane();
        board.setGridLinesVisible(true);
        centerPane.getChildren().add(board);

        setTop(top);
        setBottom(bottom);
        setLeft(left);
        setRight(right);
        setCenter(centerPane);

        createBoard();
        createLabels();
    }

    private void createBoard() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                StackPane cell = new StackPane();
                String color = (row + col) % 2 == 0 ? "white" : "gray";
                cell.setStyle("-fx-background-color: " + color + ";");

                cell.prefWidthProperty().bind(centerPane.widthProperty().divide(NUM_COLS));
                cell.prefHeightProperty().bind(centerPane.heightProperty().divide(NUM_ROWS));

                board.add(cell, col, row);
            }
        }
    }

    private void createLabels() {
        for (char c = 'a'; c <= 'h'; c++) {
            Label label = new Label(String.valueOf(c));
            label.setStyle("-fx-font-size: 14px;");
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER);

            label.prefWidthProperty().bind(centerPane.widthProperty().divide(NUM_COLS));
            label.prefHeightProperty().bind(Bindings.createDoubleBinding(() -> 30.0, centerPane.heightProperty()));

            bottom.getChildren().add(label);
        }


        for (int i = 8; i >= 1; i--) {
            Label label = new Label(String.valueOf(i));
            label.setStyle("-fx-font-size: 14px;");
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER);

            label.prefHeightProperty().bind(centerPane.heightProperty().divide(NUM_ROWS));
            label.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> 30.0, centerPane.widthProperty()));

            left.getChildren().add(label);
        }

        for (int i = 8; i >= 1; i--) {
            Label label = new Label(String.valueOf(i));
            label.setStyle("-fx-font-size: 14px;");
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER);

            label.prefHeightProperty().bind(centerPane.heightProperty().divide(NUM_ROWS));
            label.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> 30.0, centerPane.widthProperty()));

            right.getChildren().add(label);
        }
    }


    private void registerHandlers() {
    }

    private void update() {
    }
}

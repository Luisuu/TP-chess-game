package pt.isec.pa.chess.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;

import javax.swing.border.Border;

public class CenterPane extends BorderPane {
    ChessGameManager data;
    Label infoLabel, Player;
    Pane top, bottom, left, right, center;

    public CenterPane(ChessGameManager data) {
        this.data = data;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        HBox top = new HBox();
        top.setPrefHeight(30);
        top.setStyle("-fx-background-color: #80c0ff;");

        Label topLabel = new Label("Player: White !");
        topLabel.setLayoutX(10);
        topLabel.setLayoutY(10);
        top.getChildren().add(topLabel);

        HBox bottom = new HBox();
        bottom.setPrefHeight(30);
        bottom.setStyle("-fx-background-color: #80c0ff;");
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.setSpacing(60);
        bottom.setPadding(new Insets(0, 0, 0, 50));
        for (char c = 'a'; c <= 'h'; c++) {
            Label label = new Label(String.valueOf(c));
            label.setStyle("-fx-font-size: 15px;");
            bottom.getChildren().add(label);
        }


        VBox left = new VBox();
        left.setPrefWidth(30);
        left.setStyle("-fx-background-color: #80ffc0;");
        left.setAlignment(Pos.CENTER);
        left.setSpacing(35);

        for (int i = 8; i >= 1; i--) {
            Label label = new Label(String.valueOf(i));
            label.setStyle("-fx-font-size: 15px;");
            left.getChildren().add(label);
        }

        VBox right = new VBox();
        right.setPrefWidth(30);
        right.setStyle("-fx-background-color: #80ffc0;");
        right.setAlignment(Pos.CENTER);
        right.setSpacing(35);

        for (int i = 8; i >= 1; i--) {
            Label label = new Label(String.valueOf(i));
            label.setStyle("-fx-font-size: 15px;");
            right.getChildren().add(label);
        }

        center = new BoardPane();

        setCenter(center);
        setTop(top);
        setBottom(bottom);
        setLeft(left);
        setRight(right);
    }

        private void registerHandlers() {

    }

    private void update() {
    }
}

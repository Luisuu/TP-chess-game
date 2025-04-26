package pt.isec.pa.chess.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;

import javax.swing.border.Border;

public class CenterPane extends BorderPane {
    ChessGameManager data;
    Label infoLabel;
    Pane top, bottom, left, right, center;

    public CenterPane(ChessGameManager data) {
        this.data = data;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        /*this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);

        Label infoLabel = new Label("Chess Game.\nStart a new game from the Game menu.");
        infoLabel.setStyle("-fx-font-size: 16px;");

        this.getChildren().add(infoLabel);*/

        top = new Pane();
        top.setPrefHeight(50);
        top.setStyle("-fx-background-color: #80c0ff;");
        bottom = new Pane();
        bottom.setPrefHeight(50);
        bottom.setStyle("-fx-background-color: #80c0ff;");
        left = new Pane();
        left.setPrefWidth(50);
        left.setStyle("-fx-background-color: #80ffc0;");
        right = new Pane();
        right.setPrefWidth(50);
        right.setStyle("-fx-background-color: #80ffc0;");
        center = new Pane();
        center.setPrefWidth(50);
        center.setPrefHeight(50);
        center.setStyle("-fx-background-color: #ffc080;");

        setTop(top);
        setBottom(bottom);
        setLeft(left);
        setRight(right);
        setCenter(center);


    }

    private void registerHandlers() {

    }

    private void update() {
    }
}

package pt.isec.pa.chess.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;

public class CenterPane extends HBox {
    ChessGameManager data;
    Label infoLabel;

    public CenterPane(ChessGameManager data) {
        this.data = data;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);

        Label infoLabel = new Label("Chess Game.");
        infoLabel.setStyle("-fx-font-size: 16px;");

        this.getChildren().add(infoLabel);
    }

    private void registerHandlers() {

    }

    private void update() {
    }
}

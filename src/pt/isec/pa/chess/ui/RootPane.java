package pt.isec.pa.chess.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import pt.isec.pa.chess.model.ChessGameManager;

public class RootPane extends HBox { //View-Controller
    ChessGameManager data;
    // variables, including reference to views
    Label lbExample,lbCounter;
    TextField tfExample;
    Button btnExample;

    public RootPane(ChessGameManager data) {
        this.data = data;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        /* create and configure views */
        this.setPadding(new Insets(16));

        lbExample = new Label("Name:");
        tfExample = new TextField();
        btnExample = new Button("Confirm");
        lbCounter = new Label();
        lbCounter.setStyle("-fx-font-family: 'Courier New'; -fx-background-color: #c0c0ff;");

        this.getChildren().addAll(
                lbExample,tfExample,btnExample,lbCounter
        );

        this.setSpacing(10);
        this.setAlignment(Pos.BASELINE_LEFT);
    }

    private void registerHandlers() {
        /* handlers/listeners */
        btnExample.setOnAction(actionEvent -> {
            System.out.println(tfExample.getText());
            data.hello();
            update();
        });
    }

    private void update() {
        /* update views */
        lbCounter.setText(String.format("[%04d]",data.return1()));
    }
}
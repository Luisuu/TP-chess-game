package pt.isec.pa.chess.ui;

import javafx.scene.control.ListView;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import pt.isec.pa.chess.model.ModelLog;

public class LogList extends BorderPane {
    private final ListView<String> listView;

    public LogList() {
        listView = new ListView<>();
        this.setCenter(listView);

        Button btnClear = new Button("Clear Logs");
        btnClear.setOnAction(e -> {
            ModelLog.getInstance().reset();
            listView.getItems().clear();
        });

        HBox bottomBox = new HBox(btnClear);
        bottomBox.setPadding(new Insets(5));
        this.setBottom(bottomBox);
    }

    public void log(String message) {
        listView.getItems().add(message);
    }

    public void clearLogs() {
        listView.getItems().clear();
    }
}

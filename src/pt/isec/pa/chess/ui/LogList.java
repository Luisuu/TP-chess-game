package pt.isec.pa.chess.ui;

import javafx.scene.control.ListView;

public class LogList extends ListView<String> {

    public LogList() {

    }

    public void log(String message) {
        this.getItems().add(message);
    }

    public void clearLogs() {
        this.getItems().clear();
    }
}

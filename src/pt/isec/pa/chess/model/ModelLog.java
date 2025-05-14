package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.pieces.Position;

import java.util.ArrayList;
import java.util.List;

public class ModelLog {
    private static ModelLog _instance = null;

    public static ModelLog getInstance() {
        if (_instance == null) _instance = new ModelLog();
        return _instance;
    }

    protected ArrayList<String> log;

    private ModelLog() {
        log = new ArrayList<>();
    }

    public void reset() {
        log.clear();
    }

    public void log(String msg) {
        log.add(msg);
    }

    public List<String> getLog() {
        return new ArrayList<>(log);
    }

}
package pt.isec.pa.chess.model;


import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeSupport;

public class ModelLog {
    private static ModelLog _instance = null;

    private final PropertyChangeSupport pcs;

    protected ArrayList<String> log;

    private ModelLog() {
        log = new ArrayList<>();
        pcs = new PropertyChangeSupport(this);
    }


    public static ModelLog getInstance() {
        if (_instance == null) _instance = new ModelLog();
        return _instance;
    }

    public void reset() {
        log.clear();
    }

    public void log(String msg) {
        log.add(msg);
        pcs.firePropertyChange("newLog", null, msg);
    }

    public List<String> getLog() {
        return new ArrayList<>(log);
    }

    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        pcs.addPropertyChangeListener("newLog", listener); //regista o listener para a propriedade newLog
    }

}
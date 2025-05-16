package pt.isec.pa.chess.model;


import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeSupport;

public class ModelLog {
    private static ModelLog _instance = null;

    private final PropertyChangeSupport pcs;
    public static final String PROP_NEW_LOG = "newLog";

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
        pcs.firePropertyChange(PROP_NEW_LOG, null, msg);
    }

    public List<String> getLog() {
        return new ArrayList<>(log);
    }

    /*
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(PROP_NEW_LOG, listener);
    }*/

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

}
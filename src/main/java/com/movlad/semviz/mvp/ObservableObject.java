package com.movlad.semviz.mvp;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ObservableObject {

    private final PropertyChangeSupport changeSupport;

    public ObservableObject() {
        changeSupport = new PropertyChangeSupport(this);
    }

    public final void registerListener(PropertyChangeListener l) {
        changeSupport.addPropertyChangeListener(l);
    }

    public final void deregisterListener(PropertyChangeListener l) {
        changeSupport.removePropertyChangeListener(l);
    }

    protected void raisePropertyChanged(String name, Object oldValue, Object newValue) {
        changeSupport.firePropertyChange(name, oldValue, newValue);
    }

}

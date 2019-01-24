package com.movlad.semviz.application;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base controller class that allows modifying a model and then notifying the
 * listeners about the change.
 */
public class Controller {

    protected PropertyChangeSupport changeSupport;

    public Controller() {
        changeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Adds a listener to the property change support contained within this
     * class.
     *
     * @param l the listener to be registered
     */
    public void register(PropertyChangeListener l) {
        changeSupport.addPropertyChangeListener(l);
    }

    /**
     * Removes a listener from the property change support contained within this
     * class.
     *
     * @param l the listener to be unregistered
     */
    public void unregister(PropertyChangeListener l) {
        changeSupport.removePropertyChangeListener(l);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.application;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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

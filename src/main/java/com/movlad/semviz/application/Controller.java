/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.application;

import com.movlad.semviz.core.io.DirectoryLoader;
import com.movlad.semviz.core.io.InvalidDirectoryException;
import com.movlad.semviz.core.semantic.QueryManager;
import com.movlad.semviz.core.semantic.QueryResult;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private boolean isInitialized;

    private final List<String> commands;
    private int commandSelection;

    private QueryManager queryManager;
    private List<QueryResult> queryResults;
    private SuperCloud superCloud;

    private final PropertyChangeSupport changes;

    public Controller() {
        commands = new ArrayList<>();
        commandSelection = -1;
        changes = new PropertyChangeSupport(this);
        isInitialized = false;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void register(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public void deregister(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

    public void load(String path) {
        QueryManager prev = queryManager;

        isInitialized = false;

        if (queryResults != null) {
            queryResults.clear();
        }

        try {
            DirectoryLoader loader = new DirectoryLoader(path);

            loader.load();

            queryManager = new QueryManager(loader);
            isInitialized = true;

            changes.firePropertyChange("load", prev, queryManager);
        } catch (InvalidDirectoryException | NotDirectoryException e) {
            changes.firePropertyChange("error", null, e.getMessage());
        }
    }

    public SuperCloud getSuperCloud() {
        return superCloud;
    }

    public List<QueryResult> getQueryResults() {
        return queryResults;
    }

    public void queryExec(String queryString) {
        commands.add(queryString);

        commandSelection = commands.size();

        List<QueryResult> prev = queryResults;

        try {
            queryResults = queryManager.query(queryString);

            commands.add(queryString);

            superCloud = new SuperCloud(queryManager, queryResults);

            superCloud.load();

            changes.firePropertyChange("queryExec", prev, queryResults);
        } catch (Exception e) {
            changes.firePropertyChange("error", null, e.getMessage());
        }
    }

    public String getCurrentCommand() {
        if (commandSelection == -1) {
            return "";
        }

        return commands.get(commandSelection);
    }

    public void commandBackward() {
        if ((commandSelection - 1) >= 0) {
            commandSelection--;

            changes.firePropertyChange("commandSelection", commandSelection + 1,
                    commandSelection);
        }
    }

    public void commandForward() {
        if ((commandSelection + 1) <= commands.size()) {
            commandSelection++;

            changes.firePropertyChange("commandSelection", commandSelection - 1,
                    commandSelection);
        }
    }

}

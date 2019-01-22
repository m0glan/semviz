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

/**
 * The state of the application at a given time; acts like a controller (a
 * bridge of communication between the view and the model) for the application
 * within the MCV model.
 */
public final class Configuration {

    private boolean isInitialized;

    private final List<String> commands;
    private int commandSelection;

    private QueryManager queryManager;
    private List<QueryResult> queryResults;
    private SuperCloud superCloud;

    private final PropertyChangeSupport changes;

    public Configuration() {
        commands = new ArrayList<>();
        commandSelection = -1;
        changes = new PropertyChangeSupport(this);
        isInitialized = false;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Adds a listener to the property change support contained within this
     * class.
     *
     * @param l the listener to be registered
     */
    public void register(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    /**
     * Removes a listener from the property change support contained within this
     * class.
     *
     * @param l the listener to be unregistered
     */
    public void unregister(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

    /**
     * Given a Semviz directory, this method loads a
     * {@link com.movlad.semviz.core.semantic.QueryManager} from the ontology
     * file within it and notifies the property change listeners of the success
     * or failure.
     *
     * @param path is the path to the Semviz directory
     */
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
            changes.firePropertyChange("loadError", null, e.getMessage());
        }
    }

    public SuperCloud getSuperCloud() {
        return superCloud;
    }

    public List<QueryResult> getQueryResults() {
        return queryResults;
    }

    /**
     * Executes a query on the currently loaded
     * {@link com.movlad.semviz.core.semantic.QueryManager}, generates a
     * {@link com.movlad.semviz.application.SuperCloud} formed by all the clouds
     * retrieved using the query.
     *
     * @param queryString is the string of the query to be executed
     */
    public void queryExec(String queryString) {
        commands.add(queryString);

        commandSelection = commands.size() - 1;

        List<QueryResult> prev = queryResults;

        try {
            queryResults = queryManager.query(queryString);

            commands.add(queryString);

            superCloud = new SuperCloud(queryManager, queryResults);

            superCloud.load();

            changes.firePropertyChange("queryExec", prev, queryResults);
        } catch (Exception e) {
            changes.firePropertyChange("queryExecError", null, e.getMessage());
        }
    }

    public String getCurrentCommand() {
        if (commandSelection < 0 || commandSelection > (commands.size() - 1)) {
            return "";
        }

        return commands.get(commandSelection);
    }

    /**
     * Navigates to a previous command.
     */
    public void commandBackward() {
        if ((commandSelection - 1) >= 0) {
            commandSelection--;

            changes.firePropertyChange("commandSelection", commandSelection + 1,
                    commandSelection);
        }
    }

    /**
     * Navigates to a more recent command.
     */
    public void commandForward() {
        if ((commandSelection + 1) <= commands.size()) {
            commandSelection++;

            changes.firePropertyChange("commandSelection", commandSelection - 1,
                    commandSelection);
        }
    }

}

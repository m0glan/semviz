package com.movlad.semviz.application;

import com.movlad.semviz.core.io.InvalidDirectoryException;
import com.movlad.semviz.core.sqm.SQM;
import com.movlad.semviz.core.sqm.SemanticCloudDescription;
import java.io.FileNotFoundException;
import java.nio.file.NotDirectoryException;
import java.util.List;

/**
 * Controller for the Semviz Query Manager.
 */
public final class SQMController extends Controller {

    private static SQMController instance = null;
    private final SQM sqm;  // the unique instance of the Semviz Query Manager
    private List<SemanticCloudDescription> descriptions;    // the list of current query results
    private int selectedDescriptionIndex;   // the currently selected Description

    private SQMController() {
        sqm = SQM.getInstance();
        selectedDescriptionIndex = -1;
    }

    public static SQMController getInstance() {
        if (instance == null) {
            instance = new SQMController();
        }

        return instance;
    }

    /**
     * @return the currently selected description
     */
    public SemanticCloudDescription getSelectedDescription() {
        if (selectedDescriptionIndex > -1
                && selectedDescriptionIndex < descriptions.size()) {
            return descriptions.get(selectedDescriptionIndex);
        }

        return null;
    }

    /**
     * Given a Semviz directory, this method loads a Semviz directory into the
     * Semviz query manager singleton.
     *
     * @param path is the path to the Semviz directory
     */
    public void load(String path) {
        if (descriptions != null) {
            descriptions.clear();
        }

        try {
            sqm.load(path);

            changeSupport.firePropertyChange("SQMLoadSuccess", null, sqm);
        } catch (InvalidDirectoryException | NotDirectoryException | FileNotFoundException e) {
            changeSupport.firePropertyChange("SQMLoadError", null, e.getMessage());
        }
    }

    /**
     * Executes a query on the currently loaded Semviz directory.
     *
     * @param queryString is the string of the query to be executed
     */
    public void exec(String queryString) {
        List<SemanticCloudDescription> prevDescriptions = descriptions;

        try {
            changeSupport.firePropertyChange("SQMExecutionStarted", null, sqm);

            descriptions = sqm.exec(queryString);

            changeSupport.firePropertyChange("SQMFailCountChanged", null, sqm.getFailCount());
            changeSupport.firePropertyChange("SQMExecutionSuccess", prevDescriptions, descriptions);
        } catch (Exception e) {
            changeSupport.firePropertyChange("SQMExecutionError", null, e.getMessage());
        }
    }

    /**
     * Sets the selected semantic cloud description index.
     *
     * @param i is the index of the selected description
     */
    public void setSelectedDescriptionIndex(int i) {
        int prev = selectedDescriptionIndex;

        selectedDescriptionIndex = i;

        changeSupport.firePropertyChange("SQMDescriptionIndexChanged", prev,
                selectedDescriptionIndex);
    }

}

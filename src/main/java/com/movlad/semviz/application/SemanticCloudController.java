package com.movlad.semviz.application;

import com.movlad.semviz.core.semantic.QueryManager;
import com.movlad.semviz.core.semantic.QueryResult;
import com.movlad.semviz.core.semantic.SemanticCloud;
import java.util.List;

/**
 * Controller that notifies listeners about change concerning a loaded semantic
 * cloud.
 */
public class SemanticCloudController extends Controller {

    private SemanticCloud semanticCloud;

    /**
     * Loads a semantic cloud from a list of results issued from a query manager
     * and notifies listeners.
     *
     * @param queryManager contains the model from which the results are issued
     * @param queryResults are the results issued from a SPARQL query
     */
    public void loadSuperCloud(QueryManager queryManager, List<QueryResult> queryResults) {
        SemanticCloud prev = semanticCloud;

        semanticCloud = new SemanticCloud(queryManager, queryResults);

        semanticCloud.load();
        changeSupport.firePropertyChange("SemanticCloudChange", prev, semanticCloud);
    }

}

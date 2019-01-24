package com.movlad.semviz.application;

import com.movlad.semviz.core.io.DirectoryLoader;
import com.movlad.semviz.core.io.InvalidDirectoryException;
import com.movlad.semviz.core.semantic.QueryManager;
import com.movlad.semviz.core.semantic.QueryResult;
import java.nio.file.NotDirectoryException;
import java.util.List;

/**
 * Controller that creates a bridge between the view and the query manager
 * model; SPARQL queries can be executed through this controller.
 */
public class QueryManagerController extends Controller {

    private QueryManager queryManager;
    private List<QueryResult> queryResults;

    public QueryManager getQueryManager() {
        return queryManager;
    }

    public List<QueryResult> getQueryResults() {
        return queryResults;
    }

    /**
     * Given a Semviz directory, this method loads a
     * {@link com.movlad.semviz.core.semantic.QueryManager} from the ontology
     * file within it and notifies the property change listeners of the success
     * or failure.
     *
     * @param path is the path to the Semviz directory
     */
    public void loadQueryManager(String path) {
        QueryManager prev = queryManager;

        queryManager = null;

        if (queryResults != null) {
            queryResults.clear();
        }

        try {
            DirectoryLoader loader = new DirectoryLoader(path);

            loader.load();

            queryManager = new QueryManager(loader);

            changeSupport.firePropertyChange("QueryManagerLoadSuccess", prev, queryManager);
        } catch (InvalidDirectoryException | NotDirectoryException e) {
            changeSupport.firePropertyChange("QueryManagerLoadError", null, e.getMessage());
        }
    }

    /**
     * Executes a query on the currently loaded
     * {@link com.movlad.semviz.core.semantic.QueryManager}, generates a
     * {@link com.movlad.semviz.core.semantic.SemanticCloud} formed by all the
     * clouds retrieved using the query.
     *
     * @param queryString is the string of the query to be executed
     */
    public void executeQuery(String queryString) {
        List<QueryResult> prev = queryResults;

        try {
            queryResults = queryManager.query(queryString);

            changeSupport.firePropertyChange("QueryExecutionSuccess", prev, queryResults);
        } catch (Exception e) {
            changeSupport.firePropertyChange("QueryExecutionError", null, e.getMessage());
        }
    }

}

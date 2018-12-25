package com.movlad.semviz.core.semantic;

import java.util.Iterator;
import java.util.List;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

/**
 * Helper for executing a cloud selection query. Multiple clouds can be selected.
 */
public class SemvizQueryExecution implements Iterable<QuerySolution> {

    private String queryString;
    private OntModel model;
    private List<QuerySolution> solutions;
    private String solutionsToString;

    /**
     * @param queryString is the SPARQL query to be executed; must contain a variable {@code ?cloud}
     * @param model is the ontology model containing cloud-related information
     */
    SemvizQueryExecution(String queryString, OntModel model) {
        this.queryString = queryString;
        this.model = model;
    }

    @Override
    public String toString() {
        return solutionsToString;
    }

    /**
     * @return true if the query is not yet executed or the result set does not contain any solutions
     */
    public boolean isNullOrEmpty() {
        return solutions == null || solutions.isEmpty();
    }

    /**
     * Executes the {@code select} query and stores the result.
     * 
     * @throws SemvizQueryException if the query string does not contain a {@code ?cloud} variable
     */
    public void exec() throws SemvizQueryException {
        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
        ResultSet resultSet = queryExecution.execSelect();

        solutions = ResultSetFormatter.toList(resultSet);

        if (!solutions.isEmpty()) {
            if (!solutions.get(0).contains("?cloud")) {
                solutions = null;

                throw new SemvizQueryException("Not a cloud selection query.");
            }
        }

        solutionsToString = ResultSetFormatter.asText(resultSet);
    }

    @Override
    public Iterator<QuerySolution> iterator() {
        return solutions.iterator();
    }
	
}

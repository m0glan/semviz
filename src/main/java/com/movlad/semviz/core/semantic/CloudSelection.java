package com.movlad.semviz.core.semantic;

import java.util.Iterator;
import java.util.List;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryException;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFormatter;

class CloudSelection implements Iterable<QuerySolution> {

	String queryString;
	OntModel model;
	List<QuerySolution> solutions;
	
	public CloudSelection(String queryString, OntModel model) {
		this.queryString = queryString;
		this.model = model;
	}
	
	public boolean isNullOrEmpty() {
		return solutions == null || solutions.isEmpty();
	}
	
	public void exec() {
		Query query = QueryFactory.create(queryString);
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		
		solutions = ResultSetFormatter.toList(queryExecution.execSelect());
		
		if (!solutions.isEmpty()) {
			if (!solutions.get(0).contains("?cloud")) {
				throw new QueryException("Not a cloud selection query.");
			}
		}
	}

	@Override
	public Iterator<QuerySolution> iterator() {
		return solutions.iterator();
	}
	
}

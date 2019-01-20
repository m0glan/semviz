package com.movlad.semviz.core.semantic;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;

import java.util.*;

public class QueryExecutor {

    private Query query;
    private OntModel model;

    public QueryExecutor(Query query, OntModel model) {
        this.query = query;
        this.model = model;
    }

    public List<QueryResult> exec() {
        QueryExecution execution = QueryExecutionFactory.create(query.getJenaQuery(), model);
        ResultSet results = execution.execSelect();
        List<QuerySolution> solutions = ResultSetFormatter.toList(results);
        List<QueryResult> queryResults = new ArrayList<>();

        solutions.forEach(solution -> {
            Iterator<String> varNameIt = solution.varNames();
            QueryResult result = new QueryResult();

            while (varNameIt.hasNext()) {
                String varName = varNameIt.next();
                RDFNode node = solution.get(varName);
                Individual cloud = validateCloudResource(node);

                if (cloud != null) {
                    result.setCloud(cloud);
                } else {
                    result.putAttribute(varName, node.toString());
                }
            }

            if (result.getCloud() != null) {
                queryResults.add(result);
            }
        });

        return queryResults;
    }

    private Individual validateCloudResource(RDFNode node) {
        if (node.isResource()) {
            String uri = node.asResource().getURI();
            Individual individual = model.getIndividual(uri);
            OntClass ontClass = model.getOntClass(model.getNsPrefixURI("knowdip") + "Point-Cloud");

            if (individual.getOntClass().hasSuperClass(ontClass)) {
                return individual;
            }
        }

        return null;
    }

}

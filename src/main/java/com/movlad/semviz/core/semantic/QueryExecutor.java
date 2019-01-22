package com.movlad.semviz.core.semantic;

import java.util.*;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

class QueryExecutor {

    private final Query query;
    private final OntModel model;

    public QueryExecutor(Query query, OntModel model) {
        this.query = query;
        this.model = model;
    }

    public List<QueryResult> exec() {
        QueryExecution execution = QueryExecutionFactory.create(query, model);
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
                    result.setIndividual(cloud);
                } else {
                    if (node.isResource()) {
                        result.putAttribute(varName, ((Resource) node).getURI());
                    } else {
                        result.putAttribute(varName, ((Literal) node).getLexicalForm());
                    }
                }
            }

            if (result.getIndividual() != null) {
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

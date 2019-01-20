package com.movlad.semviz.core.semantic;

import org.junit.jupiter.api.Test;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class QueryExecutorTest {

    @Test
    void execTest() {
        OntModel model = ModelFactory.createOntologyModel();
        InputStream is = getClass().getClassLoader().getResourceAsStream("ontology/knowdip.owl");

        model.read(is, null);

        Query query = new Query("select ?cloud ?size { ?cloud knowdip:hasSize ?size . filter(?size > 1000) }");
        QueryExecutor queryExecutor = new QueryExecutor(query, model);

        queryExecutor.exec();
    }

}
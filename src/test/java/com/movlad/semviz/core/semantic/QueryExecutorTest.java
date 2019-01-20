package com.movlad.semviz.core.semantic;

import java.io.InputStream;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.jupiter.api.Test;

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

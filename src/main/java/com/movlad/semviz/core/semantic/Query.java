package com.movlad.semviz.core.semantic;

import org.apache.jena.query.QueryFactory;

public class Query {

    private static final String NS = "http://lab.ponciano.info/knowdip#";
    private static final String PREFIX = "PREFIX knowdip: <" + NS + ">";

    private org.apache.jena.query.Query query;

    public Query(String queryString) {
        query = QueryFactory.create(PREFIX + System.lineSeparator() + queryString);
    }

    public org.apache.jena.query.Query getJenaQuery() {
        return query;
    }

}

package com.movlad.semviz.core.semantic;

import org.apache.jena.ontology.Individual;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QueryResult {

    private Individual individual;
    private Map<String, String> attributes;

    public QueryResult() {
        individual = null;
        attributes = new HashMap<>();
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual cloud) {
        this.individual = cloud;
    }

    public void putAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public Set<String> getKeys() {
        return attributes.keySet();
    }

    public Collection<String> getValues() {
        return attributes.values();
    }

}

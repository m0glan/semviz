package com.movlad.semviz.core.sqm;

import com.movlad.semviz.core.math.geometry.PointCloud;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.apache.jena.ontology.Individual;

public class SemanticCloudDescription {

    private final Individual individual;
    private final Map<String, String> attributes;
    private final PointCloud cloud;

    public SemanticCloudDescription(Individual individual, Map<String, String> attributes,
            PointCloud cloud) {
        this.individual = individual;
        this.attributes = attributes;
        this.cloud = cloud;
    }

    public Individual getIndividual() {
        return individual;
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public Set<String> attributeKeySet() {
        return attributes.keySet();
    }

    public Collection<String> attributeValues() {
        return attributes.values();
    }

    public PointCloud getCloud() {
        return cloud;
    }

    @Override
    public String toString() {
        return individual.getLocalName();
    }

}

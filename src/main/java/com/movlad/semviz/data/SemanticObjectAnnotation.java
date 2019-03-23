package com.movlad.semviz.data;

import java.util.Map;
import org.apache.jena.ontology.Individual;

public class SemanticObjectAnnotation<T> {

    private String uri;
    private Map<String, String> annotations;
    private T annotatedObject;

    public SemanticObjectAnnotation(String uri, Individual individual, Map<String, String> annotations,
            T object) {
        this.uri = uri;
        this.annotations = annotations;
        this.annotatedObject = object;
    }

    public String getURI() {
        return uri;
    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Map<String, String> annotations) {
        this.annotations = annotations;
    }

    public T getAnnotatedObject() {
        return annotatedObject;
    }

    public void setAnnotatedObject(T annotatedObject) {
        this.annotatedObject = annotatedObject;
    }

}

package com.movlad.semviz.service.data;

import com.movlad.semviz.data.SemanticObjectAnnotation;
import java.util.List;
import java.util.Map;

public interface ISemanticObjectAnnotationService<T> {

    SemanticObjectAnnotation<T> Create(String uri, T annotatedObject, Map<String, String> annotations);

    SemanticObjectAnnotation<T> Get(String uri);

    List<SemanticObjectAnnotation<T>> GetAll();

    SemanticObjectAnnotation<T> Update(String uri, T annotatedObject, Map<String, String> annotations);

    void Delete(String uri);

}

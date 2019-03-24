package com.movlad.semviz.service.data;

import com.movlad.semviz.data.SemanticAnnotation;
import java.util.List;

public interface ISemanticObjectAnnotationService<T> {

    List<SemanticAnnotation<T>> query(String query);

}

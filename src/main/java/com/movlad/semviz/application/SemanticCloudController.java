package com.movlad.semviz.application;

import com.movlad.semviz.core.semantic.QueryManager;
import com.movlad.semviz.core.semantic.QueryResult;
import com.movlad.semviz.core.semantic.SemanticCloud;
import java.util.List;

public class SemanticCloudController extends Controller {

    private SemanticCloud semanticCloud;

    public void loadSuperCloud(QueryManager queryManager, List<QueryResult> queryResults) {
        SemanticCloud prev = semanticCloud;

        semanticCloud = new SemanticCloud(queryManager, queryResults);

        semanticCloud.load();
        changeSupport.firePropertyChange("SemanticCloudChange", prev, semanticCloud);
    }

}

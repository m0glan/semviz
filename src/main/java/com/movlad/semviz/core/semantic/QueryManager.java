/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.core.semantic;

import com.movlad.semviz.core.io.CloudLoader;
import com.movlad.semviz.core.io.InvalidDirectoryException;
import com.movlad.semviz.core.io.DirectoryLoader;
import com.movlad.semviz.core.math.geometry.PointCloud;
import java.io.IOException;
import java.util.List;
import org.apache.jena.ontology.Individual;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;

public class QueryManager {

    private static final String NS = "http://lab.ponciano.info/knowdip#";
    private static final String PREFIX = "PREFIX knowdip: <" + NS + ">";

    DirectoryLoader dir;

    public QueryManager(DirectoryLoader dir) throws InvalidDirectoryException {
        if (!dir.isValid()) {
            throw new InvalidDirectoryException("Invalid parameter directory.");
        }

        this.dir = dir;
    }

    /**
     * Queries the loaded model and notifies the observers of a change in
     * results.Updates the {@code CanvasController}.
     *
     * @param queryString is the SPARQL query
     * @return a list of query results
     */
    public List<QueryResult> query(String queryString) {
        Query query = QueryFactory.create(PREFIX + System.lineSeparator() + queryString);
        QueryExecutor queryExec = new QueryExecutor(query, dir.getModel());

        return queryExec.exec();
    }

    /**
     * @param cloudIndividual is the instance containing the semantic
     * description of a cloud
     * @return point cloud semantically described by the individual in parameter
     */
    public PointCloud retrieve(Individual cloudIndividual) {
        String pathToCloud = dir.getCloudsDir().getAbsolutePath()
                + "/" + cloudIndividual.getLocalName() + ".txt";
        CloudLoader loader = new CloudLoader(pathToCloud, true);

        try {
            loader.load();
        } catch (IOException e) {
            return null;
        }

        return loader.getCloud();
    }

}

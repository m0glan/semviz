package com.movlad.semviz.application;

import com.movlad.semviz.core.io.CloudLoader;
import com.movlad.semviz.core.math.geometry.PointCloud;
import com.movlad.semviz.core.semantic.Query;
import com.movlad.semviz.core.semantic.QueryExecutor;
import com.movlad.semviz.core.semantic.QueryResult;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.*;
import java.util.List;
import java.util.Observable;

/**
 * Helps load a directory as being correctly structured as a Semviz dataIt source.
 */
class Configuration extends Observable {

    private final ViewManager viewManager;
    private OntModel model;
    private File cloudsDir;
    private List<QueryResult> data;
    private QueryResult dataSelection;

    /**
     * @param dirPath     is the path to the directory
     * @param viewManager is the class containing current cloud display data
     * @throws InvalidDirectoryException if something goes wrong when loading a directory
     */
    public Configuration(String dirPath, ViewManager viewManager) throws InvalidDirectoryException {
        if (!validateDir(dirPath))
            throw new InvalidDirectoryException("Something went wrong when trying on load.");

        this.viewManager = viewManager;
        this.dataSelection = null;
    }

    public List<QueryResult> getData() {
        return data;
    }

    public QueryResult getDataSelection() {
        return dataSelection;
    }

    public void setDataSelection(int i) {
        dataSelection = data.get(i);

        setChanged();
        notifyObservers();
    }

    /**
     * Called within the constructor, this method checks whether the path in the parameter
     * leads to a valid Semviz directory.
     *
     * @param dirPath is the path that leads to the directory
     */
    private boolean validateDir(String dirPath) {
        this.model = null;

        File dir = new File(dirPath);

        if (!dir.exists()) {
            return false;
        }

        if (!dir.isDirectory()) {
            return false;
        }

        model = null;

        return containsValidOntology(dir) && containsCloudsDir(dir);
    }

    /**
     * Queries the loaded model and notifies the observers of a change in results.
     * Updates the {@code ViewManager}.
     *
     * @param queryString is the SPARQL query
     */
    public void query(String queryString) {
        Query query = new Query(queryString);
        QueryExecutor queryExec = new QueryExecutor(query, model);

        data = queryExec.exec();

        viewManager.stop();
        viewManager.clear();

        data.forEach(segment -> {
            PointCloud cloud = retrieve(segment.getCloudInd());

            viewManager.add(new View(cloud));
        });

        setChanged();
        notifyObservers();
    }

    /**
     * @param cloudIndividual is the instance containing the semantic description of a cloud
     * @return point cloud semantically described by the individual in parameter
     */
    private PointCloud retrieve(Individual cloudIndividual) {
        String pathToCloud = cloudsDir.getAbsolutePath() + "/" + cloudIndividual.getLocalName() + ".txt";
        CloudLoader loader = new CloudLoader(pathToCloud, true);

        try {
            loader.load();
        } catch (IOException e) {
            return null;
        }

        return loader.getCloud();
    }

    private void setVisibilityAt(int i, boolean isVisible) {
        viewManager.setVisibilityAt(i, isVisible);

        setChanged();
        notifyObservers();
    }

    /**
     * @param dir gives the array of files in the directory
     * @returns true if the a Knowdip ontology is found in the directory and it is valid
     */
    private boolean containsValidOntology(File dir) {
        File[] files = dir.listFiles();

        for (File file : files)
            if (file.getName().toLowerCase().endsWith(".owl")) {
                model = ModelFactory.createOntologyModel();

                FileInputStream is = null;

                try {
                    is = new FileInputStream(file.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    return false;
                }

                model.read(is, null);

                if (model.getNsPrefixURI("knowdip") == null) {
                    model = null;

                    return false;
                }

                return true;
            }

        return false;
    }

    /**
     * @param dir gives the array of files in the directory
     * @returns true if the directory contains a cloud folder
     */
    private boolean containsCloudsDir(File dir) {
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.getName().toLowerCase().equals("clouds")) {
                if (file.isDirectory()) {
                    cloudsDir = file;

                    return true;
                }
            }
        }

        return false;
    }

}

package com.movlad.semviz.application;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.*;
import java.nio.file.NotDirectoryException;

/**
 * Helps load a directory as being correctly structured as a Semviz data source.
 */
class Configuration implements Serializable {

    private OntModel model;

    /**
     * @param path is the path to the directory
     */
    public Configuration(String path) throws FileNotFoundException, NotDirectoryException, InvalidDirectoryException {
        this.model = null;

        File dir = new File(path);

        if (!dir.exists()) {
            throw new FileNotFoundException("Directory at given path does not exist.");
        }

        if (!dir.isDirectory()) {
            throw new NotDirectoryException("Not a directory.");
        }

        model = null;

        if (!containsValidOntology(dir) || !containsCloudsDir(dir)) {
            throw new InvalidDirectoryException("A directory must contain a Knowdip OWL file and a directory containing clouds.");
        }
    }

    /**
     * @param dir gives the array of files in the directory
     * @throws InvalidDirectoryException if the directory is not correctly structured as a Semviz source
     * @returns true if the a Knowdip ontology is found in the directory and it is valid
     */
    private boolean containsValidOntology(File dir) throws FileNotFoundException {
        File[] files = dir.listFiles();

        for (File file : files)
            if (file.getName().toLowerCase().endsWith(".owl")) {
                model = ModelFactory.createOntologyModel();

                FileInputStream is = new FileInputStream(file.getAbsolutePath());

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
     * @throws InvalidDirectoryException if the directory is not correctly structured as a Semviz source
     * @returns true if the directory contains a cloud folder
     */
    private boolean containsCloudsDir(File dir) throws InvalidDirectoryException {
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.getName().toLowerCase().equals("clouds")) {
                if (file.isDirectory()) {
                    return true;
                }
            }
        }

        return false;
    }

}

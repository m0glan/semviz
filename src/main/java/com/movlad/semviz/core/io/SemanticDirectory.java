/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.NotDirectoryException;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

public class SemanticDirectory {

    private String dirPath;
    private OntModel model;
    private File cloudsDir;
    private boolean isValid;

    /**
     * @param dirPath is the path to the directory
     */
    public SemanticDirectory(String dirPath) {
        this.dirPath = dirPath;
        this.isValid = false;
    }

    /**
     * @throws InvalidDirectoryException if something goes wrong when loading a
     * directory
     * @throws java.nio.file.NotDirectoryException
     */
    public void load() throws InvalidDirectoryException, NotDirectoryException {
        this.model = null;

        File dir = new File(dirPath);

        if (!dir.exists()) {
            throw new InvalidDirectoryException("Directory does not exist");
        }

        if (!dir.isDirectory()) {
            throw new NotDirectoryException("File is not a directory.");
        }

        model = null;

        if (!containsValidOntology(dir) || !containsCloudsDir(dir)) {
            throw new InvalidDirectoryException("Invalid directory structure.");
        }

        this.isValid = true;
    }

    public boolean isValid() {
        return isValid;
    }

    public OntModel getModel() {
        return model;
    }

    public File getCloudsDir() {
        return cloudsDir;
    }

    /**
     * Called within the constructor, this method checks whether the path in the
     * parameter leads to a valid Semviz directory.
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
     * @param dir gives the array of files in the directory
     * @returns true if the a Knowdip ontology is found in the directory and it
     * is valid
     */
    private boolean containsValidOntology(File dir) {
        File[] files = dir.listFiles();

        for (File file : files) {
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

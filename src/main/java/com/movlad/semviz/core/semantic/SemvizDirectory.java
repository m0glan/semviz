package com.movlad.semviz.core.semantic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NotDirectoryException;

/**
 * Helps validate a directory as being correctly structured as a Semviz data source.
 */
class SemvizDirectory {

    private String ontologyPath;
    private String cloudsPath;

    /**
     * @param path is the path to the directory
     * @throws IOException if the file is not found
     * @throws NotSemvizDirectoryException if path does not point to a Semviz source
     */
    public SemvizDirectory(String path) throws IOException, NotSemvizDirectoryException {
        File semvizDir = new File(path);

        ontologyPath = "";
        cloudsPath = "";

        if (!semvizDir.exists()) {
            throw new FileNotFoundException("Invalid path.");
        }

        if (!semvizDir.isDirectory()) {
            throw new NotDirectoryException("Not a directory.");
        }

        File[] files = semvizDir.listFiles();

        ontologyPath = findOntologyPath(files);
        cloudsPath = findCloudsPath(files);

        if (ontologyPath == null || cloudsPath == null) {
            throw new NotSemvizDirectoryException("Invalid Semviz directory structure.");
        }
    }

    public String getOntologyPath() { return ontologyPath; }

    public String getCloudsPath() { return cloudsPath; }

    /**
     * @param files is an array of files in the directory
     * @return the path of the ontology file in the directory
     * @throws NotSemvizDirectoryException if the directory is not correctly structured as a Semviz source
     */
    private String findOntologyPath(File[] files) throws NotSemvizDirectoryException {
        String ontologyPath = null;

        for (File file : files) {
            if (file.getName().toLowerCase().endsWith(".owl")) {
                if (ontologyPath != null) {
                    throw new NotSemvizDirectoryException("A Semviz directory can only contain one ontology file.");
                } else {
                    ontologyPath = file.getAbsolutePath();
                }
            }
        }

        return ontologyPath;
    }

    /**
     * @param files is an array of files in the directory
     * @return the path containing the cloud {@code .txt} files
     * @throws NotSemvizDirectoryException if the directory is not correctly structured as a Semviz source
     */
    private String findCloudsPath(File[] files) throws NotSemvizDirectoryException {
        String cloudsPath = null;

        for (File file : files) {
            if (file.getName().toLowerCase().equals("clouds")) {
                if (cloudsPath != null) {
                    throw new NotSemvizDirectoryException("A Semviz directory can only contain one directory containing clouds.");
                } else {
                    if (file.isDirectory()) {
                        cloudsPath = file.getAbsolutePath();
                    }
                }
            }
        }

        return cloudsPath;
    }
	
}

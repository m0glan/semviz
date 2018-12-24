package com.movlad.semviz.core.semantic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NotDirectoryException;

class SemvizDirectory {

	private String ontologyPath;
	private String cloudsPath;
	
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

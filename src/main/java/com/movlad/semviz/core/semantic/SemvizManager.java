package com.movlad.semviz.core.semantic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.ModelFactory;

public class SemvizManager {

	private static SemvizManager instance = null;
	
	private SemvizDirectory sourceDir;
	private OntModel model;
	
	private boolean isInitialized = false;
	
	private SemvizManager() { }
	
	public static SemvizManager get() {
		if (instance == null) {
			instance = new SemvizManager();
		}
		
		return instance;
	}
	
	public void load(String path) throws IOException, NotSemvizDirectoryException, SemvizOntologyException {
		isInitialized = false;
		
		sourceDir = new SemvizDirectory(path);
		
		InputStream is = new FileInputStream(new File(sourceDir.getOntologyPath()));
		OntModel tempModel = ModelFactory.createOntologyModel();
		
		tempModel.read(is, null);
		
		if (tempModel.getNsPrefixURI("knowdip") == null) {
			throw new SemvizOntologyException("The ontology detected in the given folder is not a valid Semviz/Knowdip ontology.");
		}
		
		model = tempModel;
		
		isInitialized = true;
	}
	
}

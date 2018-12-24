package com.movlad.semviz.core.semantic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

import com.movlad.semviz.core.SemvizException;
import com.movlad.semviz.core.io.CloudLoader;
import com.movlad.semviz.core.math.geometry.PointCloud;

/**
 * Singleton for various Semviz operations: loading a Semviz data directory,
 * launching a query, retrieving a point cloud from its URI etc..
 */
public class SemvizManager {

	private static SemvizManager instance = null;
	
	private static final String PREFIX = "PREFIX knowdip: <http://lab.ponciano.info/knowdip#>";
	
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
	
	/**
	 * Loads a Semviz data directory.
	 * 
	 * @param path is the path to the directory.
	 * @throws IOException if the folder cannot be opened or it does not exists
	 * @throws NotSemvizDirectoryException if the folder does not correspond to a Semviz directory
	 * @throws SemvizOntologyException if the ontology is not a valid Semviz ontology
	 */
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
	
	/**
	 * Executes a cloud selection query on the loaded model.
	 * 
	 * @param queryString is the cloud selection query
	 * @return the cloud selection (can be multiple clouds)
	 * @throws SemvizException if the manager is not initialized
	 */
	public CloudSelection query(String queryString) throws SemvizException {
		if (!isInitialized) {
			throw new SemvizException("Load a valid Semviz data directory.");
		}
		
		CloudSelection cloudSelection = new CloudSelection(PREFIX + System.lineSeparator() + queryString, model);
		
		cloudSelection.exec();
		
		return cloudSelection;
	}
	
	/**
	 * Retrieves a point cloud from its text file by its URI in the ontology.
	 * 
	 * @param cloudURI is the URI of the cloud in the ontology
	 * @param normalsIncluded is true if the file contains normals
	 * @return the structure containing cloud data
	 * @throws IOException if the cloud file is not found
	 * @throws SemvizException if the manager is not initialized with a valid Semviz data directory
	 */
	public PointCloud retrieve(String cloudURI, boolean normalsIncluded) throws IOException, SemvizException {
		if (!isInitialized) {
			throw new SemvizException("Load a valid Semviz data directory.");
		}
		
		Individual cloudInd = model.getIndividual(cloudURI);
		
		if (cloudInd == null) {
			throw new SemvizOntologyException("Cloud not present in the Ontology.");
		}
		
		String cloudPath = sourceDir.getCloudsPath() + "/" + cloudInd.getLocalName() + ".txt";
		CloudLoader loader = new CloudLoader(cloudPath, normalsIncluded);
		
		loader.load();
		
		return loader.getCloud();
	}
	
}

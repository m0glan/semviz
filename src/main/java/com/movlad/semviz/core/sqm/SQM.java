package com.movlad.semviz.core.sqm;

import com.movlad.semviz.core.io.CloudLoader;
import com.movlad.semviz.core.io.InvalidDirectoryException;
import com.movlad.semviz.core.math.geometry.PointCloud;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

/**
 * The Semviz Query Manager ({@code SQM}) is a singleton used for loading a
 * Semviz directory and retrieving semantic descriptions of clouds using SPARQL
 * queries.
 */
public class SQM {

    private static SQM instance = null;
    private OntModel model;
    private String path;
    private int failCount;

    private SQM() {
        failCount = 0;
    }

    public static SQM getInstance() {
        if (instance == null) {
            instance = new SQM();
        }

        return instance;
    }

    /**
     * @return the active ontology model currently loaded within the query
     * manager
     */
    public OntModel getModel() {
        return model;
    }

    /**
     * @return the path to the active Semviz directory
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the number of {@code SemanticCloudDescription} instances that
     * could not be retrieved on the last query
     */
    public int getFailCount() {
        return failCount;
    }

    /**
     * Attempts to load a Semviz directory.
     *
     * @param path is the absolute path to the Semviz directory to be loaded
     * @throws FileNotFoundException if the file does not exist
     * @throws NotDirectoryException if the file at the given path is not a
     * directory
     * @throws InvalidDirectoryException if the directory does not contain an
     * ontology
     */
    public void load(String path) throws InvalidDirectoryException, NotDirectoryException,
            FileNotFoundException {
        failCount = 0;
        model = null;

        File directory = new File(path);

        if (!directory.exists()) {
            throw new InvalidDirectoryException("Directory does not exist");
        }

        if (!directory.isDirectory()) {
            throw new NotDirectoryException("File is not a directory.");
        }

        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.getName().toLowerCase().endsWith(".owl")) {
                FileInputStream is = new FileInputStream(file.getAbsolutePath());

                model = ModelFactory.createOntologyModel();

                model.read(is, null);
            }
        }

        if (model == null) {
            throw new InvalidDirectoryException("Not a valid directory.");
        }

        this.path = path;
    }

    /**
     * Executes a SPARQL query on the active Semviz directory and returns the
     * list of successfully retrieved instances of
     * {@code SemanticCloudDescription}.
     *
     * @param queryString is the SPARQL query to be executed
     * @return the list of successfully retrieved instances of
     * {@code SemanticCloudDescription}.
     */
    public List<SemanticCloudDescription> exec(String queryString) {
        failCount = 0;

        List<SemanticCloudDescription> descriptions = new ArrayList<>();
        String prefixes = generatePrefixes();
        Query query = QueryFactory.create(prefixes + queryString);
        QueryExecution execution = QueryExecutionFactory.create(query, model);
        ResultSet results = execution.execSelect();

        while (results.hasNext()) {
            QuerySolution solution = results.next();

            SemanticCloudDescription result = processQuerySolution(solution);

            if (result != null) {
                descriptions.add(result);
            } else {
                failCount++;
            }
        }

        return descriptions;
    }

    /**
     * @return a String containing all the prefixes generated from the
     * namespaces found in the active ontology
     */
    private String generatePrefixes() {
        Map<String, String> namespaceMap = model.getNsPrefixMap();
        String prefixes = new String();

        prefixes = namespaceMap.keySet().stream().map((key) -> "PREFIX " + key
                + ": <" + namespaceMap.get(key) + ">"
                + System.lineSeparator()).reduce(prefixes, String::concat);

        return prefixes;
    }

    /**
     * Maps an instance of {@code SemanticCloudDescription} to an instance of
     * {@code QuerySolution}.
     *
     * @param solution is the instance of {@code QuerySolution} to be processed
     * @return an instance of {@code SemanticCloudDescription} that corresponds
     * to the parameter {@code QuerySolution}
     */
    private SemanticCloudDescription processQuerySolution(QuerySolution solution) {
        SemanticCloudDescription result;
        Iterator<String> varNameIt = solution.varNames();
        Individual individual = null;
        Map<String, String> attributes = new HashMap<>();
        PointCloud cloud = null;

        while (varNameIt.hasNext()) {
            String varName = varNameIt.next();
            RDFNode node = solution.get(varName);

            individual = validateCloudResource(node);

            if (individual == null) {
                if (node.isResource()) {
                    attributes.put(varName, ((Resource) node).getURI());
                } else {
                    attributes.put(varName, ((Literal) node).getLexicalForm());
                }
            } else {
                cloud = retrieveCloud(individual);
            }
        }

        if ((individual == null) || (cloud == null)) {
            return null;
        }

        result = new SemanticCloudDescription(individual, attributes, cloud);

        return result;
    }

    /**
     * Validates an RDF node as a point cloud resource by checking whether its
     * local name contains {@code PointCloud}.
     *
     * @param node is the RDF node to be checked
     * @return an individual if a node is a valid {@code PointCloud} resource,
     * {@code null} otherwise
     */
    private Individual validateCloudResource(RDFNode node) {
        if (node.isResource()) {
            String uri = node.asResource().getURI();
            Individual individual = model.getIndividual(uri);

            if (individual.getLocalName().contains("PointCloud")) {
                return individual;
            }
        }

        return null;
    }

    /**
     * Retrieves a "physical" point cloud based on its semantic description.
     *
     * @param individual is an ontology individual that describes a point cloud
     * @return the point cloud corresponding to a given ontology individual
     */
    private PointCloud retrieveCloud(Individual individual) {
        String localName = individual.getLocalName();
        String pathToCloud = path + "/clouds/" + localName + ".txt";
        CloudLoader loader = new CloudLoader(pathToCloud);

        try {
            loader.load();
        } catch (IOException ex) {
            return null;
        }

        return loader.getCloud();
    }

}

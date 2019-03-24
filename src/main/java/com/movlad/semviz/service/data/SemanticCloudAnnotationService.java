package com.movlad.semviz.service.data;

import com.movlad.semviz.core.io.CloudLoader;
import com.movlad.semviz.core.math.geometry.PointCloud;
import com.movlad.semviz.data.SemanticAnnotation;
import com.movlad.semviz.mvp.ObservableObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
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

public class SemanticCloudAnnotationService extends ObservableObject
        implements ISemanticObjectAnnotationService<PointCloud> {

    private int failCount;

    private String dataSource;
    private OntModel model;

    public int getFailCount() {
        return failCount;
    }

    private void setModel(String dataSource) throws IOException {
        if (this.dataSource == null || !this.dataSource.equals(dataSource)) {
            OntModel prev = model;

            File directory = new File(dataSource);

            if (!directory.exists()) {
                throw new IOException("Directory does not exist");
            }

            if (!directory.isDirectory()) {
                throw new IOException("File is not a directory.");
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
                throw new IllegalArgumentException("Not a valid Semviz directory.");
            }

            raisePropertyChanged("Model", prev, model);
        }
    }

    private void setDataSource(String dataSource) {
        String prev = this.dataSource;

        this.dataSource = dataSource;

        raisePropertyChanged("DataSource", prev, dataSource);
    }

    public void load(String dataSource) throws IOException {
        failCount = 0;

        setModel(dataSource);
        setDataSource(dataSource);
    }

    @Override
    public List<SemanticAnnotation<PointCloud>> query(String queryString) {
        failCount = 0;

        List<SemanticAnnotation<PointCloud>> soas = new ArrayList<>();
        String prefixes = generatePrefixes();
        Query query = QueryFactory.create(prefixes + queryString);
        QueryExecution execution = QueryExecutionFactory.create(query, model);
        ResultSet results = execution.execSelect();

        while (results.hasNext()) {
            QuerySolution solution = results.next();

            SemanticAnnotation<PointCloud> result = processQuerySolution(solution);

            if (result != null) {
                soas.add(result);
            } else {
                failCount++;
            }
        }

        return soas;
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
     * Maps an instance of {@code SemanticAnnotation} to an instance of
     * {@code QuerySolution}.
     *
     * @param solution is the instance of {@code QuerySolution} to be processed
     * @return an instance of {@code SemanticAnnotation<PointCloud>} that
     * corresponds to the parameter {@code QuerySolution}
     */
    private SemanticAnnotation<PointCloud> processQuerySolution(QuerySolution solution) {
        SemanticAnnotation<PointCloud> result;
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

        result = new SemanticAnnotation<>(individual.getURI(), attributes, cloud);

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
        String pathToCloud = Paths.get(dataSource, localName + ".txt").toString();
        CloudLoader loader = new CloudLoader(pathToCloud);

        try {
            loader.load();
        } catch (IOException e) {
            return null;
        }

        return loader.getCloud();
    }

}

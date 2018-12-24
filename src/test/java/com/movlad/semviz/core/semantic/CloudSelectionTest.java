package com.movlad.semviz.core.semantic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.jupiter.api.Test;

class CloudSelectionTest {

	@Test
	void test() {
		OntModel model = ModelFactory.createOntologyModel();
		
		InputStream is;
		try {
			SemvizManager semvizManager = SemvizManager.get();
			
			semvizManager.load(ClassLoader.getSystemClassLoader()
					.getResource("data/semviz").getFile());
			
			String qs = "PREFIX knowdip: <http://lab.ponciano.info/knowdip#>\n"
					+ "select ?cloud ?size where { ?cloud knowdip:hasSize ?size . }";
		} catch (NotSemvizDirectoryException | SemvizOntologyException | IOException e) {
			e.printStackTrace();
		}
	}

}

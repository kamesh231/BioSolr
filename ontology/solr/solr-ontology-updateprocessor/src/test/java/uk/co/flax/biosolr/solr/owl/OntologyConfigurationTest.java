/**
 * Copyright (c) 2015 Lemur Consulting Ltd.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.flax.biosolr.solr.owl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import org.junit.Test;

import uk.co.flax.biosolr.solr.owl.OntologyConfiguration;

/**
 * Unit tests for the OntologyConfiguration class.
 *
 * @author mlp
 */
public class OntologyConfigurationTest {
	
	private static final String COMPLETE_PROPFILE_PATH = "ontologyUpdate/config/ontology_1.properties";
	private static final String INCOMPLETE_PROPFILE_PATH = "ontologyUpdate/config/ontology_2.properties";
	
	private static String getFilePath(String file) throws URISyntaxException {
		URL fileUrl = OntologyConfigurationTest.class.getClassLoader().getResource(file);
		return new File(fileUrl.toURI()).getAbsolutePath(); 
	}
	
	@Test
	public void defaultConfiguration() {
		OntologyConfiguration test = OntologyConfiguration.defaultConfiguration();
		assertEquals(Arrays.asList(OntologyConfiguration.SYNONYM_PROPERTY_URI), test.getSynonymPropertyUris());
		assertEquals(Arrays.asList(OntologyConfiguration.LABEL_PROPERTY_URI), test.getLabelPropertyUris());
		assertEquals(Arrays.asList(OntologyConfiguration.DEFINITION_PROPERTY_URI), test.getDefinitionPropertyUris());
		assertTrue(test.getIgnorePropertyUris().isEmpty());
	}
	
	@Test(expected=java.io.IOException.class)
	public void fromPropertiesFile_noSuchFile() throws Exception {
		OntologyConfiguration.fromPropertiesFile("blah");
	}
	
	@Test
	public void fromPropertiesFile() throws Exception {
		OntologyConfiguration test = OntologyConfiguration.fromPropertiesFile(getFilePath(COMPLETE_PROPFILE_PATH));
		assertEquals(1, test.getLabelPropertyUris().size());
		assertEquals("http://www.w3.org/2000/01/rdf-schema#label", test.getLabelPropertyUris().get(0));
		assertEquals(2, test.getSynonymPropertyUris().size());
		assertEquals("http://www.ebi.ac.uk/efo/alternative_term", test.getSynonymPropertyUris().get(0));
		assertEquals("http://www.geneontology.org/formats/oboInOwl#hasExactSynonym", test.getSynonymPropertyUris().get(1));
		assertEquals(2, test.getDefinitionPropertyUris().size());
		assertEquals("http://www.ebi.ac.uk/efo/definition", test.getDefinitionPropertyUris().get(0));
		assertEquals("http://purl.obolibrary.org/obo/IAO_0000115", test.getDefinitionPropertyUris().get(1));
		assertEquals(1, test.getIgnorePropertyUris().size());
		assertEquals("http://www.geneontology.org/formats/oboInOwl#ObsoleteClass", test.getIgnorePropertyUris().get(0));
	}

	@Test
	public void fromPropertiesFile_withDefaults() throws Exception {
		OntologyConfiguration test = OntologyConfiguration.fromPropertiesFile(getFilePath(INCOMPLETE_PROPFILE_PATH));
		assertEquals(1, test.getLabelPropertyUris().size());
		assertEquals("http://www.w3.org/2000/01/rdf-schema#label", test.getLabelPropertyUris().get(0));
		assertEquals(1, test.getSynonymPropertyUris().size());
		assertEquals(OntologyConfiguration.SYNONYM_PROPERTY_URI, test.getSynonymPropertyUris().get(0));
		assertEquals(2, test.getDefinitionPropertyUris().size());
		assertEquals("http://www.ebi.ac.uk/efo/definition", test.getDefinitionPropertyUris().get(0));
		assertEquals("http://purl.obolibrary.org/obo/IAO_0000115", test.getDefinitionPropertyUris().get(1));
		assertEquals(0, test.getIgnorePropertyUris().size());
	}

}
package semantics.cityhub.keti.utils;

import java.io.File;
import java.io.InputStream;
import java.util.Stack;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;

public class UnusedCode {

	private int extractDataRangeRestrictionEndIndex(String rule_string, int index_squareBracket) {
		// TODO Auto-generated method stub
		
		int CLOSE_ROUND_BRACKET_INDEX = 0;
		
		char open_toSearch[] = { '[' , '(' }, close_toSearch[] = { ']' , ')' };
		
		int index_openBracket = index_squareBracket, lookup_offset = index_squareBracket+1;
		
		Stack<Integer> stack_openBracket = new Stack<Integer>();
		
		stack_openBracket.push( index_openBracket );
		
		for(int i=0; i<open_toSearch.length; i++) {
			
			int index_closeBracket = 0;
			
			boolean element_found = false;
			
			while(!element_found) {
				
				if( rule_string.charAt(lookup_offset) == open_toSearch[i] ) { //subsequent element opened
					
					index_openBracket = lookup_offset;
					
					stack_openBracket.push( index_openBracket );
					
				}else if( rule_string.charAt(lookup_offset) == close_toSearch[i] ) { //Some element is ended...
					
					index_openBracket = stack_openBracket.pop();
					
					index_closeBracket = lookup_offset;
					
					if(stack_openBracket.isEmpty()) {
						
						element_found = true;
					}	
				}
				
				lookup_offset++;
			}
			
			if( i==0 && rule_string.charAt(index_closeBracket+1) != '(' ) {
				
				System.out.println("Invalid String : " + "Found " + rule_string.charAt(index_closeBracket+1) 
								 + "at char " + (index_closeBracket+1) + ". Was Expecting (" );
				
				return -1;
				
			}else if(i==1) {	CLOSE_ROUND_BRACKET_INDEX = index_closeBracket;		}
			
		}
		
		System.out.println("Extracted DataRangeRestriction: " + rule_string.substring(0, CLOSE_ROUND_BRACKET_INDEX+1));
		
		//rule_elements.add( rule_string.substring(0, CLOSE_ROUND_BRACKET_INDEX+1) );
		
		return CLOSE_ROUND_BRACKET_INDEX;
	}



	OWLOntology readStoredOntology() {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		OWLOntology ontology = null;
		
		try {
			ontology = manager.loadOntologyFromOntologyDocument(new File(
																	System.getProperty("user.dir") + 
																	"\\ontologies\\SampleOntology.owl" 
																	));
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		OWLDataFactory factory = manager.getOWLDataFactory();
		
		PrefixOWLOntologyFormat prefixFormat = (PrefixOWLOntologyFormat) manager.getOntologyFormat(ontology);
		
		//System.out.println("axioms : " + getOntologyModel().getNsPrefixURI("") ); 
		
		OWLClass DataSetClass = factory.getOWLClass(":DataSet", prefixFormat); 
		 
		OWLObjectRenderer renderer = new DLSyntaxObjectRenderer();
		
		OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance(); 
		
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, new SimpleConfiguration());
		
		System.out.println("After Reading Ontology Document : " ); 
		System.out.println( ontology.getAxioms() );
		
        for (OWLNamedIndividual dataset_1 : reasoner.getInstances(DataSetClass, false).getFlattened()) { 
            System.out.println("person : " + renderer.render(dataset_1)); 
        }
        
        return ontology;
	}
	
	OntModel getOntologyModel() {
		
		OntModel model = ModelFactory.createOntologyModel();

		//Read the ontology file
		//model.begin();
		InputStream in = FileManager.get().open(System.getProperty("user.dir") + "\\ontologies\\SampleOntology.owl");
		if (in == null) {
		    throw new IllegalArgumentException("File: not found");
		}        
		model.read(in,"");
		model.commit();

		//Get the base namespace
		//model.getNsPrefixURI("");
		return model;
	}
	
	
}

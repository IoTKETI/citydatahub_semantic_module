package semantics.cityhub.keti.reasoner;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredDataPropertyCharacteristicAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import semantics.cityhub.keti.APIConfiguration;
import semantics.cityhub.keti.utils.CSVHandler;
import semantics.cityhub.keti.utils.NullOntologyModelerException;
import semantics.cityhub.keti.utils.XSDDatatypeHandler;
import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;

public class RuleEngineHandler {

	OntologyModel ONT_MODEL;
	
	OWLOntology assertionOntology;
	OWLDataFactory assertionDataFactory;
	OWLOntologyManager assertionManager;
	static CSVHandler csv_handler;
	
	
	  ///////////////////////////
	 //---->Constructor 
	///////////////////////////
	RuleEngineHandler( OntologyModel ont_model ) throws NullOntologyModelerException{
		
		csv_handler = new CSVHandler();
		
		if(ont_model!=null) {
			
			ONT_MODEL = ont_model;

		}else {
		
			throw new NullOntologyModelerException("Initialize the OntologyModeler.");
		}
		
	}
	
	public RuleEngineHandler() {
		// TODO Auto-generated constructor stub

		csv_handler = new CSVHandler();
	}
	
	
	public CSVHandler executeInferencing(BufferedReader br_data) {
		
		assertionManager = OWLManager.createOWLOntologyManager();
		assertionDataFactory = assertionManager.getOWLDataFactory();
		
		try {
		
			OWLNamedIndividual individual_dataSet = assertionDataFactory.getOWLNamedIndividual( IRI.create( APIConfiguration.PREFIX_URI + "#" + "ds_1"  ) );
		
			OWLClass class_DataSet = assertionDataFactory.getOWLClass( IRI.create( APIConfiguration.PREFIX_URI + "#DataSet" ) );
		
			Set<OWLAxiom> axiom_propAssertions = new HashSet<OWLAxiom>();
		
			axiom_propAssertions.add( assertionDataFactory.getOWLDeclarationAxiom(class_DataSet) );
		
			axiom_propAssertions.add( assertionDataFactory.getOWLDeclarationAxiom(individual_dataSet) );
		
			axiom_propAssertions.add( assertionDataFactory.getOWLClassAssertionAxiom( class_DataSet.asOWLClass() , individual_dataSet) );
			
			String readLine = "";
			
			if( (readLine = br_data.readLine()) != null ) {
				
				String[] independantVars = readLine.split(",");
				//System.out.println( "Independant Variables: " +  independantVars);
				
				csv_handler.addVariables( new ArrayList<>( Arrays.asList(independantVars) ) );
				
				readLine = br_data.readLine();
				String[] varTypes = readLine.split(",");
				//System.out.println( "Value Types: " +  varTypes);
				
				for(int rowCount=0; (readLine = br_data.readLine())!=null; rowCount++) {
					
					    //--->This Set is created especially for Data Property Assertions
					   //---->generated for each row of the Dataset. After the end of 
					  //----->iterating each column, this set is reset (Emptied) to add
					 //------>assertions of new row and then perform reasoning separately.
					//------->on that row.
					Set<OWLDataPropertyAssertionAxiom> axiom_dataPropAssertions = new HashSet<OWLDataPropertyAssertionAxiom>();
					
					assertionOntology = assertionManager.createOntology(ONT_MODEL.ontology.getAxioms());
					
					//System.out.println( readLine);
					String[] rowValues = readLine.split(",");
					
					//Checking that data has even number of columns...
					if( independantVars.length==varTypes.length && independantVars.length==rowValues.length ) {
					
						for(int columnCount=0; columnCount<rowValues.length; columnCount++) {
						
							if(ONT_MODEL.dataProperties.containsKey( independantVars[columnCount] )) {
							
								XSDDatatypeHandler xsd_data_handler = new XSDDatatypeHandler();
							
								OWLLiteral literal_value = assertionDataFactory.getOWLLiteral(
																		rowValues[columnCount], 
																		xsd_data_handler.get_OWLXSDDataTypeOnly(
																								varTypes[columnCount], 
																								assertionDataFactory
																								)							
															);
						
							axiom_dataPropAssertions.add(assertionDataFactory.getOWLDataPropertyAssertionAxiom(
																			ONT_MODEL.getDataPropertyWithTag(independantVars[columnCount]), 
																											individual_dataSet, 
																											literal_value
																											));
							}
						}//--->End of Loop for Columns
						
					}else {
						System.out.println("Exception: Variables and row values are uneven.");
					}
					
					axiom_propAssertions.addAll(axiom_dataPropAssertions);
					
					if( !axiom_propAssertions.isEmpty() ) {
						
						assertionManager.addAxioms(assertionOntology, axiom_propAssertions);
						
						axiom_propAssertions.removeAll(axiom_dataPropAssertions);
						
						executeReasoning(individual_dataSet);
					}
					
					axiom_dataPropAssertions.clear();
					
				}//--->End of Loop for Rows
			}
	      
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		return csv_handler;
		
//		InferredPropertyAssertionGenerator generator = new InferredPropertyAssertionGenerator();
//		Set<OWLPropertyAssertionAxiom<?, ?>> axioms = generator.createAxioms(assertionManager, reasoner);
	}
	
	
	public void executeReasoning(OWLNamedIndividual individual_dataSet) throws OWLOntologyCreationException {
		
		PelletReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
		PelletReasoner reasoner = reasonerFactory.createNonBufferingReasoner(assertionOntology, new SimpleConfiguration());
		
		//reasoner.getKB().realize();
		//reasoner.precomputeInferences();
		
		List<InferredAxiomGenerator<? extends OWLAxiom>> axiomGenerators = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
		axiomGenerators.add(new InferredClassAssertionAxiomGenerator() );
		axiomGenerators.add( new InferredDataPropertyCharacteristicAxiomGenerator() );
		axiomGenerators.add( new InferredPropertyAssertionGenerator() );
      
		OWLOntology infOnt = assertionManager.createOntology();
		
		InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner,axiomGenerators);
		
		iog.fillOntology(assertionManager, infOnt);
		
		listAllDataPropertyValues(individual_dataSet, assertionOntology, reasoner);
		
//		System.out.println( "\nAfter Inferencing: infOnt:" );
//	    System.out.println( infOnt.getAxioms() );
	      
//	    assertionManager.removeOntology(assertionOntology);
//	    assertionOntology = null;
//	    reasoner.flush();
//	    reasoner.dispose();
//	    assertionManager.removeOntology(infOnt);
//	    reasoner = null; 
	}
	
	
	
	public static void listAllDataPropertyValues(OWLNamedIndividual individual,OWLOntology ontology,OWLReasoner reasoner) { 
        
		Map<String,String> map_dataset = new HashMap<String,String>();
		
		OWLObjectRenderer renderer = new DLSyntaxObjectRenderer();
	
		Map<OWLDataPropertyExpression, Set<OWLLiteral>> assertedValues = individual.getDataPropertyValues(ontology); 
        
        for (OWLDataProperty dataProp : ontology.getDataPropertiesInSignature(true)) { 
            
        	for (OWLLiteral literal : reasoner.getDataPropertyValues(individual, dataProp)) { 
                
        		Set<OWLLiteral> literalSet = assertedValues.get(dataProp); 
                boolean asserted = (literalSet!=null&&literalSet.contains(literal));
                
                map_dataset.put( renderer.render(dataProp), renderer.render(literal));
                
//                System.out.println((asserted ? "asserted" : "inferred") + " data property for "+renderer.render(individual)+" : " 
//                        + renderer.render(dataProp) + " -> " + renderer.render(literal)); 
            } 
        }
        csv_handler.addValues( map_dataset );
    } 
}

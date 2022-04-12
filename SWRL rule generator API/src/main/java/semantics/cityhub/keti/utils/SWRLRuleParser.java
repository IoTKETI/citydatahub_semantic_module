package semantics.cityhub.keti.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.coode.owlapi.functionalparser.OWLFunctionalSyntaxParser;
import org.coode.owlapi.functionalparser.ParseException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import semantics.cityhub.keti.APIConfiguration;
import semantics.cityhub.keti.reasoner.OntologyModel;
import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;

public class SWRLRuleParser {
	
	  ///////////////////////////
	 //---->Constructor 
	///////////////////////////
	public SWRLRuleParser( /*OntologyModel ont_modeler*/ ){
		
//		if(ont_modeler!=null) {
//			
//			ONT_MODELER = ont_modeler;
//		}else {
//		
//			throw new NullOntologyModelerException("Initialize the OntologyModeler.");
//		}
		
	}

	
	public OntologyModel parseSWRLRule(String swrl_rule, OntologyModel ont_model) throws NoPropertyFoundException {
		// TODO Auto-generated method stub
		
		String rule_components[] = swrl_rule.split("->");
		
		List<LinkedHashSet<SWRLAtom>> body_head_atom_Sets = new LinkedList<LinkedHashSet<SWRLAtom>>();
		
		if( !ont_model.dataProperties.isEmpty() && (rule_components.length==2) ) {
			
			for(int i=0; i<ont_model.rule_Components.length; i++) {
			
				ont_model.rule_Components[i] = parseElements(rule_components[i]);
				
				 //-->When i=0, atom_set = body
				//--->When i=1, atom_set = head
				LinkedHashSet<SWRLAtom> atom_set = new LinkedHashSet<SWRLAtom>();
				
				for(int j=0; j<ont_model.rule_Components[i].size(); j++) { //-> Loop for elements in one component( Body OR Head )
					
					String elementString = ont_model.rule_Components[i].get(j);
					
					boolean conjuct_with_AND = false;
					
					int index_opAND = elementString.indexOf('^');
					
					if(index_opAND != -1) {
						
						int index_stringStart = elementString.indexOf('"');
						
						if( index_stringStart == -1 || (index_stringStart != -1 && index_stringStart>index_opAND )) {
							
							conjuct_with_AND = true;
							
							elementString = elementString.substring( index_opAND+1 );	
						}
					}
					
					elementString = elementString.trim();
					
					String functionLabel = removeParantheses(elementString);
					
					if( ont_model.isDataProperty(functionLabel) ) { //--> Found Data Property Atom!
						
						System.out.println("Found Data Property Atom with data Property: " + functionLabel );
																   
						SWRLVariable swrl_Var1 = extractVariable(elementString, 1, ont_model.swrlVariables);//-->1 = 1st Argument in the DataPropertyAtom
						
						SWRLVariable swrl_Var2 = extractVariable(elementString, 2, ont_model.swrlVariables);//--->2 = 2nd Argument in the DataPropertyAtom
						
						if(swrl_Var2!=null) { //-->Found 2nd SWRL Variable!
							
							atom_set.add(ont_model.dataFactory.getSWRLDataPropertyAtom( ont_model.dataProperties.get(functionLabel), swrl_Var1, swrl_Var2));
									
						}else { //--> This is not a Variable! Then it must be OWL Literal!
							
							XSDDatatypeHandler xsd_data_handler = new XSDDatatypeHandler();
							
							OWLLiteral literal_arg2 = xsd_data_handler.toOWLLiteral( 
																					elementString.substring( 
																											elementString.indexOf('"', elementString.indexOf(','))+1, 
																											elementString.lastIndexOf('"') ).trim() , 
																					ont_model.dataFactory 
																					);
							
							atom_set.add(
									ont_model.dataFactory.getSWRLDataPropertyAtom( 
																		ont_model.dataProperties.get(functionLabel), 
																		swrl_Var1, ont_model.dataFactory.getSWRLLiteralArgument(literal_arg2) 
																		) 
									);
						}
					}
				}
				body_head_atom_Sets.add( i ,  atom_set );
			}
												//-->  (			body			,			head			 )
			SWRLRule swrlRule = ont_model.dataFactory.getSWRLRule( body_head_atom_Sets.get(0) , body_head_atom_Sets.get(1) );
			
			ont_model.manager.addAxiom(ont_model.ontology, swrlRule);
			
			OWLAnnotationProperty owlAnnotation_isRuleEnabled = 
					ont_model.dataFactory.getOWLAnnotationProperty(
							  									IRI.create( APIConfiguration.SWRL_PREFIX + "#isRuleEnabled")
							  									);
			
			ont_model.manager.addAxiom(	
										ont_model.ontology,
										ont_model.dataFactory.getOWLDeclarationAxiom( owlAnnotation_isRuleEnabled )
										);
			
		}else {
			
			throw new NoPropertyFoundException("First provide the DatatypeProperties required for SWRL Rules.");
		}
		
		return ont_model;
	}




	private List<String> parseElements(String rule_string) {
		
		List<String> rule_elements = new ArrayList<String>();
		
		while( rule_string.length()>1 ) { //--> Until rule_string is non-empty (all elements taken out). 
		
			int index_openBracket = 0, index_closeBracket = 0, lookup_offset=0;
			
			index_openBracket = rule_string.indexOf( '(', lookup_offset );

/////////////////////////////////----------------->Code for involving Square Brackets
//			
//			int index_squareBracket = rule_string.indexOf( '[', lookup_offset );
//			
//			if( (index_squareBracket!=-1) && (index_squareBracket<index_openBracket) ) {
//				
//				System.out.println("Extracting DataRangeRestriction");
//				
//				//--> Returns the ending index of DataRangeRestriction Element
//				int index_DRR = extractDataRangeRestrictionEndIndex(rule_string, index_squareBracket);
//				
//				rule_elements.add( rule_string.substring(0, index_DRR+1) );
//				
//				rule_string = rule_string.substring( index_DRR + 1 );
//				
//			}else { //Rest of the code...
//
/////////////////////////////////----------------->Code for involving Square Brackets
			
				Stack<Integer> stack_openBracket = new Stack<Integer>();
				
				stack_openBracket.push(index_openBracket);
				
				lookup_offset = index_openBracket +1;
				
				boolean element_found = false;
				
				while(!element_found) {
					
					if( rule_string.charAt(lookup_offset) == '(' ) { //subsequent element opened
						
						index_openBracket = lookup_offset;
						
						stack_openBracket.push( index_openBracket );
						
					}else if( rule_string.charAt(lookup_offset) == ')' ) { //Some element is ended...
						
						index_openBracket = stack_openBracket.pop();
						
						index_closeBracket = lookup_offset;
						
						if(stack_openBracket.isEmpty()) { //->Now we search if this element is like (....)(..)
							
							index_openBracket = rule_string.indexOf( '(', index_closeBracket );
							
							if(index_openBracket !=-1) {
								
								for(int i=1; i<(index_openBracket-index_closeBracket) && !element_found; i++) {
									
									if( rule_string.charAt( index_openBracket+i ) != ' ' ) {
										
										element_found = true;
									}
								}
								
							}else {
							
								element_found = true;
							}
						}
						
					}else if( rule_string.charAt(lookup_offset) == '"'  ) { //We have entered a string value
						
						lookup_offset++;
						while( rule_string.charAt(lookup_offset) != '"' ) {			lookup_offset++;		}
					}
					
					lookup_offset++;
				}
				
				rule_elements.add( rule_string.substring(0, index_closeBracket+1) );
				
				rule_string = rule_string.substring(index_closeBracket+1);
				
				System.out.println("Remaining String: " + rule_string);
//			}
		}
		
		System.out.println("Extracted Elements: ");
		
		for(int i=0; i<rule_elements.size(); i++) {
			
			System.out.println( i + ". " + rule_elements.get(i));
		}
		
		
		return rule_elements;
	}
	
	

	private SWRLVariable extractVariable(String elementString, int argNumber, HashMap<String, SWRLVariable> map_swrlVars) {
		// TODO Auto-generated method stub
		
		SWRLVariable swrl_var = null;
		String varString = null;
		
		if(argNumber==1) {
			
			varString = elementString.substring( elementString.indexOf('?')+1 , elementString.indexOf(',') ).trim();
			
		}else if(argNumber==2) {
			
			try {
			
				varString = elementString.substring( 
													elementString.indexOf('?', elementString.indexOf(',')+1)+1 , 
													elementString.indexOf(')') 
													).trim();
				
				if( varString.contains("\"") ){		throw new IndexOutOfBoundsException();		}
				
			} catch (IndexOutOfBoundsException e) { //--> 2nd Argument is not a Variable. Then is must be a value!

				 //--> 2nd Argument is not a Variable. Then is must be a value!
			}
			
		}
		
		if( map_swrlVars.containsKey( varString ) ) {
			
			swrl_var = map_swrlVars.get(varString);
		}
		
		return swrl_var;
	}
	

	private String extractOWLLiteral(String elementString, OWLDataFactory dataFactory) {
		// TODO Auto-generated method stub
		
		String argString = elementString.substring( elementString.indexOf(',')+1 , elementString.lastIndexOf(')') ).trim();
		
		XSDDatatypeHandler xsdDataHandler = new XSDDatatypeHandler();
			
		OWLLiteral literal_arg2 = xsdDataHandler.toOWLLiteral(argString, dataFactory);
		
		
		return null;
	}
	
	
	private String removeParantheses(String elementString) {
		// TODO Auto-generated method stub\
		
		return elementString.substring(0, elementString.indexOf('('));
	}
	
	
	public class NoPropertyFoundException extends Exception{
		
		String exception_Message ;
		
		public NoPropertyFoundException(String message) {
			
			exception_Message = message;
		}
		
		public String toString() {
			
			return ( "No Property Found!. " + exception_Message );
		}
	}
}

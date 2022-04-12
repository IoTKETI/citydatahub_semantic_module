package com.cityhub.semantic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.jena.ext.xerces.util.URI;
import org.apache.jena.ext.xerces.util.URI.MalformedURIException;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.springframework.boot.SpringApplication;

import com.cityhub.semantic.MapListStructure.RedundantKeyException;

public class OWLOntologyModeler {

	OWLOntology parsed_Ontology;
	OWLOntologyManager manager;
	OWLDataFactory dfactory;
	
	OWLOntologyModeler() throws OWLOntologyCreationException{
		
		manager = OWLManager.createOWLOntologyManager();
		parsed_Ontology = manager.createOntology();
		dfactory = manager.getOWLDataFactory();
	}
	
	String convertResponseToSerializationFormat(ResultSet resultSet) throws RedundantKeyException {
		
		ArrayList<String> individual_declare_only = new ArrayList<String>();
		ArrayList<String> obj_property_declare_only = new ArrayList<String>();
		ArrayList<String> data_property_declare_only = new ArrayList<String>();
																									
		HashMap<String, String> labels = new HashMap<String, String>();//------->Key is an entity,
																	  //-------->Value is the label
		HashMap<String, String> comments = new HashMap<String, String>();//----->Key is an entity,
		  																//------>Value is the comment
		
		ArrayList<OWLObjectPropertyAssertionAxiom> axioms_objProperty_assertion = new ArrayList<OWLObjectPropertyAssertionAxiom>();
		ArrayList<OWLAxiom> axioms_dataProperty_assertion = new ArrayList<OWLAxiom>();
		
		
		MapListStructure classes = new  MapListStructure(); //------------------>Key is a class, 
														   //------------------->Value is a list is of subclasses
		MapListStructure equalClasses = new  MapListStructure();//-------------->Key is a class, 
		   													   //--------------->Value is a list is of Equivalent Classes
		MapListStructure classedIndividuals = new  MapListStructure();//-------->Key is a class, 
		   															 //--------->Value is a list is of namedIndividuals
		MapListStructure sub_property_assertion = new MapListStructure();//----->Key is a Data/Object property, 
		   																//------>Value is the list of sub data/object properties
		MapListStructure property_domains  = new MapListStructure();//---------->Key is a data OR Object/Data property,
																   //----------->Value is the list of data/object property domains
		MapListStructure property_ranges  = new MapListStructure();//----------->Key is a data OR Object/Data property,
		   														   //----------->Value is the list of data/object property ranges
		
		
		while(resultSet.hasNext()) {
			
			QuerySolution solution = resultSet.nextSolution();
			
			System.out.println("\nConsole LOG--> ");
			System.out.println( "Predicate: " + solution.get("p").toString() );
			
			if( solution.get("p").toString().endsWith("#type") ) { //--> Checking #type Condition
				
				
				if( solution.get("o").toString().endsWith("#Class") ) { //--> Means this is an OWL Class
				
					
									classes.put( solution.get("s").toString() );
				
									
				}else if( solution.get("o").toString().endsWith("#NamedIndividual") ) { //Means this is a NamedIndividual
				
					
									if( !individual_declare_only.contains(solution.get("s").toString()) ) {
										
										individual_declare_only.add( solution.get("s").toString() );
									}
				
					
				}else if( solution.get("o").toString().endsWith("#ObjectProperty") ) { //Means this is an OWL ObjectProperty
					
					
									if( !obj_property_declare_only.contains(solution.get("s").toString()) ) {
										
										obj_property_declare_only.add( solution.get("s").toString() );
									}
					
					
				}else if( solution.get("o").toString().endsWith("#DatatypeProperty") ) {//Means this is an OWL DatatypeProperty
					
											
									if( !data_property_declare_only.contains(solution.get("s").toString()) ) {
										
										data_property_declare_only.add( solution.get("s").toString() );
									}
									
				
				}else if( solution.get("o").toString().endsWith("#Ontology") ) {
					
									manager.setOntologyDocumentIRI(
																	parsed_Ontology, 
																	IRI.create( solution.get("s").toString() ) 
																	);
					
				}else{  //Means this is individual of some Class.
					
					
									classedIndividuals.put( solution.get("o").toString() , solution.get("s").toString() );
					
				}
			
			}else if ( solution.get("p").toString().endsWith("#subClassOf") ) { //--> Means this is an SubClass Relationship
				
				
						classes.put( solution.get("o").toString() , solution.get("s").toString() );
				
				
			}else if( solution.get("p").toString().endsWith("#equivalentClass") ) { //--> Means this is an Equivalent Class Relationship
				
				
						equalClasses.put( solution.get("s").toString() , solution.get("o").toString() );
				
				
			}else if( solution.get("p").toString().endsWith("#subPropertyOf") ) {
				
				
						sub_property_assertion.put( solution.get("o").toString() , solution.get("s").toString() );
				
				
			}else if( solution.get("p").toString().endsWith("#domain") ) {
				
				
						property_domains.put( solution.get("s").toString(), solution.get("o").toString() );
				
				
			}else if( solution.get("p").toString().endsWith("#range") ) {
				
				
						property_ranges.put( solution.get("s").toString(), solution.get("o").toString() );
				
				
			}else if( solution.get("p").toString().endsWith("#label") ){  //--> Means this Entity has a label
				
				
						labels.put( solution.get("s").toString() , solution.get("o").toString() );
				
				
			}else if( solution.get("p").toString().endsWith("#comment") ) { //--> Means this Entity has a comment
				
				
						comments.put( solution.get("s").toString() , solution.get("o").toString() );
				
				
			}else { //-->Checking for Property Assertion Type (That maybe defined in Instance Graph....)
				
				
				boolean is_URI = false;
				
				String temp_prop_assertion_range = solution.get("o").toString();
				
				try {
					URI uri = null;
					
					uri = new URI( temp_prop_assertion_range );
					
					if( temp_prop_assertion_range.startsWith("http") && temp_prop_assertion_range.contains("#") ) {
					
						is_URI = true;
					}
					
				} catch (MalformedURIException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					is_URI = false;
				}
				
				//System.out.println("\nConsole LOG--> is_URI: " + temp_prop_assertion_range );
				//System.out.println( is_URI );
				
				if( !is_URI  ) { //-->Means it can be a Data value so try to convert into Literal
					
					XSDTypeDataHandler datatype_Handler = new XSDTypeDataHandler();
					
					OWLLiteral literal = datatype_Handler.toOWLLiteral( temp_prop_assertion_range , dfactory);
					
					if( literal!=null ) { //Means value is successfully converted into Literal (Data Assertion Confirmed!)
						
						//axioms_dataProperty_assertion.add( 
						OWLAxiom axiom_dataProperty_assertion = dfactory.getOWLDataPropertyAssertionAxiom(
															dfactory.getOWLDataProperty(IRI.create( solution.get("p").toString() )),
															dfactory.getOWLNamedIndividual(IRI.create( solution.get("s").toString() )),
															literal);
								//);
						manager.applyChange( new AddAxiom( parsed_Ontology, axiom_dataProperty_assertion) );
					}
				
				}else { //----------------->Means it is an Object Property Assertion

					//System.out.println("\nConsole LOG--> Identified Object Property: \n" );
					//System.out.println( solution.get("p").toString() );
					
					//axioms_objProperty_assertion.add( 
					OWLObjectPropertyAssertionAxiom axiom_objProperty_assertion =	dfactory.getOWLObjectPropertyAssertionAxiom( 
	                										dfactory.getOWLObjectProperty(IRI.create( solution.get("p").toString() )),
	                										dfactory.getOWLNamedIndividual(IRI.create( solution.get("s").toString() )),
	                										dfactory.getOWLNamedIndividual(IRI.create( temp_prop_assertion_range ))
	                										);
	                        //));
					manager.applyChange( new AddAxiom( parsed_Ontology, axiom_objProperty_assertion) );
				}
				is_URI = false; // Reseting for next triple identification.
			}
		}
		
		
		 ///////////////////////////////////////-----------NOTE-----------/////////////////////////////////////////
		//-->Until this point, ResultSet has been parsed. Now lists are traversed to create Axioms that will    //
	   //--->be added in OWLOntology object.                                                                   //
	  ////////////////////////////////////////-----------NOTE-----------////////////////////////////////////////
	
		
		//---->Creating Class and Sub Class Axioms...
		
		Iterator class_itor = classes.getIterator();
		
		while(class_itor.hasNext()) {
			
			Map.Entry class_subclass_pair = (Map.Entry) class_itor.next();
			
			OWLClass owl_class = dfactory.getOWLClass(IRI.create( class_subclass_pair.getKey().toString() ));
			
			ArrayList<String> subClasses = (ArrayList<String>) class_subclass_pair.getValue();
			
			if(!subClasses.isEmpty()) {
			
				for(int subClassCount=0; subClassCount<subClasses.size(); subClassCount++) {
					
					OWLAxiom axiom_subclass = dfactory.getOWLSubClassOfAxiom(
																	dfactory.getOWLClass(IRI.create( subClasses.get(subClassCount) )), 
																	owl_class
																		);
					
					manager.applyChange(new AddAxiom( parsed_Ontology, axiom_subclass));
				}
			}else { // This else clause is added to make sure that all class assertions are added.
				
				OWLAxiom axiom_subclass = dfactory.getOWLSubClassOfAxiom(
																owl_class,
																dfactory.getOWLClass(IRI.create( "http://www.w3.org/2002/07/owl#Thing" ))
																);

				manager.applyChange(new AddAxiom( parsed_Ontology, axiom_subclass));
			}
		}
		
		
		
		//---->Creating Equivalent Class Assertion Axioms...
		
		Iterator equalClass_itor = equalClasses.getIterator();
		
		while(equalClass_itor.hasNext()) {
			
			Map.Entry equal_class_pair = (Map.Entry) equalClass_itor.next();
			
			OWLClass owl_class = dfactory.getOWLClass(IRI.create( equal_class_pair.getKey().toString() ));
			
			ArrayList<String> equalClass_list = (ArrayList<String>) equal_class_pair.getValue();
			
			for(int eqClassCount=0; eqClassCount<equalClass_list.size(); eqClassCount++) {
					
				OWLEquivalentClassesAxiom axiom_equalClass = dfactory.getOWLEquivalentClassesAxiom(
																	owl_class,
																	dfactory.getOWLClass(IRI.create( equalClass_list.get(eqClassCount) ))
																		);
					
					manager.applyChange(new AddAxiom( parsed_Ontology, axiom_equalClass));
			}
		}
		
		
		
		//---->Creating Named Individual Assertion Axioms...
		
		Iterator individual_itor = classedIndividuals.getIterator();
		
		while(individual_itor.hasNext()) {
			
			Map.Entry class_individual_pair = (Map.Entry) individual_itor.next();
			
			OWLClass owl_class = dfactory.getOWLClass(IRI.create( class_individual_pair.getKey().toString() ));
			
			ArrayList<String> individuals_list = (ArrayList<String>) class_individual_pair.getValue();
			
			for(int individualCount=0; individualCount<individuals_list.size(); individualCount++) {
				
				OWLClassAssertionAxiom axiom_classAssertion = dfactory.getOWLClassAssertionAxiom(
																	owl_class, 
																	dfactory.getOWLNamedIndividual(IRI.create( individuals_list.get(individualCount )))
																	);
				
				manager.applyChange( new AddAxiom( parsed_Ontology, axiom_classAssertion) );
			}
		}
		
		
		
		//---->Creating Sub Property Axioms....
		
		Iterator subProperty_itor = sub_property_assertion.getIterator();
		
		while(subProperty_itor.hasNext()) {
			
			Map.Entry subProperty_pair = (Map.Entry) subProperty_itor.next();
			
			if( obj_property_declare_only.contains(subProperty_pair.getKey().toString() ) ) { //----->For Object Properties
				
				OWLObjectProperty objectProperty = dfactory.getOWLObjectProperty(IRI.create( subProperty_pair.getKey().toString() ));
				
				ArrayList<String> sub_ObjProp_list = (ArrayList<String>) subProperty_pair.getValue();
				
				for(int propCount=0; propCount<sub_ObjProp_list.size(); propCount++) {
					
					OWLSubObjectPropertyOfAxiom subObjPropertyAxiom = 
							dfactory.getOWLSubObjectPropertyOfAxiom(
															dfactory.getOWLObjectProperty(IRI.create( sub_ObjProp_list.get(propCount) )), 
															objectProperty
																	);
					
					manager.applyChange( new AddAxiom( parsed_Ontology, subObjPropertyAxiom) );
				}
				
			}else if( data_property_declare_only.contains(subProperty_pair.getKey().toString()) ) { //----->For Datatype Properties
				
				OWLDataProperty dataProperty = dfactory.getOWLDataProperty(IRI.create( subProperty_pair.getKey().toString() ));
				
				ArrayList<String> sub_DataProp_list = (ArrayList<String>) subProperty_pair.getValue();
				
				for(int propCount=0; propCount<sub_DataProp_list.size(); propCount++) {
					
					OWLSubDataPropertyOfAxiom subDataPropertyAxiom = 
							dfactory.getOWLSubDataPropertyOfAxiom(
															dfactory.getOWLDataProperty(IRI.create( sub_DataProp_list.get(propCount) )), 
															dataProperty
																	);
					
					manager.applyChange( new AddAxiom( parsed_Ontology, subDataPropertyAxiom) );
				}
			}
			
		}
		
		
		//---->Set of All Property Domain and Range Axioms....
		
		Set<OWLAxiom> domainsAndRanges = new HashSet<OWLAxiom>();
		
		//---->Creating Property Domain Axioms....
		
		Iterator propDomain_itor = property_domains.getIterator();
		
		while(propDomain_itor.hasNext()) {
			
			Map.Entry propDomain_pair = (Map.Entry) propDomain_itor.next();
			
			if( obj_property_declare_only.contains( propDomain_pair.getKey().toString() ) ) { //-->For Object Properties
				
				OWLObjectProperty objectProperty = dfactory.getOWLObjectProperty(IRI.create( propDomain_pair.getKey().toString() ));
				
				ArrayList<String> ObjPropDomain_list = (ArrayList<String>) propDomain_pair.getValue();
				
				for(int domainCount=0; domainCount<ObjPropDomain_list.size(); domainCount++) {
					
					OWLObjectPropertyDomainAxiom ObjPropDomain_Axiom = 
							dfactory.getOWLObjectPropertyDomainAxiom(
															objectProperty,
															dfactory.getOWLClass(IRI.create( ObjPropDomain_list.get(domainCount).toString() ))
																	);
					
					
					if(!domainsAndRanges.contains( ObjPropDomain_Axiom )) {
																			domainsAndRanges.add( ObjPropDomain_Axiom );
																			}
				}
				
			}else if( data_property_declare_only.contains( propDomain_pair.getKey().toString() ) ) { //-->For Datatype Properties
				
				OWLDataProperty dataProperty = dfactory.getOWLDataProperty(IRI.create( propDomain_pair.getKey().toString() ));
				
				ArrayList<String> DataPropDomain_list = (ArrayList<String>) propDomain_pair.getValue();
				
				for(int domainCount=0; domainCount<DataPropDomain_list.size(); domainCount++) {
					
					OWLDataPropertyDomainAxiom DataPropDomain_Axiom = 
							dfactory.getOWLDataPropertyDomainAxiom(
															dataProperty,
															dfactory.getOWLClass(IRI.create( DataPropDomain_list.get(domainCount).toString() ))
																	);
					
					if(!domainsAndRanges.contains( DataPropDomain_Axiom )) {
																			domainsAndRanges.add( DataPropDomain_Axiom );
																			}
				}
			}
			
		}
		
		
		
		//---->Creating Property Range Axioms....
		
		Iterator propRange_itor = property_ranges.getIterator();
		
		while(propRange_itor.hasNext()) {
			
			Map.Entry propRange_pair = (Map.Entry) propRange_itor.next();
			
			if( obj_property_declare_only.contains( propRange_pair.getKey().toString() ) ) { //-->For Object Properties
				
				OWLObjectProperty objectProperty = dfactory.getOWLObjectProperty(IRI.create( propRange_pair.getKey().toString() ));
				
				ArrayList<String> ObjPropRange_list = (ArrayList<String>) propRange_pair.getValue();
				
				for(int rangeCount=0; rangeCount<ObjPropRange_list.size(); rangeCount++) {
					
					OWLObjectPropertyRangeAxiom ObjPropRange_Axiom = 
							dfactory.getOWLObjectPropertyRangeAxiom(
															objectProperty,
															dfactory.getOWLClass(IRI.create( ObjPropRange_list.get(rangeCount).toString() ))
																	);
					
					if(!domainsAndRanges.contains( ObjPropRange_Axiom )) {
																			domainsAndRanges.add( ObjPropRange_Axiom );
																			}
				}
				
			}else if( data_property_declare_only.contains( propRange_pair.getKey().toString() ) ) { //-->For Datatype Properties
				
				OWLDataProperty dataProperty = dfactory.getOWLDataProperty(IRI.create( propRange_pair.getKey().toString() ));
				
				ArrayList<String> DataPropRange_list = (ArrayList<String>) propRange_pair.getValue();
				
				 XSDTypeDataHandler xsd_data_handler = new XSDTypeDataHandler();
				
				for(int rangeCount=0; rangeCount<DataPropRange_list.size(); rangeCount++) {
					
					OWLDatatype owl_datatype = xsd_data_handler.get_OWLXSDDataTypeOnly( DataPropRange_list.get(rangeCount).toString(), dfactory );
					
					if(owl_datatype!=null) {
					
						OWLDataPropertyRangeAxiom DataPropRange_Axiom = 
														dfactory.getOWLDataPropertyRangeAxiom( dataProperty, owl_datatype );

						if(!domainsAndRanges.contains( DataPropRange_Axiom )) {
																				domainsAndRanges.add( DataPropRange_Axiom );
																				}
					}
				}
			}
		}
		
		if(!domainsAndRanges.isEmpty()) {

            manager.addAxioms(parsed_Ontology, domainsAndRanges);
        }
		
		
		
		//---->Creating Comment Axioms....
		
		Iterator comment_itor = comments.entrySet().iterator();
		
		while(comment_itor.hasNext()) {
			
			Map.Entry comment_pair = (Map.Entry) comment_itor.next();
			
			String the_key = comment_pair.getKey().toString();
			
			String[] commentValue = parse_CommentORLabel_Value( comment_pair.getValue().toString() );
			
			OWLLiteral commentLiteral;
			
			if( commentValue[1]==null ) {
				
				commentLiteral = dfactory.getOWLLiteral( commentValue[0] );
			
			}else {
				
				commentLiteral = dfactory.getOWLLiteral( commentValue[0], commentValue[1] );
			}
				
			OWLAnnotation commentAnnotation = dfactory.getOWLAnnotation( dfactory.getRDFSComment(), commentLiteral );
			
			OWLAxiom comment_axiom = dfactory.getOWLAnnotationAssertionAxiom( IRI.create( the_key ), commentAnnotation );
			
			manager.applyChange( new AddAxiom( parsed_Ontology, comment_axiom) );
		}
		
		
		
		
		//---->Creating Label Axioms....
		
		Iterator label_itor = labels.entrySet().iterator();
		
		while(label_itor.hasNext()) {
			
			Map.Entry label_pair = (Map.Entry) label_itor.next();
			
			String the_key = label_pair.getKey().toString();
			
			String[] labelValue = parse_CommentORLabel_Value( label_pair.getValue().toString() );
			
			OWLLiteral labelLiteral;
			
			if( labelValue[1]==null ) {
				
				labelLiteral = dfactory.getOWLLiteral( labelValue[0] );
			
			}else {
				
				labelLiteral = dfactory.getOWLLiteral( labelValue[0], labelValue[1] );
			}
				
			OWLAnnotation labelAnnotation = dfactory.getOWLAnnotation( dfactory.getRDFSLabel(), labelLiteral );
			
			OWLAxiom label_axiom = dfactory.getOWLAnnotationAssertionAxiom( IRI.create( the_key ), labelAnnotation );
			
			manager.applyChange( new AddAxiom( parsed_Ontology, label_axiom) );
		}
		
		
		return getStringifiedOntology( parsed_Ontology );
	}
	
	
	
	  ////////////////////////////////////////////
	 //-->Parse the value and seperate the value
	//--->and the language tag.
   ////////////////////////////////////////////
	String[] parse_CommentORLabel_Value(String in_value) {
		
		String[] parsed = { null, null };
		
		if(in_value.contains("@")) {
			
			String[] seperated = in_value.split("@");
			
			parsed[0] = seperated[0]; 
			parsed[1] = seperated[1]; 
			
		}else {
			parsed[0] = in_value;
		}
		
		return parsed;
	}
	
	  ////////////////////////////////////////////
	 //-->Parse the value and seperate the value
	//--->and the language tag.
   ////////////////////////////////////////////
	String getStringifiedOntology( OWLOntology in_Ontology ) {
		
		ByteArrayOutputStream out_stream = new ByteArrayOutputStream();
		
		String stringified_Ontology = null;
		
		try {
			manager.saveOntology( in_Ontology, out_stream);
			
			stringified_Ontology = new String( out_stream.toByteArray(), "UTF-8" );
			
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stringified_Ontology;
	}
}

package com.semantic.annotator.validation;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.util.*;

public class OntologyModeler {

    OWLOntology the_Vocabularies = null;

    /////////////////////////////////////////////////////////////////////
    //--->Convert the Instance Model to OWLOntology Object
    /////////////////////////////////////////////////////////////////////
    OWLOntology modelToOWLOntology(Model model) throws OWLOntologyCreationException {

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        OWLOntology ontology = null;

        ArrayList<ArrayList<String>> individuals = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> properties = new ArrayList<ArrayList<String>>();
//		ArrayList<ArrayList<String>> objectProperties = new ArrayList<ArrayList<String>>();
//		ArrayList<ArrayList<String>> dataProperties = new ArrayList<ArrayList<String>>();

        Graph g = model.getGraph();

        ontology = manager.createOntology();

        OWLDataFactory dfactory = manager.getOWLDataFactory();

        for (Iterator itor = g.find(Node.ANY, Node.ANY, Node.ANY); itor.hasNext();) {

            Triple triple = (Triple)itor.next();
            //System.out.println(" { " + triple.getSubject() + " " + triple.getPredicate() + " " + triple.getObject() + " . }");

            if( triple.getPredicate().toString().endsWith("#type") ) {

                if( !triple.getObject().toString().endsWith("#NamedIndividual") ) {//--> Means this triple defines the Class
                    //--> Class Assertion
                    //-->Adding to custom made individual list
                    individuals.add( new ArrayList<String>( Arrays.asList(
                            triple.getSubject().toString(), //-->Individual URI
                            triple.getObject().toString()   //-->Class URI
                    )) );

                    //--->Adding to Ontology Object
                    OWLNamedIndividual owl_individual = dfactory.getOWLNamedIndividual(IRI.create(
                            triple.getSubject().toString()
                    ));
                    OWLClass owl_class = dfactory.getOWLClass(IRI.create( triple.getObject().toString() ));

                    OWLClassAssertionAxiom classAssertion = dfactory.getOWLClassAssertionAxiom( owl_class , owl_individual );

                    AddAxiom addAxiom = new AddAxiom(ontology, classAssertion);

                    manager.applyChange(addAxiom);
                }

            }else {

                properties.add( new ArrayList<String>( Arrays.asList(
                        triple.getSubject().toString(),
                        triple.getPredicate().toString(),
                        triple.getObject().toString()
                )) );
            }
        }
        //getObjectPropertiesFromVocabularies();

        //System.out.println("Vocabulary is empty: " + the_Vocabularies.isEmpty());

        for(int offset=0; offset<properties.size(); offset++) {

            AddAxiom axiom_to_add = null;

            ArrayList<String> one_property = properties.get(offset);

            boolean isObjectProperty = the_Vocabularies.containsObjectPropertyInSignature(IRI.create( one_property.get(1) ))
                    ||
                    one_property.get(1).contains("http://www.w3.org/2000/01/rdf-schema#seeAlso");

            //System.out.println("Object Property: ");
            //System.out.println( one_property.get(1).toString() + " : " + isObjectProperty );


            if( /*individuals.contains(one_property.get(2))*/
                    isObjectProperty
            ) { //--> This means it is owl:ObjectProperty

//        		objectProperties.add( new ArrayList<String>( Arrays.asList(
//																		one_property.get(0).toString(),
//																		one_property.get(1).toString(),
//																		one_property.get(2).toString()
//																)) );

                OWLNamedIndividual subject = dfactory.getOWLNamedIndividual(IRI.create( one_property.get(0).toString() ));

                OWLObjectProperty objectProperty = dfactory.getOWLObjectProperty(IRI.create( one_property.get(1).toString() ));

                OWLNamedIndividual object = dfactory.getOWLNamedIndividual(IRI.create( one_property.get(2).toString() ));

                OWLObjectPropertyAssertionAxiom obj_property_Assertion =
                        dfactory.getOWLObjectPropertyAssertionAxiom(
                                objectProperty,
                                subject,
                                object
                        );

                axiom_to_add = new AddAxiom( ontology, obj_property_Assertion);

            }else { //--------------------------------------------> This means it is owl:DatatypeProperty

//        		dataProperties.add( new ArrayList<String>( Arrays.asList(
//																		one_property.get(0).toString(),
//																		one_property.get(1).toString(),
//																		one_property.get(2).toString()
//        														)) );

                OWLNamedIndividual subject = dfactory.getOWLNamedIndividual(IRI.create( one_property.get(0).toString() ));

                OWLDataProperty dataProperty = dfactory.getOWLDataProperty(IRI.create( one_property.get(1).toString() ));

                Set<OWLDataPropertyRangeAxiom> possibleRanges = the_Vocabularies.getDataPropertyRangeAxioms(dataProperty);

                Iterator itor = possibleRanges.iterator();

                boolean foundRange = false;

                while(itor.hasNext() && !foundRange){

                    OWLDataPropertyRangeAxiom rangeAxiom = (OWLDataPropertyRangeAxiom) itor.next();

                    //System.out.println("Data Property Range: " + rangeAxiom.getRange().toString());

                    /*if(
                            rangeAxiom.getRange().toString().equals("xsd:unsignedInt")
                            ||
                            rangeAxiom.getRange().toString().equals("xsd:dateTime")
                            ||
                            rangeAxiom.getRange().toString().equals("xsd:positiveInteger")
                            ||
                             rangeAxiom.getRange().toString().equals("xsd:double")
                    ){*/
                    //one_property.set(2,"\"+" + one_property.get(2).substring(1));

//                    if( one_property.get(2).contains("\"")){
//
//                            one_property.set(2, one_property.get(2).replaceAll("\\\\\"","") );
//                        }
                    //}
                }

                XSDTypeDataHandler xsd_data_handler = new XSDTypeDataHandler();

                OWLLiteral literal = xsd_data_handler.toOWLLiteral( one_property.get(2).toString().replaceAll("\"",""), dfactory );

                //System.out.println("Data Properties: ");
                //System.out.println(" { " + one_property.get(0).toString()  + " " + one_property.get(1).toString() + " " + one_property.get(2).toString() + " . }");

                OWLAxiom data_property_assertion =
                        dfactory.getOWLDataPropertyAssertionAxiom(
                                dataProperty,
                                subject,
                                literal
                        );
                axiom_to_add = new AddAxiom( ontology, data_property_assertion);
            }
            manager.applyChange( axiom_to_add );
        }


        return ontology;
    }




    /////////////////////////////////////////////////////////////////////
    //--->Create OWLOntology Object from SPARQL Query responses
    /////////////////////////////////////////////////////////////////////
    OWLOntology create_OntologyFromTDB() throws OWLOntologyCreationException {

//        System.out.println("Function create_OntologyFromTDB() called!");

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        OWLOntology ontology = null;

        ontology = manager.createOntology();

        OWLDataFactory dfactory = manager.getOWLDataFactory();

        //------------*********------------ Adding Classes in the OWLOntology Object ------------*********------------

        ArrayList<ArrayList<String>> class_hierarchy = new  ArrayList<ArrayList<String>>();

        TDBHandler tdb_handler = new TDBHandler();

        tdb_handler.populate_OntologyList();

        ResultSet rs_ontology_classes = null;

        try {
            rs_ontology_classes = tdb_handler.execute_GetOntologyClassDeclaration();

        } catch (TDBHandler.OntologiesNotAvailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while ( rs_ontology_classes.hasNext() ) {

            QuerySolution solution = rs_ontology_classes.nextSolution();

            String pClass = "http://www.w3.org/2002/07/owl#Thing";

            if( !(solution.get("parentClass")==null) ) {

                pClass = solution.get("parentClass").toString();
            }

            //System.out.println("\nConsole LOG--> Parent Class: " + pClass + "\n" );

            OWLClass owl_class = dfactory.getOWLClass(IRI.create( solution.get("class").toString() ));
            OWLClass owl_parent_class = dfactory.getOWLClass(IRI.create( pClass ));

            OWLAxiom axiom_subclass = dfactory.getOWLSubClassOfAxiom(owl_class, owl_parent_class);

            AddAxiom addAxiom_subclass = new AddAxiom(ontology, axiom_subclass);

            manager.applyChange(addAxiom_subclass);

            //class_hierarchy.add( new ArrayList<String>( Arrays.asList( solution.get("class").toString(), pClass )) );
        }

        //----------*********----------Adding Object Properties in the OWLOntology Object----------*********----------

        ResultSet rs_obj_properties = null;

        try {
            rs_obj_properties = tdb_handler.execute_GetOntologyObjectPropertyDeclaration();

        } catch (TDBHandler.OntologiesNotAvailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int objPropCount = 0;

        while ( rs_obj_properties.hasNext() ) {

            QuerySolution solution = rs_obj_properties.nextSolution();

            objPropCount++;

            //System.out.println("Object Property From Ontology: " + solution.get("objProperty").toString() + "," + objPropCount );

            OWLObjectProperty objectProperty = dfactory.getOWLObjectProperty(IRI.create( solution.get("objProperty").toString() ));

            Set<OWLAxiom> domainsAndRanges = new HashSet<OWLAxiom>();

            OWLSubObjectPropertyOfAxiom subObjPropertyAxiom = null;

            if( !(solution.get("parentProperty")==null) ) {

                OWLObjectProperty parentProperty = dfactory.getOWLObjectProperty(IRI.create( solution.get("parentProperty").toString() ));

                subObjPropertyAxiom = dfactory.getOWLSubObjectPropertyOfAxiom(objectProperty, parentProperty);

                if( !ontology.containsAxiom(subObjPropertyAxiom) ) {

                    AddAxiom addAxiom_subProperty = new AddAxiom(ontology, subObjPropertyAxiom);

                    manager.applyChange(addAxiom_subProperty);
                }
            }

            if( !(solution.get("domain")==null) ) {

                OWLClass domain_class = dfactory.getOWLClass(IRI.create( solution.get("domain").toString() ));

                OWLObjectPropertyDomainAxiom axiom_domain = dfactory.getOWLObjectPropertyDomainAxiom( objectProperty, domain_class );

                if(!domainsAndRanges.contains(axiom_domain)) {

                    domainsAndRanges.add(axiom_domain);
                }
            }

            if( !(solution.get("range")==null) ) {

                OWLClass range_class = dfactory.getOWLClass(IRI.create( solution.get("range").toString() ));

                OWLObjectPropertyRangeAxiom axiom_range = dfactory.getOWLObjectPropertyRangeAxiom( objectProperty, range_class );

                if(!domainsAndRanges.contains(axiom_range)) {

                    domainsAndRanges.add(axiom_range);
                }
            }

            if(!domainsAndRanges.isEmpty()) {

                manager.addAxioms(ontology, domainsAndRanges);
            }

        }

        //----------*********----------Adding Datatype Properties in the OWLOntology Object----------*********----------

        ResultSet rs_data_properties = null;

        try {
            rs_data_properties = tdb_handler.execute_GetOntologyDataPropertyDeclaration();

        } catch (TDBHandler.OntologiesNotAvailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while(rs_data_properties.hasNext()) {

            QuerySolution solution = rs_data_properties.nextSolution();

            OWLDataProperty dataProperty = dfactory.getOWLDataProperty(IRI.create( solution.get("dataProperty").toString() ));

            Set<OWLAxiom> domainsAndRanges = new HashSet<OWLAxiom>();

            OWLSubDataPropertyOfAxiom subDataPropertyAxiom = null;

            if( !(solution.get("parentProperty")==null) ) {

                OWLDataProperty parentProperty = dfactory.getOWLDataProperty(IRI.create( solution.get("parentProperty").toString() ));

                subDataPropertyAxiom = dfactory.getOWLSubDataPropertyOfAxiom(dataProperty, parentProperty);

                if( !ontology.containsAxiom(subDataPropertyAxiom) ) {

                    AddAxiom addAxiom_subProperty = new AddAxiom(ontology, subDataPropertyAxiom);

                    manager.applyChange(addAxiom_subProperty);
                }
            }

            if( !(solution.get("domain")==null) ) {

                OWLClass domain_class = dfactory.getOWLClass(IRI.create( solution.get("domain").toString() ));

                OWLDataPropertyDomainAxiom axiom_domain = dfactory.getOWLDataPropertyDomainAxiom( dataProperty, domain_class );

                if(!domainsAndRanges.contains( axiom_domain )) {

                    domainsAndRanges.add( axiom_domain );
                }
            }

            if( !(solution.get("range")==null) ) {

                XSDTypeDataHandler xsd_data_handler = new XSDTypeDataHandler();

                OWLDatatype owl_datatype = xsd_data_handler.get_OWLXSDDataTypeOnly( solution.get("range").toString(), dfactory );

                if(owl_datatype!=null) {

                    OWLDataPropertyRangeAxiom axiom_range = dfactory.getOWLDataPropertyRangeAxiom( dataProperty, owl_datatype );

                    domainsAndRanges.add( axiom_range );
                }
            }

            if(!domainsAndRanges.isEmpty()) {

                manager.addAxioms(ontology, domainsAndRanges);
            }

        }

        the_Vocabularies = ontology;

        return ontology;
    }


    ArrayList<String> getObjectPropertiesFromVocabularies(){

        Set<OWLObjectProperty> vocabulary_obj_properties = the_Vocabularies.getObjectPropertiesInSignature();

        ArrayList<String> vocab_obj_properties = null;

        Iterator itor = vocabulary_obj_properties.iterator();

        while(itor.hasNext()){

            OWLObjectProperty obj_property = (OWLObjectProperty) itor.next();

            vocab_obj_properties.add( obj_property.toStringID() );

            //System.out.println("Retrieved Property ID: " + obj_property.toStringID() );

        }

        return null;
    }
}

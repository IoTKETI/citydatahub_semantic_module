package com.semantics.validation;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.*;

import java.util.*;

public class EntailmentValidator {

    private static int DOMAIN_HIERARCHY = 1;
    private static int RANGE_HIERARCHY = 2;

    private OWLOntology ONTOLOGY;

    private Map<OWLObjectPropertyAssertionAxiom,Boolean> VALID_OBJECT_PROPERTY_AXIOM;

    private Map<OWLDataPropertyAssertionAxiom,Boolean> VALID_DATA_PROPERTY_AXIOM;

    LinkedList<OWLClass> domain_class_hierarchy, range_class_hierarchy;

    EntailmentValidator(){

        VALID_OBJECT_PROPERTY_AXIOM = new HashMap<OWLObjectPropertyAssertionAxiom,Boolean>();

        VALID_DATA_PROPERTY_AXIOM = new HashMap<OWLDataPropertyAssertionAxiom,Boolean>();
    }

    public boolean validateEntailment(OWLOntology ontology, OWLOntology instances) {

        setONTOLOGY(ontology); //--------------------------------------> Setting Ontology, so it can be easily used throughout the class.

        Set<OWLAxiom> axiom_set = new HashSet<OWLAxiom>();

        // Adding individual assertions to the base ontology object, so that single object will be checked for inconsistency.
        for ( final OWLAxiom one_axiom : instances.getAxioms() ) {

            axiom_set.add(one_axiom);

            //System.out.println( "Added Axiom: " + one_axiom );
        }

        Reasoner reasoner = new Reasoner(ontology);



        return reasoner.isEntailed(axiom_set) && checkPropertyEntailment( instances );
    }


    private boolean checkPropertyEntailment(OWLOntology instances) {
        // TODO Auto-generated method stub

        Iterator<OWLNamedIndividual> individual_itor = instances.getIndividualsInSignature().iterator();

        while(individual_itor.hasNext()) {

            //MultiValuedMap<OWLObjectProperty, OWLClass> assertedDomainClasses = new ArrayListValuedHashMap<OWLObjectProperty, OWLClass>();

            OWLNamedIndividual domain_individual = individual_itor.next();

            //System.out.println( "\n Domain Individual: " + domain_individual.toString() );

            ArrayList<OWLClass> currentDomainIndividualClasses = getClassesOfIndividual( domain_individual );

            Iterator<OWLObjectPropertyAssertionAxiom> assertion_itor = instances.getObjectPropertyAssertionAxioms(domain_individual).iterator();

            while(assertion_itor.hasNext()) { //----> Loop for Checking Object Property Assertions

                OWLObjectPropertyAssertionAxiom assertion_axiom = assertion_itor.next();

                //System.out.println( "\nObject Property Assertion: " + assertion_axiom.toString() );

                OWLObjectProperty obj_property = assertion_axiom.getProperty().asOWLObjectProperty();

                OWLNamedIndividual range_individual = assertion_axiom.getObject().asOWLNamedIndividual();

                ArrayList<OWLClass> currentRangeIndividualClasses = getClassesOfIndividual( range_individual );

                boolean domain_and_range_assertion = validateDomainAndRangeAssertion(
                        obj_property,
                        currentDomainIndividualClasses,
                        currentRangeIndividualClasses
                );

                VALID_OBJECT_PROPERTY_AXIOM.put( assertion_axiom , domain_and_range_assertion );
            }

            Iterator<OWLDataPropertyAssertionAxiom> data_assertion_itor = instances.getDataPropertyAssertionAxioms(domain_individual).iterator();

            while(data_assertion_itor.hasNext()) { //----> Loop for Checking Data Property Assertions (Domain Only!)

                OWLDataPropertyAssertionAxiom data_assertion_axiom = data_assertion_itor.next();

                //System.out.println( "\nObject Property Assertion: " + assertion_axiom.toString() );

                OWLDataProperty data_property = data_assertion_axiom.getProperty().asOWLDataProperty();

                boolean domain_assertion = validateDomainAssertion(
                        data_property,
                        currentDomainIndividualClasses
                );

                VALID_DATA_PROPERTY_AXIOM.put( data_assertion_axiom , domain_assertion );
            }
        }

        return allObjectPropertyAxiomsValid() && allDataPropertyAxiomsValid();
    }



    private boolean validateDomainAndRangeAssertion(
            OWLObjectProperty obj_property,
            ArrayList<OWLClass> domain_assertions,
            ArrayList<OWLClass> range_assertions
    ) {
        // TODO Auto-generated method stub

        ArrayList<Boolean> valid_domains;
        ArrayList<Boolean> valid_ranges;

        ////--------------------!!!!!!!!!!-------------------- Validating Domains --------------------!!!!!!!!!!--------------------\\\\

        Iterator<OWLObjectPropertyDomainAxiom> domain_itor = ONTOLOGY.getObjectPropertyDomainAxioms(obj_property).iterator();

        if(domain_itor.hasNext()) { //---> To see if there are any domains declares for the given Object Property

            domain_class_hierarchy = new LinkedList<OWLClass>();

            valid_domains = initializeList(new ArrayList<Boolean>(), domain_assertions.size(), false);

            while(domain_itor.hasNext()) { //---->Populating "domain_class_hierarchy"

                domain_class_hierarchy.push( domain_itor.next().getDomain().asOWLClass() );
            }

            while( !domain_class_hierarchy.isEmpty() && !allTrue(valid_domains) ) {

                OWLClass domain_declared_class = popAndUpdateClassHierarchy( DOMAIN_HIERARCHY );

                //System.out.println( "		Domain Declared Class: " + domain_declared_class);

                int valid_index = classExistsInDomainsOrRanges(  domain_declared_class , domain_assertions ); //--> This is for checking the actual declared class
                //---> Since this class will not be added in the subclass list

                if(valid_index!=-1) {		valid_domains.set(valid_index, true);		}
            }

        }else { //---> Means there are no domains defined for the given Object Property.
            //----> Means any domain class declaration is valid for this assertion.

            //System.out.println("				--!!!-- No declared domains found, So all domain classes are taken as valid! --!!!--");

            valid_domains = initializeList(new ArrayList<Boolean>(), domain_assertions.size(), true);
        }

        //System.out.println("\n				All Domains Valid: " + allTrue(valid_domains));

        ////--------------------!!!!!!!!!!-------------------- Validating Ranges --------------------!!!!!!!!!!--------------------\\\\

        Iterator<OWLObjectPropertyRangeAxiom> range_itor = ONTOLOGY.getObjectPropertyRangeAxioms(obj_property).iterator();

        if(range_itor.hasNext()) { //---> To see if there are any ranges declared for the given Object Property

            range_class_hierarchy = new LinkedList<OWLClass>();

            valid_ranges = initializeList(new ArrayList<Boolean>(), range_assertions.size(), false);

            while(range_itor.hasNext()) { //---->Populating "range_class_hierarchy"

                range_class_hierarchy.push( range_itor.next().getRange().asOWLClass() );
            }

            while( !range_class_hierarchy.isEmpty() && !allTrue(valid_ranges) ) {

                OWLClass range_declared_class = popAndUpdateClassHierarchy( RANGE_HIERARCHY );

                //System.out.println( "		Range Declared Class: " + range_declared_class);

                int valid_index = classExistsInDomainsOrRanges(  range_declared_class , range_assertions ); //--> This is for checking the actual declared class
                //---> Since this class will not be added in the subclass list

                if(valid_index!=-1) {		valid_ranges.set(valid_index, true);		}
            }


        }else { //---> Means there are no domains defined for the given Object Property.
            //----> Means any domain class declaration is valid for this assertion.

            //System.out.println("				--!!!-- No declared domains found, So all range classes are taken as valid! --!!!--");

            valid_ranges = initializeList(new ArrayList<Boolean>(), range_assertions.size(), true);
        }

        //System.out.println("\n				All Ranges Valid: " + allTrue(valid_ranges));

        return allTrue(valid_domains) && allTrue(valid_ranges);
    }




    private boolean validateDomainAssertion(OWLDataProperty data_property, ArrayList<OWLClass> domain_assertions) {
        // TODO Auto-generated method stub

        ArrayList<Boolean> valid_domains;

        Iterator<OWLDataPropertyDomainAxiom> domain_itor = ONTOLOGY.getDataPropertyDomainAxioms(data_property).iterator();

        if(domain_itor.hasNext()) { //---> To see if there are any domains declares for the given Object Property

            domain_class_hierarchy = new LinkedList<OWLClass>();

            valid_domains = initializeList(new ArrayList<Boolean>(), domain_assertions.size(), false);

            while(domain_itor.hasNext()) { //---->Populating "domain_class_hierarchy"

                domain_class_hierarchy.push( domain_itor.next().getDomain().asOWLClass() );
            }

            while( !domain_class_hierarchy.isEmpty() && !allTrue(valid_domains) ) {

                OWLClass domain_declared_class = popAndUpdateClassHierarchy( DOMAIN_HIERARCHY );

                //System.out.println( "		Domain Declared Class: " + domain_declared_class);

                int valid_index = classExistsInDomainsOrRanges(  domain_declared_class , domain_assertions ); //--> This is for checking the actual declared class
                //---> Since this class will not be added in the subclass list

                if(valid_index!=-1) {		valid_domains.set(valid_index, true);		}
            }

        }else { //---> Means there are no domains defined for the given Object Property.
            //----> Means any domain class declaration is valid for this assertion.

            //System.out.println("				--!!!-- No declared domains found, So all domain classes are taken as valid! --!!!--");

            valid_domains = initializeList(new ArrayList<Boolean>(), domain_assertions.size(), true);
        }

        //System.out.println("\n				All Domains Valid: " + allTrue(valid_domains));

        return allTrue(valid_domains);
    }


    ///////////////////////////////////////////////////////////////////////////////
    //---> Check if the Declared class in the class hierarchy of the ontology	 //
    //----> is matched with any of the domain/range assertions defined in the 	//
    //-----> Object Property. And then return the index of the matched assertion //
    //------> so that the appropriate boolean value can be set to true.		  //
    ///////////////////////////////////////////////////////////////////////////////
    private int classExistsInDomainsOrRanges(OWLClass class_from_hierarchy, ArrayList<OWLClass> domainOrRange_assertions) {
        // TODO Auto-generated method stub

        int valid_index = -1;

        for(int i=0; i<domainOrRange_assertions.size() && valid_index==-1; i++) {

            OWLClass asserted_class = domainOrRange_assertions.get(i);

            //System.out.println("				Checking Hierarchy Class: " + class_from_hierarchy + "   with assertion: " + asserted_class);

            if( class_from_hierarchy.equals( asserted_class ) ) {

                valid_index = i;
            }
        }

        //System.out.println( "				Valid Index: " + valid_index );

        return valid_index;
    }



    public String showValidatedObjectPropertyAssertions(boolean valid_assertions, boolean invalid_assertions) {

        String validation_log = "";

        if(!VALID_OBJECT_PROPERTY_AXIOM.isEmpty()) {

            Iterator<Map.Entry<OWLObjectPropertyAssertionAxiom,Boolean>> valid_axiom_itor =  VALID_OBJECT_PROPERTY_AXIOM.entrySet().iterator();

            //System.out.println( "Object Property Assertion Validation Log: " );
            if(valid_assertions || invalid_assertions){

                validation_log += "Object Property Assertion Validation Log: \n";
            }

            while( valid_axiom_itor.hasNext() ) {

                Map.Entry<OWLObjectPropertyAssertionAxiom,Boolean> axiom_set = (Map.Entry<OWLObjectPropertyAssertionAxiom,Boolean>) valid_axiom_itor.next();

                if(axiom_set.getValue()) {

                    if(valid_assertions) {

                        //System.out.println( "	" + axiom_set.getKey() );
                        //System.out.println( "	" + "Validation Status: Valid." );
                        validation_log += "	" + axiom_set.getKey() + " \n";
                        validation_log += "	" + "Validation Status: Valid. \n";
                    }
                }else {

                    if(invalid_assertions) {

                        //System.out.println( "	" + axiom_set.getKey() );
                        //System.out.println( "		" + "Validation Status: Invalid." );
                        validation_log += " 	" + axiom_set.getKey() + " \n";
                        validation_log += "	" + "Validation Status: Invalid. \n";
                    }
                }
            }

        }
        return validation_log;
    }




    public String showValidatedDataPropertyAssertions(boolean valid_assertions, boolean invalid_assertions) {

        String validation_log = "";

        if(!VALID_DATA_PROPERTY_AXIOM.isEmpty()) {

            Iterator<Map.Entry<OWLDataPropertyAssertionAxiom,Boolean>> valid_axiom_itor =  VALID_DATA_PROPERTY_AXIOM.entrySet().iterator();

            //System.out.println( "Data Property Assertion Validation Log: " );

            if(valid_assertions || invalid_assertions){

                validation_log += "Data Property Assertion Validation Log: \n";
            }

            while( valid_axiom_itor.hasNext() ) {

                Map.Entry<OWLDataPropertyAssertionAxiom,Boolean> axiom_set = (Map.Entry<OWLDataPropertyAssertionAxiom,Boolean>) valid_axiom_itor.next();

                if(axiom_set.getValue()) {

                    if(valid_assertions){

                        //System.out.println( "	" + axiom_set.getKey() );
                        //System.out.println( "	" + "Validation Status: Valid." );
                        validation_log += "	" + axiom_set.getKey() + " \n";
                        validation_log += "	" + "Validation Status: Valid. \n";
                    }
                }else {

                    if(invalid_assertions){

                        //System.out.println( "	" + axiom_set.getKey() );
                        //System.out.println( "		" + "Validation Status: Invalid." );
                        validation_log += " 	" + axiom_set.getKey() + " \n";
                        validation_log += "	" + "Validation Status: Invalid. \n";
                    }
                }
            }

        }

        return validation_log;
    }




    private OWLClass popAndUpdateClassHierarchy(int hierarchy_id) {
        // TODO Auto-generated method stub

        OWLClass popped_class = null;

        if ( hierarchy_id == DOMAIN_HIERARCHY ) {

            popped_class = domain_class_hierarchy.pop();

            Iterator<OWLClassExpression> subClass_itor = popped_class.getSubClasses(ONTOLOGY).iterator();

            while(subClass_itor.hasNext()) {

                domain_class_hierarchy.push( subClass_itor.next().asOWLClass() );

            }

        }else if( hierarchy_id == RANGE_HIERARCHY ) {

            popped_class = range_class_hierarchy.pop();

            Iterator<OWLClassExpression> subClass_itor = popped_class.getSubClasses(ONTOLOGY).iterator();

            while(subClass_itor.hasNext()) {

                range_class_hierarchy.push( subClass_itor.next().asOWLClass() );

            }
        }

        return popped_class;
    }






    ///////////////////////////////////////////////////////////////
    //--------> Get the Class types of the given Individual
    ///////////////////////////////////////////////////////////////
    ArrayList<OWLClass> getClassesOfIndividual(OWLNamedIndividual individual){

        Iterator<OWLClassExpression> individual_type_itor = individual.getTypes(ONTOLOGY).iterator();

        ArrayList<OWLClass> classes_of_individual = new ArrayList<OWLClass>();

        while(individual_type_itor.hasNext()) { //----> Retrieving Domain Classes

            OWLClass individual_type = individual_type_itor.next().asOWLClass();

            if(!individual_type.getIRI().toString().equals("http://www.w3.org/2002/07/owl#Namedindividual")) {

                //System.out.println( "\n     Individual's Class:  " + individual_type.toString() );

                classes_of_individual.add( individual_type );
            }
        }


        return classes_of_individual;
    }


    private boolean allObjectPropertyAxiomsValid() {

        boolean allAxiomsValid = true;

        if(!VALID_OBJECT_PROPERTY_AXIOM.isEmpty()) {

            Iterator<Map.Entry<OWLObjectPropertyAssertionAxiom,Boolean>> valid_axiom_itor =  VALID_OBJECT_PROPERTY_AXIOM.entrySet().iterator();

            while(valid_axiom_itor.hasNext() && allAxiomsValid) {

                Map.Entry<OWLObjectPropertyAssertionAxiom,Boolean> axiom_set = (Map.Entry<OWLObjectPropertyAssertionAxiom,Boolean>) valid_axiom_itor.next();

                allAxiomsValid = axiom_set.getValue();
            }

        }

        return allAxiomsValid;
    }



    private boolean allDataPropertyAxiomsValid() {

        boolean allAxiomsValid = true;

        if(!VALID_DATA_PROPERTY_AXIOM.isEmpty()) {

            Iterator<Map.Entry<OWLDataPropertyAssertionAxiom,Boolean>> valid_axiom_itor =  VALID_DATA_PROPERTY_AXIOM.entrySet().iterator();

            while(valid_axiom_itor.hasNext() && allAxiomsValid) {

                Map.Entry<OWLDataPropertyAssertionAxiom,Boolean> axiom_set = (Map.Entry<OWLDataPropertyAssertionAxiom,Boolean>) valid_axiom_itor.next();

                allAxiomsValid = axiom_set.getValue();
            }

        }

        return allAxiomsValid;
    }




    private boolean allTrue(ArrayList<Boolean> valid_list) {

        boolean allTrue = true;

        for(int i=0; i<valid_list.size() && allTrue; i++) {

            allTrue = valid_list.get(i);
        }

        return allTrue;
    }


    private ArrayList<Boolean> initializeList(ArrayList<Boolean> list, int size, boolean initial_value) {
        // TODO Auto-generated method stub

        for(int i=0; i<size; i++) {		list.add(initial_value);	}


        return list;
    }

    public void setONTOLOGY(OWLOntology ont) {
        ONTOLOGY = ont;
    }
}

package com.semantic.annotator.validation;

import org.apache.jena.rdf.model.Model;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class Validator
{
    OWLOntology vocabulary, instanceOntology;
    OntologyModeler modeler;

    String entailment_validation_log = "";

    ///////////////////////////////////////////////////////////////////
    //-->Constructor to initialize the ontology after retrieving from
    //--->TDB which will be used to validate the instances.
    ////////////////////////////////////////////////////////////////////
    public Validator(){

        modeler = new OntologyModeler();

        vocabulary  = null;

        try {
            vocabulary = modeler.create_OntologyFromTDB();

        } catch (OWLOntologyCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("validator() called");
    }

    ///////////////////////////////////////////////////
    //-->Function to validate if the provided instance
    //--->is consistent
    ////////////////////////////////////////////////////
    public boolean isConsistent() throws NullOntologyException {

        boolean isConsistent = false;

        if(instanceOntology!=null && vocabulary!=null) {

            ConsistencyValidator consistency_validator = new ConsistencyValidator();

            isConsistent = consistency_validator.validateConsistency(vocabulary, instanceOntology);

        }else {

            throw new NullOntologyException("Provide valid OWLOntology.");
        }

        return isConsistent;
    }

    ///////////////////////////////////////////////////
    //-->Function to validate if the provided instance
    //--->is Entailed
    ////////////////////////////////////////////////////
    public boolean isEntailed(boolean valid_obj_prop_assert_log, boolean invalid_obj_prop_assert_log, boolean valid_data_prop_assert_log, boolean invalid_data_prop_assert_log) throws NullOntologyException {

        boolean isEntailed = false;

        if(instanceOntology!=null && vocabulary!=null) {

            EntailmentValidator entailment_validator = new EntailmentValidator();

            isEntailed = entailment_validator.validateEntailment(vocabulary, instanceOntology);

            entailment_validation_log = "";

            entailment_validation_log = entailment_validator.showValidatedObjectPropertyAssertions(valid_obj_prop_assert_log, invalid_obj_prop_assert_log); //-->For showing the Validation log

            entailment_validation_log += entailment_validator.showValidatedDataPropertyAssertions(valid_data_prop_assert_log, invalid_data_prop_assert_log); //-->For showing the Validation log

        }else {

            throw new NullOntologyException("Provide valid OWLOntology.");
        }

        return isEntailed;
    }

    ///////////////////////////////////////////////////////////////////
    //-->Function to create the ontology from Model object which
    //--->contains the instance. This ontology will be validated.
    ////////////////////////////////////////////////////////////////////
    public void createOWLOntologyFromInstance(Model instanceModel) {

        try {
            instanceOntology = modeler.modelToOWLOntology(instanceModel);

        } catch (OWLOntologyCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public class NullOntologyException extends Exception{

        String exception_Message ;

        public NullOntologyException(String message) {

            exception_Message = message;
        }

        public String toString() {

            return ("Exception: Null Ontology!. " + exception_Message);
        }
    }

    public String show_EntailmentValidationLog(){

        return entailment_validation_log;
    }
}

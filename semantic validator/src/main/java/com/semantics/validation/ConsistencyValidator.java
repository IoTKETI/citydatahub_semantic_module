package com.semantics.validation;

import com.clarkparsia.owlapi.explanation.BlackBoxExplanation;
import com.clarkparsia.owlapi.explanation.HSTExplanationGenerator;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.util.Set;

public class ConsistencyValidator {

    private OWLOntologyManager manager = null;
    private Reasoner.ReasonerFactory factory = null;

    ConsistencyValidator(){

        manager = OWLManager.createOWLOntologyManager();
    }

    public OWLReasoner initializeReasoner(OWLOntology ontology) {

        factory = new Reasoner.ReasonerFactory();

        Configuration configuration = new Configuration();

        configuration.throwInconsistentOntologyException=false;

        // Let us confirm that the ontology is inconsistent
        return factory.createReasoner(ontology, configuration);
    }

    public boolean validateConsistency(OWLOntology ontology, OWLOntology instances) {

        // Adding individual assertions to the base ontology object, so that single object will be checked for inconsistency.
        for ( final OWLAxiom axiom : instances.getAxioms() ) {

            manager.addAxiom(ontology, axiom);
            //System.out.println( "Added Axiom: " + axiom );
        }

        OWLReasoner reasoner = initializeReasoner(ontology);

        boolean instanceConsistent = reasoner.isConsistent();

        if(!instanceConsistent) {

            explainInconsistency(ontology, reasoner);
        }

        return instanceConsistent;
    }



    public void explainInconsistency(OWLOntology ontology, OWLReasoner reasoner) {

        //Let's see why the ontology is inconsistent.
        System.out.println("Computing explanations for the inconsistency...");

        factory = new Reasoner.ReasonerFactory() {

            protected OWLReasoner createHermiTOWLReasoner(org.semanticweb.HermiT.Configuration configuration,OWLOntology ontology) {

                // don't throw an exception since otherwise we cannot compte explanations
                configuration.throwInconsistentOntologyException=false;

                return new Reasoner(configuration,ontology);
            }
        };

        BlackBoxExplanation exp = new BlackBoxExplanation(ontology, factory, reasoner);

        HSTExplanationGenerator multExplanator=new HSTExplanationGenerator(exp);

        OWLDataFactory dataFactory = manager.getOWLDataFactory();

        // Now we can get explanations for the inconsistency
        displayExplanation( multExplanator.getExplanations(dataFactory.getOWLThing()) );
    }

    public void displayExplanation(Set<Set<OWLAxiom>> explanations) {

        // Let us print them. Each explanation is one possible set of axioms that cause the
        // unsatisfiability.
        for (Set<OWLAxiom> explanation : explanations) {

            System.out.println("------------------");

            System.out.println("Axioms causing the inconsistency: ");

            for (OWLAxiom causingAxiom : explanation) {

                System.out.println(causingAxiom);
            }

            System.out.println("------------------");
        }
    }
}

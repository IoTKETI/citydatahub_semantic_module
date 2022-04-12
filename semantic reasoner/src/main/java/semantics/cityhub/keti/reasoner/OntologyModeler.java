package semantics.cityhub.keti.reasoner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.SWRLVariable;

import semantics.cityhub.keti.APIConfiguration;
import semantics.cityhub.keti.model.SWRLRuleEntity;
import semantics.cityhub.keti.utils.SWRLRuleParser.NoPropertyFoundException;


public class OntologyModeler {

	private OntologyModel ONT_MODEL;
	
	
//	public OntologyModel getOntologyModel(String ont_ModelId){
//		
//		boolean model_found = false;
//		
//		OntologyModel model_to_return = null;
//		
//		for(int i=0; i<list_ONT_MODELs.size() && !model_found; i++) {
//			
//			if(list_ONT_MODELs.get(i).getId().equals(ont_ModelId)) {
//				
//				model_to_return = list_ONT_MODELs.get(i);
//				model_found = true;
//			}
//		}
//		
//		return model_to_return;
//	}
	
	
	
	public void generateOntologyModel(Map<String, Object> json_modelValues) throws NoPropertyFoundException {
		
		String in_model_id = (String) json_modelValues.get("id");
		
		ONT_MODEL  = new OntologyModel( in_model_id );
		
		generateDataProperties( (ArrayList<String>) json_modelValues.get("dataProperties"), ONT_MODEL );
		
		generateSWRLVariables( (ArrayList<String>) json_modelValues.get("variables") , ONT_MODEL);
		
		ONT_MODEL.setSwrl_rule_string( json_modelValues.get("rule").toString() );
			
		System.out.println("New Rules Parsed: " + ONT_MODEL.ontology.getAxioms() );
	}
	
	
	
	
	public void generateRetrievedModel(SWRLRuleEntity rule_Entity) throws NoPropertyFoundException {
		
		ONT_MODEL  = new OntologyModel( rule_Entity.getRule_Id() );
		
		try {
			generateDataProperties( Arrays.asList( rule_Entity.getData_Properties().split(",") ) , ONT_MODEL );
			
			generateSWRLVariables( Arrays.asList( rule_Entity.getSwrl_Variables().split(",") ) , ONT_MODEL);
			
			ONT_MODEL.setSwrl_rule_string( rule_Entity.getSwrl_RuleString() );
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		System.out.println("Saved Rule Parsed: " + ONT_MODEL.ontology.getAxioms() );
	}
	
	
	
	
	   //////////////////////////////////////////////////////////////////
	  //--->Main Function to create owl:DatatypeProperty assertions
	 //---->From the received list of property strings 
	//////////////////////////////////////////////////////////////////
	void generateDataProperties(List<String> properties, OntologyModel ont_model) {
		
		OWLDataProperty parentProperty = ont_model.dataFactory.getOWLDataProperty( IRI.create( "http://www.w3.org/2002/07/owl#topDataProperty" ) );
		
		Set<OWLAxiom> axiom_propDeclarations = new HashSet<OWLAxiom>();
		
		for(String property:properties) {
			
			OWLDataProperty dataProperty = ont_model.dataFactory.getOWLDataProperty( IRI.create( APIConfiguration.PREFIX_URI + "#" + property ) );
			
			ont_model.dataProperties.put( property , dataProperty );
			
			axiom_propDeclarations.add(		ont_model.dataFactory.getOWLDeclarationAxiom( dataProperty )		);
			
			//axiom_propDeclarations.add( 	ONT_MODELER.dataFactory.getOWLSubDataPropertyOfAxiom( dataProperty , parentProperty )	 );
			
//			AddAxiom addAxiom_subProperty = new AddAxiom(	
//														ONT_MODELER.ontology, 
//														ONT_MODELER.dataFactory.getOWLSubDataPropertyOfAxiom( dataProperty , parentProperty )
//														);
//			
//			ONT_MODELER.manager.applyChange( addAxiom_subProperty );
		}
		
		if( !axiom_propDeclarations.isEmpty() ) {
			
			ont_model.manager.addAxioms(ont_model.ontology, axiom_propDeclarations);
		}
		
		System.out.println("axioms : " + ont_model.ontology.getAxiomCount() );
		System.out.println("DataProperties : " + ont_model.dataProperties );
	}
	
	
	
	   //////////////////////////////////////////////////////////
	  //--->Main Function to create SWRL Variables
	 //---->From the received list of SWRL Variable strings 
	///////////////////////////////////////////////////////////	
	public void generateSWRLVariables(List<String> swrl_vars, OntologyModel ont_Model) {
		// TODO Auto-generated method stub
		
		for(int i=0; i<swrl_vars.size(); i++) {
			
			SWRLVariable var = ont_Model.dataFactory.getSWRLVariable(
											IRI.create(APIConfiguration.PREFIX_URI + swrl_vars.get(i))
											);
			
			ont_Model.swrlVariables.put( swrl_vars.get(i) , var );	
		}
	}
	
	
	public OntologyModel getOntologyModel() {
		
		return ONT_MODEL;
	}
	
	
	public void setOtologyModel(OntologyModel model) {
		
		this.ONT_MODEL = model;
	}
	
	
//	int getModelIndex(String model_id) {
//		
//		int model_index = -1;
//		
//		for(int i=0; i<list_ONT_MODELs.size() && (model_index==-1); i++) {
//			
//			if(list_ONT_MODELs.get(i).getId().equals(model_id)) {
//		
//				model_index = i;
//			}
//		}
//		
//		return model_index;
//	}
}

package semantics.cityhub.keti.reasoner;

import java.io.BufferedReader;
import java.util.Map;

import semantics.cityhub.keti.model.SWRLRuleEntity;
import semantics.cityhub.keti.utils.CSVHandler;
import semantics.cityhub.keti.utils.NullOntologyModelerException;
import semantics.cityhub.keti.utils.SWRLRuleParser;
import semantics.cityhub.keti.utils.SWRLRuleParser.NoPropertyFoundException;

/**
 * Hello world!
 *
 */
public class SemanticReasoner 
{
	
	private OntologyModeler ONT_MODELER;
	
	
	public SemanticReasoner() {
		
		ONT_MODELER = new OntologyModeler();
	}
	
	
    public static void main( String[] args )
    {
        System.out.println( 
        		"This is a Main Class for Semantic Reasoner Project. \n"
        	  + "This class can be used as the module for user Applications.\n"
        	  + "In order to use this project as a module, this main method should be removed. \n"
        	  + "So that, other point of execution can be defined and Reasoning functionalities can be \n"
        	  + "called from this class. " );
    }

	
	      //////////////////////////////////////////////////////////
	     //-->Compose OntologyModel from JSON payload send 
	    //--->by the API Controller. The created model will
	   //---->be stored by the OntologyModeler. Therefore,
	  //----->instance of this class can be used to 
	 //------>retrieve the OntologyModel if required.
	//////////////////////////////////////////////////////////
	public void composeModel( Map<String, Object> json_modelValues ) throws NoPropertyFoundException{
		
		ONT_MODELER.generateOntologyModel(json_modelValues);
		
		SWRLRuleParser ruleParser = new SWRLRuleParser();
		
		ONT_MODELER.setOtologyModel(
								ruleParser.parseSWRLRule( 
												ONT_MODELER.getOntologyModel().getSwrl_rule_string(), 
												ONT_MODELER.getOntologyModel()
												)
								);
	}
	
	
	   /////////////////////////////////////////////////////////
	  //-->Compose OntologyModel from SWRLRuleEntity object
	 //--->which represents the Entity in the SQL dataset.
	/////////////////////////////////////////////////////////
	public void composeModel(SWRLRuleEntity rule_Entity) throws NoPropertyFoundException {
	
		ONT_MODELER.generateRetrievedModel(rule_Entity);
		
		SWRLRuleParser ruleParser = new SWRLRuleParser();
		
		ONT_MODELER.setOtologyModel(
				ruleParser.parseSWRLRule( 
								ONT_MODELER.getOntologyModel().getSwrl_rule_string(), 
								ONT_MODELER.getOntologyModel()
								)
				);
	}
	
	
	public CSVHandler performInferencing(BufferedReader bReader) throws NullOntologyModelerException {
		
		RuleEngineHandler ruleEngine = new RuleEngineHandler( ONT_MODELER.getOntologyModel() );
		
		return ruleEngine.executeInferencing(bReader);
	}
	
	
	
	public SWRLRuleEntity exportRuleEntity() {
		
		return ONT_MODELER.getOntologyModel().getSWRLRuleEntity();
	}
	
	
	public OntologyModel getOntologyModel() {
		
		return ONT_MODELER.getOntologyModel();
	}
}

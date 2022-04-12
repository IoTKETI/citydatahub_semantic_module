package semantics.cityhub.keti.reasoner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.SWRLVariable;

import semantics.cityhub.keti.APIConfiguration;
import semantics.cityhub.keti.model.SWRLRuleEntity;

public class OntologyModel {
	
	String id;

	public OWLOntology ontology = null;
	public OWLOntologyManager manager;
	public OWLDataFactory dataFactory;
	public HashMap<String,OWLDataProperty> dataProperties;
	public HashMap<String,SWRLVariable> swrlVariables;
	public List<String>[] rule_Components;
	public String swrl_rule_string;
	
	public boolean INITIALIZED = false;
	
	  ////////////////////////////////////
	 //---->Constructor (Empty)
	////////////////////////////////////
	OntologyModel() {
		
	}
	
	
	  ////////////////////////////////////
	 //---->Constructor (Parameterized)
	////////////////////////////////////
	OntologyModel(String in_id) {
		
		id = in_id;
		
		manager = OWLManager.createOWLOntologyManager();
		dataFactory = manager.getOWLDataFactory();
		
		//OWLXMLDocumentFormat owlXmlFormat = new OWLXMLDocumentFormat();
		
		try {
			ontology = manager.createOntology();
			//manager.setOntologyFormat( ontology , new OWLXMLDocumentFormat() );
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dataProperties = new HashMap<String,OWLDataProperty>();
		swrlVariables = new HashMap<String,SWRLVariable>();
		
		rule_Components = new ArrayList[2]; 			//body_elements, head_elements;
		rule_Components[0] = new ArrayList<String>();	//--> This is body
		rule_Components[1] = new ArrayList<String>();	//--> This is head
		
		INITIALIZED = true;
	}
	
	
	boolean isSWRLVariable(String variableTag) {
		
		return swrlVariables.containsKey(variableTag);
	}
	
	
	public boolean isDataProperty(String elementTag) {
		
		return ontology.containsDataPropertyInSignature( IRI.create( APIConfiguration.PREFIX_URI + "#" + elementTag ) ) && 
				dataProperties.containsKey(elementTag);
	}
	
	
	  //////////////////////////////////////////////////
	 //---->Getters And Setters
	//////////////////////////////////////////////////
	
	
	  /////////////////
	 //---->Getters
	/////////////////
	
	
	SWRLRuleEntity getSWRLRuleEntity() {
		
		SWRLRuleEntity ruleEntity = new SWRLRuleEntity();
		
		ruleEntity.setRule_Id(id);
		ruleEntity.setSwrl_RuleString(swrl_rule_string);
		String comaSeperatedProp = "", comaSeperatedVar = "";
		
		Iterator propItor = dataProperties.keySet().iterator();
		
		while(propItor.hasNext()) { 
			
			comaSeperatedProp += propItor.next().toString();
			
			if(propItor.hasNext()) {
				comaSeperatedProp += ",";
			}
		}
		
		ruleEntity.setData_Properties(comaSeperatedProp);
		
		Iterator varItor = swrlVariables.keySet().iterator();
		
		while(varItor.hasNext()) { 
			
			comaSeperatedVar += varItor.next().toString();
			
			if(varItor.hasNext()) {
				comaSeperatedVar += ",";
			}
		}
		
		ruleEntity.setSwrl_Variables(comaSeperatedVar);
		
//		try {
			
			//ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
			
			//manager.saveOntology( ontology , output_stream );
			
			//ruleEntity.setOntology_OWLSerialized( new String( output_stream.toByteArray() ) );
			ruleEntity.setOntology_OWLSerialized("n/a");
			
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return ruleEntity;
	}
	
	
	OWLDataProperty getDataPropertyWithTag(String propertyTag) {
		
		return dataProperties.get( propertyTag );
	}
	
	List<String> getDataPropertiesAsList(){
	
		List<String> list_properties = new ArrayList<String>();
		
		Iterator itor = dataProperties.keySet().iterator();
		
		while(itor.hasNext()) {
			
			list_properties.add( (String) itor.next() );
			
		}
		
		return list_properties;
	}
	
	
	public JSONArray getDataPropertiesAsJSON(){
		
		JSONArray json_properties = new JSONArray();
		
		Iterator itor = dataProperties.keySet().iterator();
		
		while(itor.hasNext()) {
			
			json_properties.add( (String) itor.next() );
			
		}
		
		return json_properties;
	}
	
	
	List<String> getVariablesAsList(){
		
		List<String> list_variables = new ArrayList<String>();
		
		Iterator itor = swrlVariables.keySet().iterator();
		
		while(itor.hasNext()) {
			
			list_variables.add( (String) itor.next() );
			
		}
		
		return list_variables;
	}
	
	
	public JSONArray getDataVariablesAsJSON(){
		
		JSONArray json_variables = new JSONArray();
		
		Iterator itor = swrlVariables.keySet().iterator();
		
		while(itor.hasNext()) {
			
			json_variables.add( (String) itor.next() );
			
		}
		
		return json_variables;
	}
	
	
//	JSONArray getRuleStringsAsJSON(){
//		
//		JSONArray json_ruleStrings = new JSONArray();
//		
//		Iterator itor = swrl_rule_strings.iterator();
//		
//		while(itor.hasNext()) {
//			
//			json_ruleStrings.add( (String) itor.next() );
//			
//		}
//		
//		return json_ruleStrings;
//	}
	
	
	public String getId() {
		return id;
	}
	
	public String getSwrl_rule_string() {
		
		return swrl_rule_string;
	}


	  /////////////////
	 //---->Setters
	/////////////////
	
	public void setId(String id) {
		this.id = id;
	}


	public void setSwrl_rule_string(String swrl_rule_string) {
		
		this.swrl_rule_string = swrl_rule_string;
	}
}

package semantics.cityhub.keti.utils;

import java.io.IOException;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import semantics.cityhub.keti.model.SWRLRuleEntity;
import semantics.cityhub.keti.reasoner.OntologyModel;

public class JSONDataHandler {
	
	 public String toJSONString( SWRLRuleEntity rule_Entity) {
		
		JSONObject json_ruleSetData = new JSONObject();
		
		try {
			
			json_ruleSetData.put( "id", rule_Entity.getRule_Id() );
			json_ruleSetData.put( "dataProperties" ,  rule_Entity.getDataPropertiesAsJSON() );
			json_ruleSetData.put( "variables" ,  rule_Entity.getDataVariablesAsJSON() );
			json_ruleSetData.put( "rule" , rule_Entity.getSwrl_RuleString()  );
		
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return prettyJSONString( json_ruleSetData );
	}
	 
	 
	 String toJSONString(OntologyModel ont_model) {
			
			JSONObject json_ruleSetData = new JSONObject();
			
			json_ruleSetData.put( "id", ont_model.getId() );
			json_ruleSetData.put( "dataProperties" ,  ont_model.getDataPropertiesAsJSON() );
			json_ruleSetData.put( "variables" ,  ont_model.getDataVariablesAsJSON() );
			json_ruleSetData.put( "rule" , ont_model.getSwrl_rule_string()  );
			
			return prettyJSONString( json_ruleSetData );
		}
	
	
	String prettyJSONString( JSONObject ugly_json ) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		return gson.toJson( ugly_json );
	}
	
}

package com.cityhub.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class JSONHandler {

	String[] hierarchy_prefix = null;
	String class_for_hierarchy = null;
	int hierarchy_limit = 0;
	
	public String parseGraphDataToJSON(ResultSet results, String resultVar, String jsonObjName, int prefix_format) {
		
		// Put Graphs in Array........
		JSONArray json_Graphs = new JSONArray();
		JSONArray json_Prefixes = new JSONArray();
		
		if( resultVar.equals("hierarchy") && class_for_hierarchy!=null ) { //--> This means Class Hierarchy Function has 
																		  //---> called this Function. This clause will add
																		 //----> This is adding the first class in hierarchy.
			JSONObject one_first_prefix = new JSONObject();
			
			if(prefix_format==1) {
				
				one_first_prefix.put( hierarchy_prefix[0] , hierarchy_prefix[1]  );
				
			}else if(prefix_format==2) {
				
				one_first_prefix.put( "prefix", hierarchy_prefix[0] );
				one_first_prefix.put( "namespaceIRI", hierarchy_prefix[1] );
			}
			
			json_Prefixes.add(one_first_prefix);
			json_Graphs.add( class_for_hierarchy );
		}
		
		PrefixRepository prefixRep = new PrefixRepository();
		
		while ( results.hasNext() ) {
			
			QuerySolution solution = results.nextSolution();
			
			String graph_uri = solution.get(resultVar).toString();
			
			//System.out.println( graph_uri );
			
			String prefix_enabled_uri[] = prefixRep.getPrefixEnabledURI(graph_uri);
					
			if( prefix_enabled_uri[0] !=null ) {
				
				JSONObject one_prefix = new JSONObject();
				
				if(prefix_format==1) {
					
					one_prefix.put( prefix_enabled_uri[0], prefix_enabled_uri[1] );
					
				}else if(prefix_format==2) {
					
					one_prefix.put( "prefix", prefix_enabled_uri[0] );
					one_prefix.put( "namespaceIRI", prefix_enabled_uri[1] );
				}
				
				json_Prefixes.add(one_prefix);
			}

			json_Graphs.add( prefix_enabled_uri[2] );
		}
		
		JSONObject obj = new JSONObject();
		
		obj.put( jsonObjName, json_Graphs );
		obj.put( "prefixList", json_Prefixes );
	
		return prettyJSONString( obj );
	}
	
	
	public String parseTripleDataToJSON(ResultSet results) {
		
		JSONObject obj = new JSONObject();
		
		JSONArray triple_set = new JSONArray();
		
		while ( results.hasNext() ) {
			
			JSONObject one_triple = new JSONObject();
			
			QuerySolution solution = results.nextSolution();
			
			one_triple.put( "sub", solution.get("s").toString() );
			one_triple.put( "pred", solution.get("p").toString() );
			one_triple.put( "obj", solution.get("o").toString() );
			
			triple_set.add(one_triple);
		}
		
		obj.put( "triples", triple_set);
		
		return prettyJSONString( obj );
	}
	
	
	String prettyJSONString(JSONObject uglyJSON) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		
		return gson.toJson( uglyJSON );
		
	}


	public String parseNormalFormatDataToJSON(ResultSet results, String jsonObjName, int prefix_format) {
		// TODO Auto-generated method stub
		
		JSONArray json_entity_desc = new JSONArray();
		JSONArray json_Prefixes = new JSONArray();
		
		PrefixRepository prefixRep = new PrefixRepository();
		
		while(results.hasNext()) {
			
			QuerySolution solution = results.nextSolution();
			
			String prefix_enabled_subj[] = prefixRep.getPrefixEnabledURI( solution.get("s").toString() );
			String prefix_enabled_pred[] = prefixRep.getPrefixEnabledURI( solution.get("p").toString() );
			String prefix_enabled_obj[] = prefixRep.getPrefixEnabledURI( solution.get("o").toString() );
			
			String prefix_enabled_triple[][] = { prefix_enabled_subj, prefix_enabled_pred, prefix_enabled_obj };
			
			JSONObject one_prefix;
			
			for(int i=0; i<prefix_enabled_triple.length; i++) {
				
				if( prefix_enabled_triple[i][0] !=null ) {
				
					one_prefix = new JSONObject();
					
					if(prefix_format==1) {
					
						one_prefix.put( prefix_enabled_triple[i][0], prefix_enabled_triple[i][1] );
					
					}else if(prefix_format==2) {
						
						one_prefix.put( "prefix", prefix_enabled_triple[i][0] );
						one_prefix.put( "namespaceIRI", prefix_enabled_triple[i][1] );
					}
					
					json_Prefixes.add(one_prefix);
				}
			}
			
			//Now inserting the actual triple as JSON data
			
			JSONObject one_json_triple = new JSONObject();
			
			one_json_triple.put( "subject" , prefix_enabled_triple[0][2] );
			one_json_triple.put( "predicate" , prefix_enabled_triple[1][2] );
			one_json_triple.put( "object" , prefix_enabled_triple[2][2] );
			
			json_entity_desc.add(one_json_triple);
			
		}// End of while() loop....
		
		JSONObject obj = new JSONObject();
		
		obj.put( "prefixList", json_Prefixes );
		obj.put( jsonObjName , json_entity_desc );
		
		return prettyJSONString( obj );
	}
	
	public String parseSimpleFormatDataToJSON(ResultSet results, int prefix_format) {
		
		JSONArray json_entity_desc = new JSONArray();
		JSONArray json_Prefixes = new JSONArray();
		
		PrefixRepository prefixRep = new PrefixRepository();
		
		//To Arrange all the Data First
		HashMap<String, ArrayList<String>> entity_description = new HashMap<String, ArrayList<String>>();
		
		while(results.hasNext()) {
			
			QuerySolution solution = results.nextSolution();
			
			String prefix_enabled_pred[] = prefixRep.getPrefixEnabledURI( solution.get("p").toString() );
			String prefix_enabled_obj[] = prefixRep.getPrefixEnabledURI( solution.get("o").toString() );
			
			String prefix_enabled_triple[][] = { prefix_enabled_pred, prefix_enabled_obj };
			
			JSONObject one_prefix;
			
			for(int i=0; i<prefix_enabled_triple.length; i++) { // Adding Prefixes......
				
				if( prefix_enabled_triple[i][0] !=null ) {
				
					one_prefix = new JSONObject();
					
					if(prefix_format==1) {
					
						one_prefix.put( prefix_enabled_triple[i][0], prefix_enabled_triple[i][1] );
					
					}else if(prefix_format==2) {
						
						one_prefix.put( "prefix", prefix_enabled_triple[i][0] );
						one_prefix.put( "namespaceIRI", prefix_enabled_triple[i][1] );
					}
					
					json_Prefixes.add(one_prefix);
				}// Adding Prefixes Finishes here....
			}
			
			// now adding prefix enabled entity_description triples
			
			ArrayList<String> objects = entity_description.get( prefix_enabled_triple[0][2] );
			
			if(objects==null) { //means this predicate came first time
				
				objects = new ArrayList<String>();
			}
			
			objects.add( prefix_enabled_triple[1][2] );
				
			entity_description.put( prefix_enabled_triple[0][2] , objects);	
		}
		
		//Now finally adding triples in the json object
		
		for(String predicate : entity_description.keySet()) {
			
			JSONObject same_predicate_data = new JSONObject();
			JSONArray json_object_list = new JSONArray();
			
			ArrayList<String> object_list = entity_description.get(predicate);
				
			json_object_list.addAll( object_list );
			
			same_predicate_data.put( predicate, json_object_list );
			
			json_entity_desc.add( same_predicate_data );
		}
		
		JSONObject obj = new JSONObject();
		
		obj.put( "prefixList", json_Prefixes );
		obj.put( "entity-description" , json_entity_desc );
		
		return prettyJSONString( obj );
	}


	public void setClass_for_hierarchy(String hierarchy_class) {
		this.class_for_hierarchy = hierarchy_class;
	}


	public void setHierarchy_prefix(String[] hierarchy_prefix) {
		this.hierarchy_prefix = hierarchy_prefix;
	}


	public void setHierarchy_limit(int hierarchy_limit) {
		this.hierarchy_limit = hierarchy_limit;
	}
}

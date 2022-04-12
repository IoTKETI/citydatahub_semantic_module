package com.cityhub.semantic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import com.cityhub.semantic.MapListStructure.RedundantKeyException;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryEngine;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

public class TDBHandler {

	String tdb_access_url, tdb_userName, tdb_userPass;
	
	TDBHandler(){
		
		tdb_access_url = ServerConfiguration.TDB_BASE_URL;
		tdb_userName = ServerConfiguration.TDB_USER;
		tdb_userPass = ServerConfiguration.TDB_PASS;
	}
	
	  /////////////////////////////////////////////
	 //------->Function for Insert Triple Data
	/////////////////////////////////////////////	
	void execute_InsertTripleData(Map<String, Object> triple_data) {
		
		ArrayList<Object> triples =  (ArrayList<Object>) triple_data.get("triples");
		
		String subject = (String) ((Map<String, String>)triples.get(0)).get("sub");
		
		System.out.println(subject);
		System.out.println(subject);
		System.out.println(subject);
		
		String query_string = //"PREFIX common: <http://www.city-hub.kr/ontologies/2019/1/common#> \n"
				//+ "PREFIX parking: <http://www.city-hub.kr/ontologies/2019/1/parking#> \n"
				//+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				
				  "INSERT { "
				+ "        GRAPH <" + triple_data.get("graph_uri") + ">"
				+ "         { \n";
		
		for(int i=0; i<triples.size();i++) {
			
				query_string += "            " + ((Map<String, String>)triples.get(i)).get("subject") + " "
				+ "" + ((Map<String, String>)triples.get(i)).get("predicate") + " " + ((Map<String, String>)triples.get(i)).get("object") + " . \n";
			
		}
		
		query_string += " }\n"
					 + "}";
		
		showQuery(query_string);
				
		execute_Update(query_string);
	}
	
	
	  /////////////////////////////////////////////
	 //------->Function for Delete Graph
	/////////////////////////////////////////////	
	void execute_DeleteGraph(String graph_uri) {
		
		String delete_query = "DROP SILENT GRAPH <" + graph_uri + ">";
		
		showQuery(delete_query);
		
		execute_Update(delete_query);
	}
	
	
	  /////////////////////////////////////////////
	 //------->Function for Graph List
	/////////////////////////////////////////////
	String  execute_GraphSearchQuery(boolean graphTypes[], LinkedList<String> keyWords, int prefix_format, long limit) {
		
		String query_string = "SELECT DISTINCT ?graph \n"
							+ "WHERE { GRAPH ?graph \n"
							+ "	{ ?s ?p ?o } \n";
							//+ "  FILTER( \n"
				//+ "} ORDER BY ?graph";
		
		String filter = "   FILTER ( \n";
		
		if(graphTypes[0] && !graphTypes[1]) {
		
			filter += "             !CONTAINS( STR(?graph), \"#\" ) && \n";
			
		}else if(!graphTypes[0] && graphTypes[1]) {
			
			filter += "             CONTAINS( STR(?graph), \"#\" )  && \n";
			
		}
		
		if(!keyWords.isEmpty()) {
			
			filter += "            ( \n";
			
			for(int i=0; i<keyWords.size(); i++) {
				
				filter += "              CONTAINS( STR(?graph), \"" + keyWords.get(i) + "\" ) \n";
				
				if( i < (keyWords.size()-1) ) {
					   
					filter += "              || \n";
				}
			}
			filter += "            )   \n";
		}
														//----> Increase this range "60" if some URI 
														//----> is not shown in the result.......
		//filter += "            ( STRLEN( STR(?graph) ) <= " + ServerConfiguration.MAX_ONTOLOGY_LENGTH + " ) \n"
		  filter += "       )   \n";
		
		query_string += filter;
		
		query_string += "} \n"
					// + "OFFSET 1 \n"
					 + "LIMIT " + String.valueOf(limit);
		
		System.out.println("\nConsole LOG--> Generated Query: \n" );
		System.out.println( query_string );
		
		JSONHandler json_handler = new JSONHandler();
		
		return json_handler.parseGraphDataToJSON( execute_Query( query_string ), "graph", "graphList", prefix_format);
	}



	  /////////////////////////////////////////////
	 //------->Function for Entity List
	/////////////////////////////////////////////
	public String execute_EntityListSearchQuery(LinkedList keyWords, LinkedList<String> entityTypes, int prefix_format, long limit) {
		// TODO Auto-generated method stub
		
		String query_string = "";
		
		PrefixRepository prefix_Rep = new PrefixRepository();
		
		for(int i=0; i<entityTypes.size(); i++) {
			
			String prefix_namespace[] = prefix_Rep.getNamespaceOf( entityTypes.get(i).split(":")[0] );
			
			if(prefix_namespace[0]!=null) {
				
				String prefix_to_add = "PREFIX " + prefix_namespace[0] + ": " + "<" + prefix_namespace[1] + "> \n";
				
				if(!query_string.contains(prefix_to_add)) {		query_string += prefix_to_add;		}
			}
		}
		
		query_string += "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				     + "SELECT DISTINCT ?entity \n"
					 + "WHERE { "
					 + "GRAPH ?graph { \n";
		
		int j=0;
		while(j<entityTypes.size()-1) {
			
			query_string += "        { ?entity rdf:type " + entityTypes.get(j) + " } \n";
			query_string += "        UNION \n";
			
			j++;
		}
		query_string += "        { ?entity rdf:type " + entityTypes.get(j) + " } \n";
		
		
		if(!keyWords.isEmpty()) {
		
		String filter = "   FILTER ( \n";
		
			for(int k=0; k<keyWords.size(); k++) {
				
				filter += "              CONTAINS( LCASE(STR(?entity)), \"" + keyWords.get(k) + "\" ) \n";
				
				if( k < (keyWords.size()-1) ) {
					   
					filter += "              || \n";
				}
			}
			
			filter += "              )   \n";
			
			query_string += filter;
		}
		
		query_string += "} } \n"
				+ "LIMIT " + String.valueOf(limit);
		
				//+ "ORDER BY ?graph \n";
		
		System.out.println("\n Query String: \n" + query_string);
		
		JSONHandler json_handler = new JSONHandler();
		
		return json_handler.parseGraphDataToJSON( execute_Query( query_string ), "entity", "entity-list", prefix_format );
	}

	  /////////////////////////////////////////////
	 //------->Function for Entity Info
	/////////////////////////////////////////////	
	public String execute_GetEntityInfo(String entityURI, int responseType, int prefix_format, long limit) {
		
		String query_string = "";
		
		PrefixRepository prefix_Rep = new PrefixRepository();
			
		String prefix_namespace[] = { null, null };
		
		    if(!entityURI.contains("#")) {
		    
		    	prefix_namespace = prefix_Rep.getNamespaceOf( entityURI.split(":")[0] );
		    }
			
			if(prefix_namespace[0]!=null) {
				
				query_string += "PREFIX " + prefix_namespace[0] + ": " + "<" + prefix_namespace[1] + "> \n";
			}
			
			String select_query_part = "", where_query_part = "";
			
			if(query_string.contains("PREFIX ")) {	
				
				select_query_part = "SELECT (" + entityURI	+ " AS ?s ) ?p ?o \n";
				where_query_part = entityURI + " ?p ?o \n";
				
			}else {
				
				select_query_part = "SELECT <" + entityURI	+ "> AS ?s ?p ?o \n";
				where_query_part = "<" + entityURI + "> ?p ?o \n";
			}
			
			query_string += select_query_part 
					      + "WHERE{ GRAPH ?g { \n"
					      + "                  " + where_query_part
					      + "       } \n"
					      + "} \n"
					      + "LIMIT " + String.valueOf(limit);
			
			System.out.println("\n Query String: \n" + query_string);
			
			JSONHandler json_handler = new JSONHandler();
			 
		if(responseType==1) { // simple format
			
			return json_handler.parseSimpleFormatDataToJSON( execute_Query(query_string), prefix_format );
			
		}else { // normal format
		
			return json_handler.parseNormalFormatDataToJSON( execute_Query(query_string), "entity-description", prefix_format );
		}
	}
	

	  /////////////////////////////////////////////
	 //-------->Function for Graph Info
	/////////////////////////////////////////////
	public String execute_GetGraphInfo(String graphURI, int prefix_format, long limit, boolean saveAsFile) {
		// TODO Auto-generated method stub
		
		String query_string = "";
		
		PrefixRepository prefix_Rep = new PrefixRepository();
		
		String prefix_namespace[] = { null, null };
		
	    if( graphURI.contains(":") ) {
	    	
	    	prefix_namespace = prefix_Rep.getNamespaceOf( graphURI.split(":")[0] );
	    
	    }else {
	    	
	    	prefix_namespace = prefix_Rep.getNamespaceOf( graphURI );
	    	prefix_namespace[1] = prefix_namespace[1].split("#")[0];
	    	graphURI = graphURI + ":";
	    }
		
		if(prefix_namespace[0]!=null) {
			
			query_string += "PREFIX " + prefix_namespace[0] + ": " + "<" + prefix_namespace[1] + "> \n";
		}
		
		query_string += "SELECT DISTINCT * \n"
				      + "FROM " + graphURI + " \n"
				      + "WHERE { \n"
				     + "        ?s ?p ?o  \n"
				     + "} \n"
				     + "LIMIT " + String.valueOf(limit);;
		
		System.out.println("\n Query String: \n" + query_string);
		
		JSONHandler json_handler = new JSONHandler();
		
		String returned_Output = null;
		
		if(saveAsFile) {
			
			try {
				OWLOntologyModeler modeler = new OWLOntologyModeler();
				
				returned_Output = modeler.convertResponseToSerializationFormat( execute_Query(query_string) );
				
			} catch (RedundantKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OWLOntologyCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			
			returned_Output = json_handler.parseNormalFormatDataToJSON( execute_Query(query_string), "graphTriples", prefix_format );
		}
		
		return returned_Output;
	}
	
	
	
	  /////////////////////////////////////////////
	 //------>Function for Individual List
	/////////////////////////////////////////////	
	public String execute_IndividualSearchQuery(LinkedList<String> classIds, int prefix_format, long limit) {
		// TODO Auto-generated method stub
		
		String query_string = "", where_statements = "";
		
		PrefixRepository prefix_Rep = new PrefixRepository();
		
		for(int i=0; i<classIds.size(); i++) {
			
			String prefix_namespace[] = { null, null };
			
		    if( classIds.get(i).contains(":") ) {
		    
		    	prefix_namespace = prefix_Rep.getNamespaceOf( classIds.get(i).split(":")[0] );
		    }
			
			if(prefix_namespace[0]!=null) {
				
				query_string += "PREFIX " + prefix_namespace[0] + ": " + "<" + prefix_namespace[1] + "> \n";
			}
			
			where_statements += "       { ?individual rdf:type " + classIds.get(i) + " } \n";
			
			if( i < (classIds.size()-1) ) {
				
				where_statements += "       UNION \n";
			}
			
			
		}// End of for() loop....
		
		query_string += "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				      + "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
				      + "SELECT DISTINCT ?individual \n"
				      + "WHERE { \n"
				      + "GRAPH ?graph{ \n"
				      + "       ?individual rdf:type owl:NamedIndividual. \n"
				      + where_statements
				      + "} \n"
				      + "} \n"
				      + "LIMIT " + String.valueOf(limit);
		
		System.out.println("\nConsole LOG--> Generated Query: \n" );
		System.out.println( query_string );
		
		JSONHandler json_handler = new JSONHandler();
		
		return json_handler.parseGraphDataToJSON( execute_Query( query_string ), "individual", "named-individuals", prefix_format);
	}
	
	
	  /////////////////////////////////////////////
	 //------>Function for CLass Hierarchy
	/////////////////////////////////////////////	
	public String execute_GetClassHierarchy(String classURI, int prefix_format, long limit) {
		// TODO Auto-generated method stub
		
		String query_string = "";
		
		PrefixRepository prefix_Rep = new PrefixRepository();
		
		String prefix_namespace[] = { null, null };
		
	    if( classURI.contains(":") ) {
	    
	    	prefix_namespace = prefix_Rep.getNamespaceOf( classURI.split(":")[0] );
	    }
		
		if(prefix_namespace[0]!=null) {
			
			query_string += "PREFIX " + prefix_namespace[0] + ": " + "<" + prefix_namespace[1] + "> \n";
		}
		
//		query_string += "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
//				      + "SELECT ?hierarchy \n"
//				      + "WHERE { \n"
//				      + "       { \n"
//				      + "        SELECT * \n"
//				      + "        WHERE { \n"
//				      + "               ?x rdfs:subClassOf ?hierarchy . \n"
//				      + "        } \n"
//				      + "       } \n"
//				      + "      OPTION ( TRANSITIVE, t_distinct, t_in(?x), t_out(?hierarchy) ). \n"
//				      + "      FILTER( ?x = " + classURI + " ) \n"
//				      + "} \n"
//				      + "LIMIT " + String.valueOf(limit);
		
		query_string += "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
			      + "SELECT DISTINCT ?hierarchy \n"
			      + "WHERE { \n"
			      + "       { \n"
			      + "        SELECT * \n"
			      + "        WHERE { GRAPH ?g { \n"
			      + "               ?x rdfs:subClassOf+ ?hierarchy . \n"
			      + "        } } \n"
			      + "       } \n"
			      //+ "      OPTION ( TRANSITIVE, t_distinct, t_in(?x), t_out(?hierarchy) ). \n"
			      + "      FILTER( ?x = " + classURI + " ) \n"
			      + "} \n"
			      + "LIMIT " + String.valueOf(limit);
		
		System.out.println("\n Query String: \n" + query_string);
		
		JSONHandler json_handler = new JSONHandler();
		
		json_handler.setClass_for_hierarchy(classURI);
		json_handler.setHierarchy_prefix(prefix_namespace);
		json_handler.setHierarchy_limit((int) limit);
		
		return json_handler.parseGraphDataToJSON( execute_Query( query_string ), "hierarchy", "class-hierarchy", prefix_format );
	}
	
	
	public String execute_SelectAllQuery(LinkedList graph_list) {
		// TODO Auto-generated method stub
		
		String graphs = "";
		
		for( int gItor=0; gItor<graph_list.size(); gItor++ ) {
					
					graphs += "FROM <" + graph_list.get(gItor) + ">  \n";
		}
		
		String query_string = "SELECT DISTINCT * \n"
							+ graphs
							+ "WHERE  \n"
							+ "	{ ?s ?p ?o } ";
		
		
		JSONHandler json_handler = new JSONHandler();
		
		return json_handler.parseTripleDataToJSON( execute_Query(query_string) );
	}
	
	
	
	
	private ResultSet execute_Query(String query) {
		// TODO Auto-generated method stub
		
		VirtGraph set = new VirtGraph (tdb_access_url, tdb_userName, tdb_userPass);
		
		Query sparql = QueryFactory.create( query );
		
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		
		return vqe.execSelect();
	}
	
	
	void execute_Update(String update_query) {
		
		/*			STEP 1			url --> "jdbc:virtuoso://localhost:1111"*/
		VirtGraph set = new VirtGraph ( tdb_access_url, tdb_userName, tdb_userPass );
		
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create( update_query, set );
        vur.exec();
		
        System.out.println("Executed Update Query....");
	}
	
	void showQuery(String query) {
		
		System.out.println( "Generated Query: \n" );
		System.out.println( query );
	}
	
}

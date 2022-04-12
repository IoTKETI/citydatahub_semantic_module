package com.semantic;

import java.util.HashMap;
import java.util.Map;

public class QuerySet {

private Map<String, String> querySet = new HashMap<String, String>();
	
	QuerySet(){
		
		querySet.put("graph_serach_query", 
				"SELECT DISTINCT ?graph "
						+ "WHERE { GRAPH ?graph "
						+ "	{ ?s ?p ?o } "
						+ "} ORDER BY ?graph");
//------------------------------------------------------------------------------------------------------------------------		
		querySet.put("select_all_query", 
				"SELECT DISTINCT * "
						+ "WHERE "
						+ "	{ ?s ?p ?o } ");
//------------------------------------------------------------------------------------------------------------------------		
		querySet.put("create_graph_query", 
				"CREATE GRAPH "
						+ "<http://localhost:8890/DAV/home/exUser/rdf_sink/smart_parking.owl/LDITEMP> ");
		
//------------------------------------------------------------------------------------------------------------------------		
		querySet.put("drop_graph_query", 
				"DROP SILENT GRAPH "
						+ "<http://localhost:8890/DAV/home/exUser/rdf_sink/vocabulary/parking.owl> ");
		//http://localhost:8890/DAV/home/exUser/rdf_sink/ParkingLot_And_Spot_Instance.rdf/LDITEMP
//------------------------------------------------------------------------------------------------------------------------				
		querySet.put("insert_triple_query", 
				"INSERT INTO GRAPH <http://localhost:8890/DAV/home/exUser/rdf_sink/park.rdf/LDITEMP> "
						+ "{ <http://www.semanticweb.org/smart-city/ontologies/2019/1/parkingLot#parkingSpot_3> "
						+ "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
						+ "<http://www.w3.org/2002/07/owl#NamedIndividual> . "
						+ "<http://www.semanticweb.org/smart-city/ontologies/2019/1/parkingLot#parkingSpot_3> "
						+ "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
						+ "<http://www.semanticweb.org/smart-city/ontologies/2019/1/parkingLot#ParkingSpot> . }");
//------------------------------------------------------------------------------------------------------------------------				
		querySet.put("delete_triple_query", 
				"DELETE FROM GRAPH <http://localhost:8890/DAV/home/exUser/rdf_sink/park.rdf/LDITEMP> "
						+ "{ <http://www.semanticweb.org/smart-city/ontologies/2019/1/parkingLot#parkingSpot_3> "
						+ "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
						+ "<http://www.w3.org/2002/07/owl#NamedIndividual> . "
						+ "<http://www.semanticweb.org/smart-city/ontologies/2019/1/parkingLot#parkingSpot_3> "
						+ "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
						+ "<http://www.semanticweb.org/smart-city/ontologies/2019/1/parkingLot#ParkingSpot> . }");
//------------------------------------------------------------------------------------------------------------------------
		querySet.put("search_update_query",
				"PREFIX common: <http://www.city-hub.kr/ontologies/2019/1/common#> \n"
			  + "PREFIX parking: <http://www.city-hub.kr/ontologies/2019/1/parking#> \n"
			  + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
					+ "WITH <http://localhost:8890/DAV/home/exUser/rdf_sink/ParkingLot_And_Spot_Instance.rdf/LDITEMP> "
					+ "DELETE "
						+ "{ ?state parking:hasParkingStatusValue ?value . } "
					+ "INSERT "
					+ "{ ?state parking:hasParkingStatusValue 'occupied' . } "
					+ "WHERE { "
					+ "         ?state rdf:type parking:ParkingSpotStatus ; " 
					+ "                common:isPropertyOf parking:ParkingSpot_1 ;"
					+ "                parking:hasParkingStatusValue ?value . "
					+ " } ");

//------------------------------------------------------------------------------------------------------------------------
		querySet.put("construct_query", 
				"CONSTRUCT { ?x <http://www.semanticweb.org/smart-city/ontologies/2019/1/parkingLot#hasParkingSpot> ?y } "
				+ "FROM <http://localhost:8890/DAV/home/exUser/rdf_sink/park.rdf/LDITEMP> "
				+ "WHERE "
				+ "{ ?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
				+ "<http://www.semanticweb.org/smart-city/ontologies/2019/1/parkingLot#ParkingLot>. "
				+ "?y <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
				+ "<http://www.semanticweb.org/smart-city/ontologies/2019/1/parkingLot#ParkingSpot> }");
//------------------------------------------------------------------------------------------------------------------------
		querySet.put("construct_query_2", 
				"PREFIX park: <http://www.semanticweb.org/smart-city/ontologies/2019/1/parkingLot#> \n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "CONSTRUCT { ?x park:hasParkingSpot ?y } "
				+ "FROM <http://localhost:8890/DAV/home/exUser/rdf_sink/park.rdf/LDITEMP> "
				+ "WHERE "
				+ "{ ?x rdf:type park:ParkingLot. "
				+ "?y rdf:type park:ParkingSpot. "
				+ "FILTER STRENDS( STR(?x), SUBSTR( STR(?y), 78, STRLEN(?y) ) ) }");
//------------------------------------------------------------------------------------------------------------------------
		querySet.put("ask_query", 
				"PREFIX park: <http://www.semanticweb.org/smart-city/ontologies/2019/1/parkingLot#> \n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "ASK FROM <http://localhost:8890/DAV/home/exUser/rdf_sink/park.rdf/LDITEMP> "
				+ "WHERE "
				+ "{ ?x rdf:type park:ParkingLot. "
				+ "?y rdf:type park:ParkingSpot }");
//------------------------------------------------------------------------------------------------------------------------		
		querySet.put("describe_query", 
				"DESCRIBE <nodeID://b10295> "
						+ "FROM <http://localhost:8890/DAV/home/exUser/rdf_sink/smart_parking.owl/LDITEMP> ");
//------------------------------------------------------------------------------------------------------------------------			
		querySet.put("graph_with_triple_query", 
				"SELECT DISTINCT * "
						+ "WHERE { GRAPH ?graph "
						+ "{ ?s ?p ?o ."
						+ "FILTER regex(?graph, \"http://localhost:8890/DAV/home/exUser/rdf_sink/\", \"i\")} } limit 100");
//------------------------------------------------------------------------------------------------------------------------			
		querySet.put("select_triple_query",
				"PREFIX common: <http://www.city-hub.kr/ontologies/2019/1/common#> \n"
			  + "PREFIX parking: <http://www.city-hub.kr/ontologies/2019/1/parking#> \n"
			  + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			  + "SELECT DISTINCT ?state "
						+ "WHERE { GRAPH <http://localhost:8890/DAV/home/exUser/rdf_sink/ParkingLot_And_Spot_Instance.rdf/LDITEMP> "
						+ "{ ?state rdf:type parking:ParkingSpotStatus ;"
						+ "         common:isPropertyOf parking:ParkingSpot_1 . } "
						+ "} ");
		
	}
	
	public String getQuery(String the_key){
		
		return querySet.get(the_key);	
	}
}

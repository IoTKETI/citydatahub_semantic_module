package com.semantic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class QueryGenerator {

	
	public String get_Graph_Search_Query() {
		
		String the_Query = "SELECT DISTINCT ?graph \n"
						 + "WHERE { GRAPH ?graph \n"
						 + "		{ ?s ?p ?o } \n"
						 + "        FILTER( !CONTAINS( STR(?graph), \"#\" ) ) \n"
						 + "}";
		
		return the_Query;
	}
	
	
	
	public String get_Class_Hierarchy_Query(EntryInterface ei, LinkedList<String> graph) {
		
		String the_Query = "";		   
		int offset = 0;
		
		String entry_Label = ei.getEntryLabel();
		
		System.out.println("\nConsole LOG--> In QueryGenerator: entry_Label: \n" + entry_Label );
		
		String graphs = "";
		
		String filter = "		 FILTER ( \n";
		
		if(!ei.searchAllClasses()) {

			filter += "				   ( ?s IN( \n";
			
			//System.out.println("\nConsole LOG--> count: \n" + ei.count(entry_Label) );
			
			while( offset < ei.count(entry_Label) ) {
	
				filter += "					<" + ei.getEntry(entry_Label, offset) + ">";
	   
				if( offset < (ei.count(entry_Label)-1) ) {
		   
					filter += ", \n";
				}
				offset++;
			}

			filter += ")					) \n"
					+ "					&& \n";
		}

		filter += "					( ! isBLANK( ?o ) ) \n"
			   	+ "				) \n";
		
		//Put Graph List in the query
		for( int gItor=0; gItor<graph.size(); gItor++ ) {
			
			graphs += "FROM <" + graph.get(gItor) + ">  \n";
		}
		
		 the_Query = "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
				   + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				   + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
				   
				   + "SELECT DISTINCT ?s ?o  ( count(?mid) AS ?count) \n"
				   + graphs
				   + "WHERE {  \n"
				   + "		  { \n"
				   
				   + "			?s rdf:type owl:Class . \n"
				   + "			FILTER NOT EXISTS{ ?s rdfs:subClassOf ?any . } \n"
				   
				   + "		  } UNION \n"
				   + "		  { \n"
				   
				   + "			?s rdf:type owl:Class . \n"
				   + "			?s rdfs:subClassOf+ ?mid . \n"
				   + "			?mid rdfs:subClassOf* ?o . \n"
				   
				   + "		  } \n"
				   + filter
				   + " 		} \n"
				   + "GROUP BY ?s ?o \n"
				   + "ORDER BY DESC(?count) \n";	   
				  
		 showQuery( the_Query );
		
		return the_Query;
	}
	
	
	
	public String get_Object_Property_Query(LinkedList<String> classes, LinkedList<String> graph) {
		
		String inClause = " IN( \n";
		
		String graphs = "";
		
		int listSize = classes.size();
		
		for(int i=0; i<listSize; i++) {
			
			inClause += "                      <" + classes.get(i) + ">";
					
			if( i<(listSize-1) ) {
				
				inClause += ", ";
			}
			
			inClause += "\n";
		}
		inClause += "					) ";
		
		//Put Graph List in the query
		for( int gItor=0; gItor<graph.size(); gItor++ ) {
					
				graphs += "FROM <" + graph.get(gItor) + ">  \n";
		}
		
		String the_Query = "";

		the_Query = "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
		   + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		   + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
		   
		   + "SELECT DISTINCT ?s ?p ?o \n"
		   + graphs
		   + "WHERE {  \n"
		   + "       { \n"
		   
		   + "          ?p rdf:type owl:ObjectProperty . \n"
		   + "          { \n"
		   + "              { \n"
		   + "                ?p rdfs:range ?o . \n"
		   + "               } UNION \n"
		   + "              { \n"
		   + "                   ?p rdfs:range ?node1 . \n"
		   + "                   ?node1 owl:unionOf ?node2 . \n"
		   + "                   { ?node2 rdf:first ?o } UNION \n"
		   + "                   { ?node2 rdf:rest+/rdf:first ?o } \n"
		   + "               } \n"
		   + "              FILTER( ?o " + inClause + " ) . \n"
		 
		   + "             { \n"
		   + "                ?p rdfs:domain ?s .\n"
		   + "                FILTER( ?s " + inClause + " ) . \n"
		   + "              } UNION \n"
		    
		   + "             { \n"
		   + "                FILTER NOT EXISTS{ ?p rdfs:domain ?s . } \n"
		   + "              } \n"
		   
		   + "           } UNION \n"
		   
		   + "          { \n"
		   + "             ?p rdfs:domain ?s .\n"
		   + "             FILTER NOT EXISTS{ ?p rdfs:range ?o . } . \n"
		   + "             FILTER( ?s " + inClause + " ) . \n"
		   + "           } \n"
		   + "        } \n"
		   + "       } ";
		
//		the_Query = "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
//				   + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//				   + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
//				   
//				   + "SELECT DISTINCT ?s ?p ?o \n"
//				   + graphs
//				   + "WHERE {  \n"
//				   + "       { \n"
//				   + "       	{ \n"
//				   
//				   + "			  SELECT ( ?sa AS ?s ) ( ?pa AS ?p ) ( ?oa AS ?o ) \n"
//				   + "			  WHERE{ \n"
//				   
//				   + "          	?pa rdf:type owl:ObjectProperty . \n"
//				   + "          	?pa rdfs:domain ?sa .\n"
//				   + "          	?pa rdfs:range ?oa . \n"
//				   + "		  		FILTER( ?sa " + inClause + " ) . \n"
//				   + "		 		FILTER(  ?oa " + inClause + " ) \n"
//				   
//				   + "			  } \n"
//				   + "        	} UNION \n"
//				   + "		   	{ \n"
//				   
//				   + "			  SELECT ( ?sb AS ?s ) ( ?pb AS ?p ) ( ?ob AS ?o ) \n"
//				   + "			  WHERE{ \n"
//				   
//				   + "          	?pb rdf:type owl:ObjectProperty . \n"
//				   + "          	?pb rdfs:domain ?sb .\n"
//				   + "		  		FILTER NOT EXISTS{ ?pb rdfs:range ?ob . } . \n"
//				   + "		  		FILTER( ?sb " + inClause + " ) . \n"
//				   
//				   + "			  } \n"
//				   + "			} UNION \n"
//				   + "		   	{ \n"
//				   
//				   + "			  SELECT ( ?sc AS ?s ) ( ?pc AS ?p ) ( ?oc AS ?o ) \n"
//				   + "			  WHERE{ \n"
//				   
//				   + "          	?pc rdf:type owl:ObjectProperty . \n"
//				   + "          	?pc rdfs:range ?oc .\n"
//				   + "		  		FILTER NOT EXISTS{ ?pc rdfs:domain ?sc . } \n"
//				   + "		 		FILTER( ?oc " + inClause + " ) \n"
//				   
//				   + "			  } \n"
//				   + "			} \n"
//				   + "       } \n"
//				   + "		} ";
	
//		   + "        VALUES ?check { \n"
//		   + "                      <http://www.city-hub.kr/ontologies/2019/1/common#ProfilePicture> \n"
//		   + "                      <http://www.city-hub.kr/ontologies/2019/1/common#ContactPoint> \n"
//		   + "                      <http://www.city-hub.kr/ontologies/2019/1/common#ZoneBasedLocation> \n"
//		   + "                      <http://www.city-hub.kr/ontologies/2019/1/parking#ParkingLotProfile> \n"
//		   + "                      <http://www.city-hub.kr/ontologies/2019/1/common#GeoProperty> \n"
//		   + "                      <http://www.city-hub.kr/ontologies/2019/1/parking#ParkingLot> \n"
//		   + "                      <http://www.city-hub.kr/ontologies/2019/1/common#Zone> \n"
//		   + "                      <http://www.city-hub.kr/ontologies/2019/1/common#Profile> \n"
//		   + "                      <http://www.city-hub.kr/ontologies/2019/1/common#LocationProperty> \n"
//		   + "                      <http://www.city-hub.kr/ontologies/2019/1/common#Property> \n"
//		   + "                      <http://www.city-hub.kr/ontologies/2019/1/common#CoordinateBasedLocation> \n"
//		   + "                      <http://www.w3.org/2006/time#GeneralDateTimeDescription> \n"
//		   + "                      <http://www.w3.org/2006/time#TemporalPosition> \n"
//		   + "                      <http://www.w3.org/2006/time#Instant> \n"
//		   + "                      <http://www.w3.org/2006/time#TemporalEntity> \n"
//		   + "                      <http://www.city-hub.kr/ontologies/2019/1/common#System> \n"
//		   + "                      <http://www.city-hub.kr/ontologies/2019/1/common#FeatureOfInterest> \n"
//		   + "                      <http://www.w3.org/2006/time#DayOfWeek> \n"
////		   + "                      UNDEF \n"
//		   + "                       } \n"
		
		showQuery( the_Query );
		
		return the_Query;
	}



	public String get_Data_Property_Query(LinkedList<String> classes, LinkedList<String> graph) {
		
		String the_Query = "", domain_values = "", graphs = "";
		
		int listSize = classes.size();
		
		for(int i=0; i<listSize; i++) {
			
			domain_values += "						            <" + classes.get(i) + ">" ;
			
			if( i < (listSize-1) ) {
				domain_values += ", \n";
			}
		}
		
		domain_values += " \n";
		
		//Put Graph List in the query
		for( int gItor=0; gItor<graph.size(); gItor++ ) {
							
				graphs += "FROM <" + graph.get(gItor) + ">  \n";
		}
		
		the_Query =  "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
				   + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				   + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
				   
				   + "SELECT DISTINCT ?s ?p ?o  \n"
				   + graphs
				   + "WHERE { \n"
				   + "           { \n"
				   + "               ?p rdf:type owl:DatatypeProperty . \n"
				   
				   + "               { \n"
				   + "                   { \n"
				   + "                       ?p rdfs:domain ?s . \n"
				   + "                       FILTER( ?s IN( \n"
				   + domain_values
				   + "                        )) . \n"
				   + "                    } UNION \n"
				   + "                   { \n"
				   + "                       FILTER NOT EXISTS { ?p rdfs:domain ?s } \n"
				   + "                    } \n"
				   + "                } . \n"
				   
				   + "               { \n"
				   + "                   { \n"
				   + "                       ?p rdfs:range ?o . \n"
				   + "                    } UNION \n"
				   + "                   { \n"
				   + "                       FILTER NOT EXISTS { ?p rdfs:range ?o } . \n"
				   + "                    } . \n"
				   + "                } \n"
				   + "            } \n"
				   + "  } \n";

//--------------------------------------------------------------------------------------------------------------						   		
//		the_Query =  "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
//				   + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//				   + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
//				   
//				   + "SELECT DISTINCT ?s ?p ?o \n"
//				   + graphs
//				   + "WHERE { \n"
//				   + "			{ \n"
//				   + "				SELECT ( ?domain AS ?s ) ?p ?o \n"
//				   + "				WHERE { \n"
//				   + "						?p rdf:type owl:DatatypeProperty . \n"
//				   + "						?p rdfs:domain ?domain . \n"
//				   + "						?p rdfs:range ?o . \n"
//				   
//				   + "						FILTER( ?domain IN( \n"
//				   + domain_values
//				   + "						)) \n"
//				   + "        		} \n"
//				   + "       	} UNION \n"
//				   + "			{ \n"
//				   + "				SELECT ( ?noS AS ?s ) ?p ?o \n"
//				   + "				WHERE { \n"
//				   + "						?p rdf:type owl:DatatypeProperty . \n"
//				   + "						?p rdfs:range ?o . \n"
//				   
//				   + "						FILTER NOT EXISTS { ?p rdfs:domain ?noS } \n"
//				   + "        		} \n"
//				   + "       	} UNION \n"
//				   + "			{ \n"
//				   + "				SELECT ( ?domain AS ?s ) ?p ( ?noO AS ?o ) \n"
//				   + "				WHERE { \n"
//				   + "						?p rdf:type owl:DatatypeProperty . \n"
//				   + "						?p rdfs:domain ?domain . \n"
//				   
//				   + "						FILTER NOT EXISTS { ?p rdfs:range ?noO } . \n"
//				   + "						FILTER( ?domain IN( \n"
//				   + domain_values
//				   + "						)) \n"
//				   + "        		} \n"
//				   + "       	} UNION \n"
//				   + "			{ \n"
//				   + "				SELECT ( ?noS AS ?s ) ?p ( ?noO AS ?o ) \n"
//				   + "				WHERE { \n"
//				   + "						?p rdf:type owl:DatatypeProperty . \n"
//				   
//				   + "						FILTER NOT EXISTS { ?p rdfs:domain ?noS } \n"
//				   + "						FILTER NOT EXISTS { ?p rdfs:range ?noO } \n"
//				   + "        		} \n"
//				   + "       	} \n"
//				   + "} ";

		
		System.out.println("\nConsole LOG--> Generated Query: \n" + the_Query );
		
		return the_Query;
	}

	
	
	private void showQuery(String the_Query) {
		// TODO Auto-generated method stub
		
		//String NEW_LINE = System.getProperty("line.separator");
		
		String[] line = the_Query.split("\n");
		
		System.out.println("\nConsole LOG--> Generated Query: \n" );
		
		for(int i=0; i<line.length; i++) {
		
			System.out.println( (i+1) + ": " + line[i] );
		}
		
	}
	
}


/*

PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
					 
SELECT DISTINCT ?s ?p ?o 
     FROM <http://localhost:8890/DAV/home/exUser/rdf_sink/vocabulary/common.owl>
     FROM <http://localhost:8890/DAV/home/exUser/rdf_sink/vocabulary/parking.owl>
     FROM <http://localhost:8890/DAV/home/exUser/rdf_sink/vocabulary/external/time.owl>
WHERE { 
       {        
        SELECT ( ?domain AS ?s ) ?p ?o
        WHERE{
              ?p rdf:type owl:DatatypeProperty .
              ?p rdfs:domain ?domain .
              ?p rdfs:range ?o .
              FILTER( ?domain IN(
<http://www.city-hub.kr/ontologies/2019/1/common#Property>, 
<http://www.city-hub.kr/ontologies/2019/1/common#GeoProperty>, 
<http://www.city-hub.kr/ontologies/2019/1/common#FeatureOfInterest>, 
<http://www.city-hub.kr/ontologies/2019/1/parking#ParkingLot>, 
<http://www.city-hub.kr/ontologies/2019/1/parking#ParkingLotProfile>, 
<http://www.city-hub.kr/ontologies/2019/1/common#ZoneBasedLocation>, 
<http://www.city-hub.kr/ontologies/2019/1/common#System>, 
<http://www.city-hub.kr/ontologies/2019/1/common#LocationProperty>, 
<http://www.w3.org/2006/time#TemporalEntity>, 
<http://www.w3.org/2006/time#Instant>, 
<http://www.city-hub.kr/ontologies/2019/1/common#ProfilePicture>, 
<http://www.city-hub.kr/ontologies/2019/1/common#CoordinateBasedLocation>, 
<http://www.city-hub.kr/ontologies/2019/1/common#ContactPoint>, 
<http://www.city-hub.kr/ontologies/2019/1/common#Zone>, 
<http://www.city-hub.kr/ontologies/2019/1/common#Profile>, 
<http://www.w3.org/2006/time#TemporalPosition>, 
<http://www.w3.org/2006/time#GeneralDateTimeDescription>, 
<http://www.w3.org/2006/time#DayOfWeek>
                                 )
                     )
              }
         } UNION
       {
        SELECT ( ?noS AS ?s ) ?p ?o
        WHERE {
               ?p rdf:type owl:DatatypeProperty .
               ?p rdfs:range ?o .
               FILTER NOT EXISTS { ?p rdfs:domain ?noS }
               } 
       } UNION
        {
         SELECT ( ?domain AS ?s ) ?p ( ?noO AS ?o )
         WHERE {
                ?p rdf:type owl:DatatypeProperty .
                ?p rdfs:domain ?domain .
                FILTER NOT EXISTS { ?p rdfs:range ?noO } .
                FILTER( ?domain IN(
<http://www.city-hub.kr/ontologies/2019/1/common#Property>, 
<http://www.city-hub.kr/ontologies/2019/1/common#GeoProperty>, 
<http://www.city-hub.kr/ontologies/2019/1/common#FeatureOfInterest>, 
<http://www.city-hub.kr/ontologies/2019/1/parking#ParkingLot>, 
<http://www.city-hub.kr/ontologies/2019/1/parking#ParkingLotProfile>, 
<http://www.city-hub.kr/ontologies/2019/1/common#ZoneBasedLocation>, 
<http://www.city-hub.kr/ontologies/2019/1/common#System>, 
<http://www.city-hub.kr/ontologies/2019/1/common#LocationProperty>, 
<http://www.w3.org/2006/time#TemporalEntity>, 
<http://www.w3.org/2006/time#Instant>, 
<http://www.city-hub.kr/ontologies/2019/1/common#ProfilePicture>, 
<http://www.city-hub.kr/ontologies/2019/1/common#CoordinateBasedLocation>, 
<http://www.city-hub.kr/ontologies/2019/1/common#ContactPoint>, 
<http://www.city-hub.kr/ontologies/2019/1/common#Zone>, 
<http://www.city-hub.kr/ontologies/2019/1/common#Profile>, 
<http://www.w3.org/2006/time#TemporalPosition>, 
<http://www.w3.org/2006/time#GeneralDateTimeDescription>, 
<http://www.w3.org/2006/time#DayOfWeek>
                                 )
                     )
                } 
        } UNION
       {
        SELECT ( ?noS AS ?s ) ?p ( ?noO AS ?o )
        WHERE {
               ?p rdf:type owl:DatatypeProperty .
 
               FILTER NOT EXISTS { ?p rdfs:domain ?noS } .
               FILTER NOT EXISTS { ?p rdfs:range ?noO }
               } 
       }
     
	}



		String domain_values = "";
		
		int listSize = classes.size();
		
		for(int i=0; i<listSize; i++) {
			
			domain_values += "						<" + classes.get(i) + "> \n" ;
		}
			domain_values += "                      UNDEF \n";
		
			
			the_Query =  "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
					   + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
					   + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
					   
					   + "SELECT DISTINCT (?domain AS ?s) ?p ?o \n"
					   + "FROM <http://localhost:8890/DAV/home/exUser/rdf_sink/vocabulary/common.owl>  \n"
					   + "FROM <http://localhost:8890/DAV/home/exUser/rdf_sink/vocabulary/parking.owl>  \n"
					   + "FROM <http://localhost:8890/DAV/home/exUser/rdf_sink/vocabulary/external/time.owl>  \n"
					   + "WHERE {  \n"
					   + "        VALUES ?domain { \n"
					   + 							domain_values
					   + "                       } \n"
					   + "        VALUES ?dtype { \n"
					   + "                      rdfs:domain \n"
					   + "						rdfs:range \n"
					   + "                       } \n"
					   + "		  ?p rdf:type owl:DatatypeProperty . \n"
					   + "		  ?p rdfs:domain ?domain . \n"
					   + "		  ?p rdfs:range ?o . \n"
					   + "} \n";

 */


//the_Query = "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
//		  + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//		  + "SELECT DISTINCT ?s \n"
//		  + "FROM <http://localhost:8890/DAV/home/exUser/rdf_sink/vocabulary/parking.owl>  \n"
//		  + "FROM <http://localhost:8890/DAV/home/exUser/rdf_sink/vocabulary/common.owl>  \n"
//		  + "WHERE {  \n"
//		  + "   	?s rdf:type owl:Class. \n"
//		  + "FILTER ( "
//		  + "		  STRENDS( STR(?s), \"ParkingLot\" ) "
//		  + "		  || "
//		  + "		  STRENDS( STR(?s), \"ParkingLotProfile\" ) "
//		  + "        ) \n"
//		  + "} \n";

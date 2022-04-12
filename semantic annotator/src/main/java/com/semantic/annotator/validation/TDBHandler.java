package com.semantic.annotator.validation;

import org.apache.jena.query.*;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

import java.util.ArrayList;

public class TDBHandler {


    //String base_tdb_url = "jdbc:virtuoso://172.20.0.129:1111";
    String base_tdb_url = "jdbc:virtuoso://172.20.0.119:1111";
    String user_id = "dba";
    String user_pass = "dba";

    ArrayList<String> ontologies = null;

    void populate_OntologyList(){

        String query_string = "SELECT DISTINCT ?graph \n"
                + "WHERE { GRAPH ?graph \n"
                + "		{ ?s ?p ?o } \n"
                + "               FILTER ( \n"
                + "                       !CONTAINS( STR(?graph), \"#\") && \n"
                + "                          ( \n"
                + "                           CONTAINS( STR(?graph), \"common\") || \n"
                + "                           CONTAINS( STR(?graph), \"parking\") || \n"
                + "                           CONTAINS( STR(?graph), \"air-quality\") || \n"
                + "                           CONTAINS( STR(?graph), \"weather\") \n"
                + "                           ) \n"
                + "                ) \n"
                + "} ";

//        String testing_query_string = "SELECT DISTINCT ?graph \n"
//                + "WHERE { GRAPH ?graph \n"
//                + "		{ ?s ?p ?o } \n"
//                + "               FILTER ( \n"
//                + "                       CONTAINS( STR(?graph), \"common.owl\") || \n"
//                + "                       CONTAINS( STR(?graph), \"parking.owl\") \n"
//                + "                ) \n"
//                + "} ";

        //System.out.println("\nConsole LOG--> Generated Query: \n" );
        //System.out.println( query_string );

        //ResultSetFormatter.out( execute_Query( query_string ) );

        ontologies = parseOntologies( execute_Query( query_string ) );
    }

    ResultSet execute_GetOntologyClassDeclaration() throws OntologiesNotAvailableException {

        if( ontologies==null ) {

            throw new OntologiesNotAvailableException("Please execute \"populate_OntologyList()\" first and populate the Ontology list.");
        }

        String from_graphs = "";

        for( int gItor=0; gItor<ontologies.size(); gItor++ ) {

            from_graphs += "FROM <" + ontologies.get(gItor) + ">  \n";
        }

        String query_string = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
                + "\nSELECT DISTINCT ?class ?parentClass \n\n"
                + from_graphs
                + "WHERE { \n"
                + "       { \n"
                + "        SELECT ?class \n"
                + "        WHERE{ ?class rdf:type owl:Class. \n"
                + "               FILTER( NOT EXISTS { \n"
                + "                                    ?class rdfs:subClassOf ?parentClass. \n"
                + "                                    ?parentClass rdf:type owl:Class \n"
                + "                        }   ) \n"
                + "               FILTER ( \n"
                + "                       !STRSTARTS(STR(?class), \"nodeID:\") \n"
                + "                ) \n"
                + "         } \n"
                + "        } \n"
                + "       UNION \n"
                + "       { \n"
                + "         ?class rdfs:subClassOf ?parentClass. \n"
                + "         FILTER ( !STRSTARTS( STR(?parentClass), \"nodeID:\" ) ) \n"
                + "         } \n"
                + " }";


//        System.out.println("\nConsole LOG--> Generated Query: \n" );
//        System.out.println( query_string );


        //ResultSetFormatter.out( execute_Query( query_string ) );


        return execute_Query( query_string );
    }

    ResultSet execute_GetOntologyObjectPropertyDeclaration() throws OntologiesNotAvailableException {

        if( ontologies==null ) {

            throw new OntologiesNotAvailableException("Please execute \"populate_OntologyList()\" first and populate the Ontology list.");
        }

        String from_graphs = "";

        for( int gItor=0; gItor<ontologies.size(); gItor++ ) {

            from_graphs += "FROM <" + ontologies.get(gItor) + ">  \n";
        }

        String query_string = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
                + "\nSELECT ?objProperty ?domain ?range ?parentProperty \n\n"
                + from_graphs
                + "WHERE{ \n"
                + "       { \n"
                + "         ?objProperty rdf:type owl:ObjectProperty. \n"
                + "         { \n"
                + "         ?objProperty rdfs:domain ?domain. \n"
                + "         ?objProperty rdfs:range ?range. \n"
                + "          } UNION \n"
                + "         { \n"
                + "         ?objProperty rdfs:domain ?domain. \n"
                + "         FILTER NOT EXISTS{ ?objProperty rdfs:range ?range. } \n"
                + "          } UNION \n"
                + "         { \n"
                + "         ?objProperty rdfs:range ?range. \n"
                + "         FILTER NOT EXISTS{ ?objProperty rdfs:domain ?domain. } \n"
                + "          } \n"
                + "        } . \n"
                + "      {"
                + "       { \n"
                + "        FILTER NOT EXISTS{ ?objProperty rdfs:subPropertyOf ?parentProperty } \n"
                + "        } \n"
                + "       UNION \n"
                + "       { \n"
                + "          ?objProperty rdfs:subPropertyOf ?parentProperty. \n"
                + "        } \n"
                + "       } \n"
                //+ "      FILTER( !STRSTARTS( STR(?domain), \"nodeID:\" ) ) \n"
                //+ "      FILTER( !STRSTARTS( STR(?range), \"nodeID:\" ) ) \n"
                + "}";

        //System.out.println("\nConsole LOG--> Generated Query: \n" );
        //System.out.println( query_string );


        //ResultSetFormatter.out( execute_Query( query_string ) );


        return execute_Query( query_string );
        //return null;
    }

    ResultSet execute_GetOntologyDataPropertyDeclaration() throws OntologiesNotAvailableException {

        if( ontologies==null ) {

            throw new OntologiesNotAvailableException("Please execute \"populate_OntologyList()\" first and populate the Ontology list.");
        }

        String from_graphs = "";

        for( int gItor=0; gItor<ontologies.size(); gItor++ ) {

            from_graphs += "FROM <" + ontologies.get(gItor) + ">  \n";
        }

        String query_string = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
                + "\nSELECT ?dataProperty ?domain ?range ?parentProperty \n\n"
                + from_graphs
                + "WHERE{ \n"
                + "       { \n"
                + "         ?dataProperty rdf:type owl:DatatypeProperty. \n"
                + "         { \n"
                + "         ?dataProperty rdfs:domain ?domain. \n"
                + "         ?dataProperty rdfs:range ?range. \n"
                + "          } UNION \n"
                + "         { \n"
                + "         ?dataProperty rdfs:domain ?domain. \n"
                + "         FILTER NOT EXISTS{ ?dataProperty rdfs:range ?range. } \n"
                + "          } UNION \n"
                + "         { \n"
                + "         ?dataProperty rdfs:range ?range. \n"
                + "         FILTER NOT EXISTS{ ?dataProperty rdfs:domain ?domain. } \n"
                + "          } \n"
                + "        } . \n"
                + "      {"
                + "       { \n"
                + "        FILTER NOT EXISTS{ ?dataProperty rdfs:subPropertyOf ?parentProperty } \n"
                + "        } \n"
                + "       UNION \n"
                + "       { \n"
                + "          ?dataProperty rdfs:subPropertyOf ?parentProperty. \n"
                + "        } \n"
                + "       } \n"
                + "}";

        //System.out.println("\nConsole LOG--> Generated Query: \n" );
        //System.out.println( query_string );


        //ResultSetFormatter.out( execute_Query( query_string ) );


        return execute_Query( query_string );
        //return null;
    }

    private ResultSet execute_Query(String query) {
        // TODO Auto-generated method stub

        VirtGraph set = new VirtGraph (base_tdb_url, user_id, user_pass);

        Query sparql = QueryFactory.create( query );

        VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);

        return vqe.execSelect();
    }

    ArrayList<String> parseOntologies(ResultSet rs){

        ArrayList<String> ontologies = new ArrayList<String>();

        while ( rs.hasNext() ) {

            QuerySolution solution = rs.nextSolution();

            ontologies.add( solution.get("graph").toString() );
        }

        return ontologies;
    }

    class OntologiesNotAvailableException extends Exception{

        String exception_Message ;

        public OntologiesNotAvailableException(String message) {

            exception_Message = message;
        }

        public String toString() {

            return ("Exception: Ontologies Not Available!. " + exception_Message);
        }
    }
}

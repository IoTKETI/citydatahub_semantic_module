package com.semantic;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class TemplateSaver {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		/*			STEP 1			url --> "jdbc:virtuoso://localhost:1111"*/
		VirtGraph set = new VirtGraph ("http://localhost:8890/DAV/home/exUser/rdf_sink/vocabulary/common.owl",
				"jdbc:virtuoso://localhost:1111",
				"exUser",
				"keti123");

/*			STEP 2			*/


/*			STEP 3			*/
/*		Select all data in virtuoso	*/
		
		QuerySet qs = new QuerySet();
		
		Query sparql = QueryFactory.create( qs.getQuery("select_all_query") );

/*			STEP 4			*/
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);

		ResultSet results = vqe.execSelect();
		//ResultFormatter.formatMyOutput(results);
		
		saveInJSONFormat(results);
	}
	
	static void saveInJSONFormat(ResultSet res) {
		
		JSONObject obj = new JSONObject();
		
		obj.put("ontology-uri", "http://localhost:8890/DAV/home/exUser/rdf_sink/vocabulary/common.owl");
		
		JSONArray triples = new JSONArray();
		
		while ( res.hasNext() ) {
			
			QuerySolution solution = res.nextSolution();
			
			JSONObject one_triple = new JSONObject();
			
			one_triple.put("s", solution.get("s").toString());
			one_triple.put("p", solution.get("p").toString());
			one_triple.put("o", solution.get("o").toString());
			
			triples.add( one_triple );
		}
		
		obj.put("triples", triples);
		
		FileWriter writer = null;
		
		try {
			writer = new FileWriter("common.json");
			
			writer.write(obj.toJSONString());
			System.out.println("Successfully Copied JSON Object to File....");
			System.out.println("\nJSON Object: " + obj);
			
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}

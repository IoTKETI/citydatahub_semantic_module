package com.semantic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.json.simple.JSONObject;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class InfoEntryMain {
	
	InterfaceConfiguration IC = new InterfaceConfiguration();
	
	static VirtGraph ts_access;
	static EntryInterface EI = new EntryInterface();
	static EntrySet<String, String, String> finalOntology;
	static TripleParser tp;
	String entryIndex = "";
	
	InfoEntryMain() {
		
		/*			STEP 1 : Triple-Store Credentials		url --> "jdbc:virtuoso://localhost:1111"*/
		ts_access = new VirtGraph (IC.TDB_URL, IC.TDB_USER_NAME, IC.TDB_USER_PASS);
	}
	
	
	void initialize_Entry_Data(JSONObject inputEntrySet) {
		
		//EI = new EntryInterface();
			
		EI.initializeEntry(inputEntrySet);
		
		//System.out.println("\nConsole LOG--> In InfoEntryMain: IE.count: \n" + EI.count(EI.getEntryLabel()));
	}
	
	void set_Current_Entry_Values(String input_selectedEntryLabel, String in_entryIndex) {
		
		EI.setEntryLabel(input_selectedEntryLabel);
		
		System.out.println("\nConsole LOG--> In InfoEntryMain: EntryLabel: \n" + input_selectedEntryLabel);
		
		setEntryIndex( in_entryIndex );
		
		if(entryIndex.equals("0")) {
		
			EI.setSearchingAllClasses(true);
		
		}else {
		
			EI.setSearchingAllClasses(false);
		}
		
		
	}
	
	LinkedList<String> explore_TripleStore_Graphs(){
		
		LinkedList<String> fetched_graph_list = null;
		
		QueryGenerator qGen = new QueryGenerator();
		
		String graph_search_query = qGen.get_Graph_Search_Query();
		
		Query sparql = QueryFactory.create( graph_search_query );
		
		/*			STEP 4			*/
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, ts_access);
		
		ResultSet results = vqe.execSelect();
		
		TripleParser graph_tp = new TripleParser(results);
		
		fetched_graph_list = graph_tp.getParsedGraphList();
		
		return fetched_graph_list;
	}
	
	LinkedList<String> explore_TDB_CLasses(LinkedList<String> graphList) {
		
		System.out.println("\nConsole LOG--> In explore_ParkingLot_Entry_CLasses():  ESet.count: " + EI.count(EI.getEntryLabel()));
		
		LinkedList<String> fetched_class_list = null;
		
		QueryGenerator qGen = new QueryGenerator();
		
		String class_hierarchy_query = "";
			
		class_hierarchy_query = qGen.get_Class_Hierarchy_Query( EI, graphList );
	
		
		Query sparql = QueryFactory.create( class_hierarchy_query );

		/*			STEP 4			*/
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, ts_access);
		
		ResultSet results = vqe.execSelect();
		//OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, vqe.execConstruct() ); 
		
		tp = new TripleParser();
		
		//After this function, you can get CLassList through the fucntion getClassList() 
		tp.parseClassHierarchy(results);
		
		fetched_class_list = tp.getClassList();
		
//		ResultFormatter.formatMyOutput(results);
		
		return fetched_class_list;
		
//		//String property_query = qGen.get_Object_Property_Query(classList);
//		String property_query = qGen.get_Data_Property_Query(classList);
//		
//		sparql = QueryFactory.create( property_query );
//		
//		vqe = VirtuosoQueryExecutionFactory.create (sparql, ts_access);
//		
//		results = vqe.execSelect();
		
//		EntrySet<String, String, String> relations = tp.parseRelations(results);
//		
//		classList = tp.provideURIForClasses(EI);
//		
//		createTemplate(classList, relations);
//			
//			displayEntryOntology();
		
//		ResultFormatter.formatMyOutput(results);
//		
//		explore_ParkingLot_Entry_CLasses( customEI );
	}
	
	static EntrySet<String, String, String>  explore_ObjectProperties(LinkedList<String> classList, LinkedList<String> graph_list) {
		
		QueryGenerator qGen = new QueryGenerator();
		
		String property_query = qGen.get_Object_Property_Query( classList, graph_list );
//		String property_query = qGen.get_Data_Property_Query(classList);
		
		Query sparql = QueryFactory.create( property_query );
		
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, ts_access);
		
		ResultSet results = vqe.execSelect();
		
		EntrySet<String, String, String> relations = tp.parseRelations(results);
		
		//ResultFormatter.formatMyOutput(results);
//		
//		classList = tp.provideURIForClasses(EI);
//		
//		createTemplate(classList, relations);
//			
//		displayEntryOntology();
		//return null;
		return relations;
	}
	
	static EntrySet<String, String, String>  explore_DataProperties(LinkedList<String> graphList){
		
		QueryGenerator qGen = new QueryGenerator();
		
		String data_property_query = qGen.get_Data_Property_Query( tp.getClassList(), graphList );
		
		Query sparql = QueryFactory.create( data_property_query );
		
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, ts_access);
		
		ResultSet results = vqe.execSelect();
		
		EntrySet<String, String, String> relations = tp.parseRelations(results);
		
		//ResultFormatter.formatMyOutput(results);
		
		return relations;
		//return null;
	}
	
	
	EntrySet<String, Integer, String> getClassHierarchy(){
	
		return tp.getParsedClassHierarchy();
	}
	
	
	void initializeClassHierarchy(EntrySet<String, Integer, String> cH) {
		
		tp.setParsedClass(cH);
	}
	
	void initializeClassesForThisConxext(LinkedList<String> cL) {
		
		tp.populateClassList(cL);
	}
	
	LinkedList<String> getClassListForThisContext(){
		
		return tp.getClassList();
	}
	
	LinkedList<String> getSubCLassesOf(String classURI, LinkedList<String> fetched_List) {
		
		return tp.getSubClasses(classURI, fetched_List);
	}

	static void displayEntryOntology() {
		
		System.out.println("----------------------------------------- Final Generated Ontology for Entry -----------------------------------------");
		
		Iterator f_itor = finalOntology.getIterator();
		
		while(f_itor.hasNext()) {
		
			Map.Entry pair = (Map.Entry) f_itor.next();
			
			String s = pair.getKey().toString();
			
			System.out.println("Subject Class: " + s );
			
			Map<String, String> sub_map = (Map<String, String>) pair.getValue();
			
			Iterator sub_itor = sub_map.entrySet().iterator();
			
			while(sub_itor.hasNext()) {
				
				Map.Entry sub_pair = (Map.Entry) sub_itor.next();
				
				System.out.println("	Property: " + sub_pair.getKey() + "	" + "Object Class: " + sub_pair.getValue() );
			}
			System.out.println();
		}
		
		
	}
	
	
	ArrayList<String> getEntryIndividuals(String in_entryLabel){
		
		System.out.println("\nConsole LOG--> In InfoEntryMain: getEntryIndividuals(): in_entryLabel: " + in_entryLabel);
		
		return EI.getEntryIndividuals(in_entryLabel);
	}
	
	String getEntryIndex() {
		
		return entryIndex;
	}
	
	void setEntryIndex(String in_Index) {
		
		entryIndex = in_Index;
	}

	void setEntryLabel(String in_Label) {
		
		EI.setEntryLabel(in_Label);;
	}
}

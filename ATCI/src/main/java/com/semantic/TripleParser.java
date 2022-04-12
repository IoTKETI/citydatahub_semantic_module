package com.semantic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

public class TripleParser {

	private EntrySet<String, Integer, String> parsedClass;
	LinkedList<String> classList, graphList;
	
	private EntrySet<String, LinkedList<String>, LinkedList<String>> union_Relation = null;
	private EntrySet<String, Integer, String> next_To_parse;
	
	public TripleParser() {
		
		parsedClass = new EntrySet<String, Integer, String>();
	}
	
	
	public TripleParser(ResultSet res) {
		
		graphList = new LinkedList<String>();
		
		while( res.hasNext() ) {
			
			QuerySolution solution = res.nextSolution();
			
			graphList.add(  solution.get("graph").toString()  );
		}
	}
	
	
	void parseClassHierarchy(ResultSet res) { 
		
		 int cpc_index = 0, cp_index=0, count = 0;
		 String ontology_uri = "";
		 classList = new LinkedList<String>();
		 EntrySet<String, Integer, String> currentParentClasses = new EntrySet<String, Integer, String>();
		 
		while ( res.hasNext() ) {
			
			QuerySolution solution = res.nextSolution();
			
			String parent;
			
			try {
				parent = solution.get("o").toString();
				
			}catch(NullPointerException e) {
				
				parent = "n/a";
			}
			
			String child  = solution.get("s").toString();
			
			//int level = Integer.parseInt( solution.get("count").toString() );
			
			
			cpc_index = currentParentClasses.count(child);
			if( cpc_index > 0 ) {  //--> this means already a higher level parent class is parsed, and is present in the list.
				
				for(int i=0; i<cpc_index; i++) {
					
					String higher_parent = currentParentClasses.get(child, i);
					
					cp_index = parsedClass.count( higher_parent );
					
					if( !parsedClass.containsValue( higher_parent, parent ) ) {
					
						parsedClass.put( higher_parent, cp_index, parent );
					}
				}
			}
			currentParentClasses.put( child, cpc_index, parent );
			
			
//			if( index < (level-1) ) {
//				
//				index = level-1;
//			}
			cp_index = parsedClass.count(parent);
			
			if( !parsedClass.containsValue( parent, child ) ) {
				
				parsedClass.put( parent, cp_index, child );
			}
			
			
			//Prepare List to search Corresponding Object Properties:
			if( !classList.contains(parent) && !parent.equals("n/a") ) {
				classList.add(parent);
			}
			if( !classList.contains(child) ) {
				classList.add(child);
			}
		}
		
		displayParsedData();
	}
	
	EntrySet<String, String, String> parseRelations(ResultSet res) {
		
		EntrySet<String, String, String> parsedRelation = new EntrySet<String, String, String>();
		
		while ( res.hasNext() ) {
			
			QuerySolution solution = res.nextSolution();
			
			String domain = null, range = null;
			
			
			try {
				domain = solution.get("s").toString();
				
			}catch(NullPointerException e) {
				
				domain = "n/a";
			}
			
			try {
				range = solution.get("o").toString();
				
			}catch(NullPointerException e) {
				
				range = "n/a";
			}
			
			String property = solution.get("p").toString();			
			
			if( parsedRelation.containsKeys(property) ) { //--> Means that property has union domain/range
				
				if( parsedRelation.containsKeys(property, domain) ) { //--> Means domain is already there.
					
					domain = String.valueOf( parsedRelation.get(property).size() - 1 );
				}
				
				if( parsedRelation.containsValue(property, range) ) { //--> Means range is already there.
					
					range = String.valueOf( parsedRelation.get(property).size() - 1 );
				}
				
			}
			
			parsedRelation.put(property, domain, range);
			
			System.out.println("\nLOG: Relation inserted:");
			System.out.println("Property: " + property + ", Domain: " + domain + ", Range: " + range);
			
			System.out.println("\nLOG: Number of Parsed Domain/Ranges: " + parsedRelation.get(property).size());
		}
		
		return parsedRelation;
	}
	
	
	
	LinkedList<String> provideURIForClasses(EntryInterface ei_set){
		
		LinkedList<String> classURISet = new LinkedList<String>();
		
		String entry_label = ei_set.getEntryLabel();
		
		
		for(int i=0; i < ei_set.count(entry_label); i++) {
			
			String class_to_check = "#" + ei_set.getEntry(entry_label, i);
			
			boolean found=false;
			
			for(int j=0; (j<classList.size()) && (!found); j++ ) {
				
				String check = classList.get(j);
				
				if( check.endsWith(class_to_check) ) {

					classURISet.add( check );
					found = true;
				}
			}
		}
		
		return classURISet;
	}
	
	EntrySet<String, Integer, String> getParsedClassHierarchy(){
		
		return parsedClass;
	}
	
	
	LinkedList<String> getParsedGraphList() {
		
		return graphList;
	}
	
	
	LinkedList<String> getSubClasses(String super_class, LinkedList<String> subClasses) {
		
		if( super_class.equals("n/a") ) {
			
			subClasses = classList;
			
		}else if(super_class.startsWith("http")){		//--> Means value is URI, not a number
		
			if(!subClasses.contains(super_class)) {		subClasses.add(super_class);	}
			
			Map<Integer, String> temp = parsedClass.get(super_class);
			
			if(temp!=null) {
			
				Iterator classItor = temp.entrySet().iterator();
				
				while(classItor.hasNext()) {
					
					String subClass = ( (Map.Entry) classItor.next() ).getValue().toString();
					
					if( !subClasses.contains(subClass) ) {		subClasses.add(subClass);		}
				}
			}
		}
		
		return subClasses;
	}
	
	
	
	void populateClassList(LinkedList<String> in_List) {
		
		classList = in_List;
	}
	
	LinkedList<String> getClassList(){
		
		return classList;
	}
	
	void setParsedClass(EntrySet<String, Integer, String> pC) {
		
		parsedClass = pC;
	}
	
	
	private void displayParsedData() {
		// TODO Auto-generated method stub
		
		try {
			
			Iterator classItor = parsedClass.getIterator();
			
			while( classItor.hasNext() ) {
				
				//if( parsedClass.containsKeys() ) {
				
					Map.Entry classEntryPair = (Map.Entry) classItor.next();
					
					System.out.println("\n    Parent Class: " + classEntryPair.getKey() + " : ");
					
					Map<Integer, String> childSet = (Map<Integer, String>) classEntryPair.getValue();
					
					Iterator childItor = childSet.entrySet().iterator();
					
					while (childItor.hasNext()) {
						
				        Map.Entry childEntryPair = (Map.Entry) childItor.next();
				        
				        System.out.println(" 		sub-class:" + childEntryPair.getKey() + " : " + childEntryPair.getValue() );
				    }
				//}
		}
			
		}catch(NullPointerException e) {
			System.out.print("Console LOG-->   -------End of List-------");
		}
	}

	
	
	//--> To Display the union Domain/Range list on console..
	void show_Union_List(LinkedList<String> unionList) {
		
		Iterator itor = unionList.iterator();
		
		while(itor.hasNext()) {
			
			System.out.println( "Union List item: " + itor.next().toString() );
		}
	}

	
	
	private String parse_URI_prefix(RDFNode rdfNode) {
		// TODO Auto-generated method stub
		
		String uri = rdfNode.toString();
		
		return uri.substring( 0, uri.indexOf('#') ) ;
	}
	
	private String parse_class_from_URI(String uri) {
		// TODO Auto-generated method stub
		
		return uri.substring( uri.indexOf('#') ) ;
	}


	private boolean isOWLClass(QuerySolution qs) {
		// TODO Auto-generated method stub
		
		return (
				 qs.get("p").toString().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
				 && 
				 qs.get("o").toString().equals("http://www.w3.org/2002/07/owl#Class")
				 );
	}
	
	
	private boolean isSuperClass(QuerySolution qs) {
		// TODO Auto-generated method stub
		
		
		return (
				 qs.get("p").toString().equals("http://www.w3.org/2000/01/rdf-schema#subClassOf")
				 &&
				 !qs.get("o").toString().startsWith("nodeID://")
				);
	}
	
	
	private boolean isBlank(QuerySolution qs) {
		// TODO Auto-generated method stub
		
		return (
				 qs.get("p").toString().equals("http://www.w3.org/2000/01/rdf-schema#subClassOf")
				 &&
				 qs.get("o").toString().startsWith("nodeID://")
				);
	}
	
	// Function NOT USED !!!
	private boolean isRestriction(QuerySolution qs) {
		// TODO Auto-generated method stub
	
		return (
				 qs.get("p").toString().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
				 &&
				 qs.get("o").toString().equals("http://www.w3.org/2002/07/owl#Restriction")
				);
	}
	
	private boolean isRelation(QuerySolution qs) {
		// TODO Auto-generated method stub
		
		return (
				 qs.get("s").toString().startsWith("nodeID://")
				 &&
				 qs.get("p").toString().equals("http://www.w3.org/2002/07/owl#onProperty")
				);
	}
	
	private boolean isReflexive(String subject, String object) {
		// TODO Auto-generated method stub
		
		return subject.equals( object );
	}
	
	private boolean isRange(QuerySolution qs) {
		// TODO Auto-generated method stub
		
		return (
				 qs.get("s").toString().startsWith("nodeID://")
				 &&
				 (
				   qs.get("p").toString().startsWith("http://www.w3.org/2002/07/owl#")
				   && (
						 qs.get("p").toString().startsWith("allValuesFrom", 30)
						 ||
						 qs.get("p").toString().startsWith("someValuesFrom", 30)
					   )
				   )
				 
				);
	}
}

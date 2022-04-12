package com.semantic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.jena.query.QuerySolution;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TemplateFileStore {

	LinkedList<String[]> ObjectProperties, DataProperties;
	LinkedList<String> IndividualList, classList, typeList;
	LinkedList<String> unusedClass;
	String entryIndex;
	
	/*
	"Parking Lot", "Parking Spot", "Parking Service", "Parking Availability Evaluation Service",
	"Parking Congestion Estimation Service", "Parking Service Provider", "Parking Spot Availability",
	"Parking Congestion Estimation"
	*/
	
	TemplateFileStore( String index, 
						LinkedList<String> inIndividual,
						LinkedList<String> inClass,
						LinkedList<String[]> inObj, 
						LinkedList<String[]> inData ){
	
		entryIndex = index;
		IndividualList = inIndividual;
		classList = inClass;
		ObjectProperties = inObj;
		DataProperties = inData;
		
		System.out.println(IndividualList);
		
		if(ObjectProperties==null) {	ObjectProperties = new LinkedList<String[]>();	}
		if( DataProperties==null ) {	DataProperties = new LinkedList<String[]>();	}
		
		unusedClass = new LinkedList<String>();
		typeList = new LinkedList<String>();
	}
	
	
	//--> Check the data to identify any classes that are not used
	//--> in any of the properties....
	public boolean unusedClasses() {
		
		for(int i=0; i<IndividualList.size(); i++) {
			
			// checkClass is actually holding an Individual Value.
			String checkIndividual = IndividualList.get(i);
			
			boolean found = false;
			
			for(int j=0, k=0; (j<ObjectProperties.size() || k<DataProperties.size()) && !found; j++, k++) {
				
				if(j<ObjectProperties.size()) {
					
					if( checkIndividual.equals(ObjectProperties.get(j)[0]) || checkIndividual.equals(ObjectProperties.get(j)[2]) ) {
						found = true;
					}
				}
				
				if(k<DataProperties.size()) {
					
					if( checkIndividual.equals(DataProperties.get(k)[0]) ) {
						found = true;
					}
				}
				
			}
			
			if(!found) {
				unusedClass.add(checkIndividual);
			}
		}
		
		return !unusedClass.isEmpty();
	}

	
	
	public void saveTemplateInJSONFile(String savePath) {
		// TODO Auto-generated method stub
		
		String entryName = "N/A";
			 
		JSONObject obj = new JSONObject();
		
		//Provide Entry Name........
		obj.put( "entry-name", entryName );
		
		// Put Classes........
		JSONArray individuals = new JSONArray();
		JSONArray types = new JSONArray();
		
		for(int i=0; i<IndividualList.size(); i++) {
			
			String the_uri = IndividualList.get(i);
			
			boolean typeFound = false;
			String typeClass = null;
			for(int iter = 0; (iter<classList.size()) || !typeFound; iter++) {
				
				if( the_uri.contains(classList.get(iter)) ) { //--> It means that this (checkClass) Individual is made from 
															 //--->this class in the classList!!!
					typeFound = true;
					typeClass = classList.get(iter);
				}
			}
			
			//int uri_type = contains_URI(individuals, the_uri);
			
			/*if( uri_type == 2 ) {
			
				String prev_uri = individuals.get(individuals.size()-1).toString();
				
				int indvl_offset = Integer.parseInt( prev_uri.substring( prev_uri.lastIndexOf('_') + 1 ) );
	
				indvl_offset++;
				
				individuals.add( the_uri + "_" + indvl_offset );
					
			}else if( uri_type == 1 ) {
				
				individuals.set( individuals.size()-1, the_uri + "_1");
				
				individuals.add(the_uri + "_2");
			}else {*/
			if( !unusedClass.contains(the_uri) ){
				
				individuals.add( the_uri );
				types.add( typeClass );
			}
			
			//types.add( the_uri );
		}
		
		obj.put( "type-list", types );
		obj.put( "individual-list", individuals );
		
		// Put Object Properties........
		JSONArray triples = new JSONArray();
		
		for(int j=0; j<ObjectProperties.size(); j++) {
			
			JSONObject one_triple = new JSONObject();
			
			one_triple.put("domain", ObjectProperties.get(j)[0] );
			one_triple.put("property", ObjectProperties.get(j)[1] );
			one_triple.put("range", ObjectProperties.get(j)[2] );
			
			triples.add( one_triple );
		}
		
		obj.put("object-properties", triples);
		
		// Put Data Properties........
		triples = new JSONArray();
		
		for(int k=0; k<DataProperties.size(); k++) {
			
			JSONObject one_triple = new JSONObject();
			
			one_triple.put("domain", DataProperties.get(k)[0] );
			one_triple.put("property", DataProperties.get(k)[1] );
			one_triple.put("range", DataProperties.get(k)[2] );
			
			triples.add( one_triple );
		}
		
		obj.put("data-properties", triples);
		
		FileWriter writer = null;
		
		try {
			writer = new FileWriter(savePath);
			
			writer.write( prettyJSONString(obj) );
			System.out.println("Successfully Copied JSON Object to File....");
			System.out.println("\nJSON Object: " + obj);
			
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	void saveEntryInJSONFile(String entryName, LinkedList<String> entries) {
		
		JSONObject obj = new JSONObject();
		
		JSONArray entryList = new JSONArray();
		
		entryList.addAll(entries);
		
		obj.put(entryName, entryList);
		
		FileWriter writer = null;
		
		try {
			writer = new FileWriter( System.getProperty("user.dir") + "\\EntryStore\\entries.json" );
			
			//writer.write( prettyJSONString(obj) );
			System.out.println("Successfully Copied JSON Object to File....");
			//System.out.println("\nJSON Object: " + obj);
			
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	boolean uriIsNumbered(String input_uri) {
		
		boolean is_numbered = false;
		
		if( input_uri.lastIndexOf('_') > (input_uri.length()-5)) {
			is_numbered = true;
		}
		
		return is_numbered;
	}
	
	
	// Returns 0, 1 or 2
	int contains_URI(JSONArray uri_array, String uri) {
		
		int contains = 0;
		
		for(int i=0; i<uri_array.size() && (contains==0); i++){
			
			String check_uri = uri_array.get(i).toString();
			
			if( check_uri.equals(uri) ) {
				
				contains = 1;
				
			}//else if( check_uri.startsWith(uri+"_") ) {
				
			//	contains = 2;
			//}
		}
		
		return contains;
	}
	
	String prettyJSONString(JSONObject uglyJSON) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		
		return gson.toJson( uglyJSON );
		
	}
	
}

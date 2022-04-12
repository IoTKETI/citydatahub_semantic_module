package com.cityhub.semantic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapListStructure {

	Map<String,ArrayList<String>> mapList;
	
	MapListStructure(){
		
		mapList = new  HashMap<String,ArrayList<String>>();
	}
	
	
	  ////////////////////////////////////
	 //-->		Key , Empty List 
	////////////////////////////////////
	void put(String key) throws RedundantKeyException {
		
		if( !mapList.containsKey(key) ) {
			
			mapList.put( key , new ArrayList<String>() );
			
		}else {
			
			ArrayList<String> list_from_key = mapList.get(key);
			
			if( !list_from_key.isEmpty() ) {
				
				throw new RedundantKeyException("The input key already contains a non-empty list. "
						  + "Therefore, cannot override this key with an empty list.");	
			}
		}
	}
	
	  ////////////////////////////////////////////
	 //-->		Key , Add one value in the List 
	////////////////////////////////////////////	
	void put(String key, String one_value) {
		
		if( mapList.containsKey(key) ) {
			
			ArrayList<String> list_from_key = mapList.get(key);
			
			if( !list_from_key.contains(one_value) ) {
				
				list_from_key.add(one_value);
				
				mapList.put(key, list_from_key);
			}
		
		}else {
			
			mapList.put(key, new ArrayList<String>(Arrays.asList(one_value)) );
		}
		
	}
	
	
	
	  ////////////////////////////////////////////
	 //-->Checks whether list contains this key  
	////////////////////////////////////////////
	boolean containsKey(String in_key) {
		
		return mapList.containsKey(in_key);
	}
	
	
	
	  ////////////////////////////////////////////
	 //-->Return Iterator to traverse the list 
	////////////////////////////////////////////
	Iterator getIterator() {
		
		return mapList.entrySet().iterator();
	}
	
	
	
	
	
	  ////////////////////////////////////
	 //-->Class for Exception Handling
	////////////////////////////////////
	public class RedundantKeyException extends Exception{
		
		String exception_Message;
		
		public RedundantKeyException(String message) {
		
			exception_Message = message;
		}
		
		public String toString() {
			
			return("Redundant Key Exception! : " + exception_Message);
		}
	}
}

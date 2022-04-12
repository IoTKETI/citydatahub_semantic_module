package com.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class EntryInterface {

	private static EntrySet<String, Integer, String> ESet;
	private static EntrySet<String, Integer, String> IndividualSet; 
	private String selected_entry_Label = "";
	boolean search_All_Classes = false;
	
	public EntryInterface() {
	
		ESet = new EntrySet<String, Integer, String>();
		IndividualSet = new EntrySet<String, Integer, String>();
	}
	
	public void initializeEntry(JSONObject input_set) {
		
		//System.out.println("\nConsole LOG--> Status: Initializing Entry Set with Parking Lot Entry Data .....");
		
		if(input_set != null) {
			
			Iterator itor_Entries = input_set.keySet().iterator();
			
			while(itor_Entries.hasNext()) { //----> Iterating Entries
				
				String entry_Label = itor_Entries.next().toString();
				
				JSONObject json_entry_classes = (JSONObject) input_set.get( entry_Label );
				
				Iterator itor_Classes = json_entry_classes.keySet().iterator();
				
				int class_offset = 0, individual_offset = 0;
				
				while(itor_Classes.hasNext()) { //---> Iternating Classes for each Entries.
					
					String one_class = (String) itor_Classes.next();
					
					//System.out.println("\nConsole LOG--> " + one_class);
					
					ESet.put(entry_Label, class_offset, one_class);
					//System.out.println("\nConsole LOG--> In EntryInterface: count(" + entry_Label + "): \n" + count(entry_Label));
					
					JSONArray jsonArr_individuals = (JSONArray) json_entry_classes.get(one_class);
					
					for(int i=0; i<jsonArr_individuals.size(); individual_offset++, i++) {
						
						System.out.println("\nConsole LOG--> In EntryInterface: initializeEntry: \n" + jsonArr_individuals.get(i).toString() );
						
						IndividualSet.put(entry_Label, individual_offset, jsonArr_individuals.get(i).toString());
					}
					
					class_offset++;
				}
			}
		}
		
		//System.out.println("\nConsole LOG--> In EntryInterface: count: " + count(getEntryLabel()));
		
//		if(entry_Label.equals("ParkingLot_Entry")) {
//		
//			ESet.put(entry_Label, 0, "ParkingLot");
//			ESet.put(entry_Label, 1, "ParkingLotProfile");
//			ESet.put(entry_Label, 2, "ContactPoint");
//			ESet.put(entry_Label, 3, "ProfilePicture");
//			ESet.put(entry_Label, 4, "GeoProperty");
//			ESet.put(entry_Label, 5, "ZoneBasedLocation");
//			ESet.put(entry_Label, 6, "TemporalEntity");
//			ESet.put(entry_Label, 7, "Instant");
//			ESet.put(entry_Label, 8, "GeneralDateTimeDescription");
//			ESet.put(entry_Label, 9, "DayOfWeek");
//			ESet.put(entry_Label, 10, "Evaluation");
//		}
	}
	
//	public void customEntry(EntrySet<String, Integer, String> input_Set) {
//		
//		System.out.println("\nConsole LOG--> Status: Initializing Entry Set with Customized Entry Data .....");
//		
//		//ESet = input_Set;
//	}
	
//	public void displayEntryData(String Entry_Label) {
//		
//		Map<Integer, String> entry_data = ESet.get(Entry_Label);
//		
//		Iterator itor = entry_data.entrySet().iterator();
//		
//		while(itor.hasNext()) {
//			
//			Map.Entry pair = (Map.Entry) itor.next();
//	        System.out.println(pair.getKey() + " = " + pair.getValue());
//		}
//	}

	  //////////////
	 //-->Getters
	//////////////
	
	public ArrayList<String> getEntryIndividuals(String entryLabel){
		
		ArrayList<String> individuals = new ArrayList();
		
		if(IndividualSet.containsKeys(entryLabel)) {
			
			Map<Integer,String> map = IndividualSet.get(entryLabel);
			
			for(int i=0; i<map.size(); i++) {
				
				individuals.add(map.get(i));
			}
		}
		
		return individuals;
	}
	
	public Map<Integer, String> getEntryData(String Entry_Label) {
		
		return ESet.get(Entry_Label);
	}
	
	public String getEntry(String Entry_Label, int offset) {
		
		return ESet.get(Entry_Label, offset);
	}
	
	public String getEntryLabel() {
		
		return selected_entry_Label;
	}
	
	public boolean searchAllClasses() {
		
		return search_All_Classes;
	}
	
	
	  //////////////
	 //-->Setters
	//////////////
	
	public void setEntryLabel(String in_entryLabel) {
		
		selected_entry_Label = in_entryLabel;
	}
	
	public void setSearchingAllClasses(boolean in_searchAll) {
		
		search_All_Classes = in_searchAll;
	}
	
	
	
	public int count(String Entry_Label) {
		
		return ESet.count(Entry_Label);
	}
}

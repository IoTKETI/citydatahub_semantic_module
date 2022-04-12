package semantics.cityhub.keti.utils;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVHandler {

	List<ArrayList<String>> dataset;
	
	public CSVHandler(){
		
		dataset = new ArrayList<ArrayList<String>>();
	}
	
	
	public void writeDatasetToCSV(Writer writer){
		
		try {
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
			
			csvPrinter.printRecords(dataset);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	public void addRow(String[] row) {
		
		dataset.add( (ArrayList<String>) Arrays.asList(row) );
	}
	
	
	public void addVariables(ArrayList<String> variables) {
		
		ArrayList<String> row_list;
		
		if(dataset.isEmpty()) { //--> Add Variables for the first time.
			
			row_list = new ArrayList<String>();
			
			dataset.add( variables );
			
		}else { //------------------> Add Variables in existing list of Variables.
		
			row_list = dataset.get( 0 );
			
			for(int i=0; i<variables.size(); i++) {
				
				if( !row_list.contains( variables.get(i) ) ) {
				
					row_list.add( variables.get(i) );
				}
			}
			
			dataset.set( 0 , row_list);
		}
	}
	
	
	public void addValues(Map<String,String> value_Map) {
		
		ArrayList<String> variable_list = dataset.get( 0 );
		
		ArrayList<String> value_list= new ArrayList<>(  Arrays.asList(new String[variable_list.size()]) );
		Collections.fill(value_list, "NULL");
		
		Iterator<Entry<String,String>> itor = value_Map.entrySet().iterator();
		
		Map<String, String> unexpectedVars = new HashMap<String,String>();
		
		while(itor.hasNext()) {
			
			Entry<String,String> value_Entry = itor.next();
			
			if( variable_list.contains( value_Entry.getKey() ) ) { //-->Variable Already Exist.	
				
				value_list.set( variable_list.indexOf(value_Entry.getKey()), value_Entry.getValue() );
				
			}else { //------------------------------------------------->New Unexpected Variable. Sort Dataset!
				
				unexpectedVars.put(
								value_Entry.getKey().toString(), 
								value_Entry.getValue().toString()
								);
			}
			
		}
		
		if(!unexpectedVars.isEmpty()) { //
			
			sortDataSetForNewEntry( value_list, unexpectedVars );
		
		}else {//-->Add values with no new variables
		
			dataset.add( value_list );
		}
	}


	
	private void sortDataSetForNewEntry(ArrayList<String> value_list, Map<String,String> value_map) {
		// TODO Auto-generated method stub
		
		int columnCount = 0;
		
		ArrayList<String> variable_list = dataset.get( 0 );
		
		Iterator<Entry<String,String>> itor = value_map.entrySet().iterator();
		
		while(itor.hasNext()) {
			
			Entry<String,String> value_entry = itor.next();
			
			 variable_list.add( value_entry.getKey() );
			 value_list.add( value_entry.getValue() );
			 columnCount++;
		}
		
		dataset.set(0, variable_list);
		
		
		 //-->Now Adding NULL values in the previous rows of the dataset
		//--->based on columnCount
		
		for(int row=1; row<dataset.size(); row++) {
			
			ArrayList<String> one_row = dataset.get(row);
			
			for(int column=0; column<columnCount; column++) {
				
				one_row.add("NULL");
			}
			
			dataset.set(row, one_row);
		}
	}
	
}

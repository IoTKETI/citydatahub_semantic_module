package com.cityhub.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

public class PrefixRepository {

	private List<String> prefixes = new ArrayList<String>();
	private List<String> namespaces = new ArrayList<String>();
	private List<Boolean> visited = new ArrayList<Boolean>();
	private List<Boolean> ont_visited = new ArrayList<Boolean>();
	
	PrefixRepository(){
		
		// Standard Prefixes
		prefixes.add("rdf"); 			namespaces.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#");				visited.add(false);
		prefixes.add("rdfs"); 			namespaces.add("http://www.w3.org/2000/01/rdf-schema#");					visited.add(false);
		prefixes.add("owl"); 			namespaces.add("http://www.w3.org/2002/07/owl#");							visited.add(false);
		prefixes.add("xsd"); 			namespaces.add("http://www.w3.org/2001/XMLSchema#");						visited.add(false);
		prefixes.add("xml"); 			namespaces.add("http://www.w3.org/XML/1998/namespace#");					visited.add(false);
		
		prefixes.add("common"); 		namespaces.add("http://www.city-hub.kr/ontologies/2019/1/common#");         visited.add(false);		ont_visited.add(false);
		prefixes.add("parking"); 		namespaces.add("http://www.city-hub.kr/ontologies/2019/1/parking#");		visited.add(false);		ont_visited.add(false);
		prefixes.add("weather"); 		namespaces.add("http://www.city-hub.kr/ontologies/2019/1/weather#");		visited.add(false);		ont_visited.add(false);
		prefixes.add("air-quality"); 	namespaces.add("http://www.city-hub.kr/ontologies/2019/1/air-quality#");	visited.add(false);		ont_visited.add(false);
		prefixes.add("time"); 			namespaces.add("http://www.w3.org/2006/time#");								visited.add(false);		ont_visited.add(false);
		prefixes.add("saref"); 			namespaces.add("https://w3id.org/saref#");									visited.add(false);		ont_visited.add(false);
	}


	String[] getPrefixEnabledURI(String input_uri) {
		
	//--> prefixURI_Set[] : { prefix, namespace, prefix:namespace OR completeURI }
		String prefixURI_Set[] = { null, null, null};
		
		//System.out.println("URI retrived: " + input_uri);
		
		String uri_parts[] = input_uri.split("#");
		
		int index = namespaces.indexOf( uri_parts[0] + "#" );
		
		if( index != -1 ) {//-------> The Prefix is found
			
			//-------> The Prefix is new, not added before
			if(!visited.get(index)) {
				
				prefixURI_Set[0] = prefixes.get(index);
				
				prefixURI_Set[1] = namespaces.get(index);
				visited.set(index, true);
			}
			
			if(uri_parts.length>1) {
				
				prefixURI_Set[2] = prefixes.get(index) + ":" + uri_parts[1];
			
			}else {
				
				prefixURI_Set[2] = input_uri;
			}
		
		//-------> No Prefix is matched, entire uri should be added as it is
		}else {
			
			boolean namespace_found = false;
			for(int i=5; i<namespaces.size() && !namespace_found; i++) {
				
				if(!ont_visited.get(i-5)) { //Means it is not previously visited. As ontology.
				
					String ont_check_uri = namespaces.get(i).split("#")[0];
					
					if( ont_check_uri.equals( uri_parts[0] ) ) {
						
						prefixURI_Set[0] = prefixes.get(i);
						prefixURI_Set[1] = ont_check_uri;
						
						namespace_found = true;
						ont_visited.set(i-5, true);
					}
				}
			}
			
			prefixURI_Set[2] = input_uri;
		}
		
		return prefixURI_Set;
	}
	
	
	String[] getPrefixOf(String name_space) {
		
		String p[] = { null, null };
		
		int index = namespaces.indexOf( name_space + "#" );
		
		if( index != -1 ) {
			
			p[0] = prefixes.get(index);
			p[1] = namespaces.get(index);
		}
		
		return p;
	}
	
	String[] getNamespaceOf(String prefix) {
		
		String p[] = { null, null };
		
		int index = prefixes.indexOf(prefix);
		
		if(index != -1) {
			
			p[0] = prefixes.get(index);
			p[1] = namespaces.get(index);
		}
		
		return p;
	}
	
	
}

package com.semantic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class PrefixSet {

	private Map<String, String> prefixSet = new HashMap<String, String>();
	
	PrefixSet(){
		
		prefixSet.put(	"virtuoso_rep_uri", 	"http://localhost:8890/DAV/home/exUser/rdf_sink");
		
		prefixSet.put(	"ontology_common", 		"http://www.city-hub.kr/ontologies/2019/1/common#");
		
		prefixSet.put(	"ontology_parking", 	"http://www.city-hub.kr/ontologies/2019/1/parking#");
		
		prefixSet.put(	"ontology_weather", 	"http://www.city-hub.kr/ontologies/2019/1/weather#");
		
		prefixSet.put(	"ontology_air-quality", "http://www.city-hub.kr/ontologies/2019/1/air-quality#");
		
		prefixSet.put(	"ontology_time", 		"http://www.w3.org/2006/time#");
		
		prefixSet.put(	"ontology_saref", 		"https://w3id.org/saref#");
		
		prefixSet.put(	"ontology_saref4city", 	"https://w3id.org/def/saref4city#");
	}
	
	public LinkedList<String> getAllDataValueTypes(){
		
		LinkedList<String> dataTypes = new LinkedList<String>();
		
		dataTypes.add("http://www.w3.org/2002/07/owl#rational");
		dataTypes.add("http://www.w3.org/2002/07/owl#real");
		
		dataTypes.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#PlainLiteral");
		dataTypes.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral");
		
		dataTypes.add("http://www.w3.org/2000/01/rdf-schema#Literal");
		
		dataTypes.add("http://www.w3.org/2001/XMLSchema#anyURI");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#base64Binary");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#boolean");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#byte");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#dateTime");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#dateTimeStamp");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#decimal");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#double");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#float");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#hexBinary");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#int");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#integer");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#language");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#long");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#Name");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#NCName");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#negativeInteger");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#NMTOKEN");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#nonPositiveInteger");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#normalizedString");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#positiveInteger");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#short");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#string");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#token");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#unsignedByte");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#unsignedInt");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#unsignedLong");
		dataTypes.add("http://www.w3.org/2001/XMLSchema#unsignedShort");
		
		return dataTypes;
	}
	
	
	public String getPrefix(String the_key){
		
		return prefixSet.get(the_key);	
	}
	
	public Iterator getPrefixIterator() {
		
		return prefixSet.entrySet().iterator();
	}
}

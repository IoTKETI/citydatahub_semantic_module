package com.cityhub.semantic;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ServerConfiguration.API_BASE_URI)
public class APIController {
	
	
	  /////////////////////////////////////////////
	 //------------>GET Graph List
	/////////////////////////////////////////////
	@RequestMapping(value = "/graphs")
	@CrossOrigin("*")
	@ResponseBody
	public ResponseEntity<String> getGraphList(
			@RequestParam(value = "graphType") String[] graphType,
			@RequestParam(value = "keyword") String[] keywords,
			@RequestParam(value = "prefixFormat", defaultValue = "normal") String prefixFormat,
			@RequestParam(value = "limit", defaultValue = "1000") long limit,
			@RequestParam(value = "user") String user,
			@RequestParam(value = "accessToken") String accessToken
								) {
		
TDBHandler tdb_handler = new TDBHandler();
		
		//-------------------->ontology, instance
		boolean graphTypes[] = { false, false };


		// Access Control Policy Work:

		ACPHandler acp_handler = new ACPHandler();

		boolean access = false;

		try {
			access = acp_handler.postAccessDecision(user, accessToken, "R");

		} catch (IOException e) {
			e.printStackTrace();
		}

		if(access){

			//-------------->Checking graphTypes
			for(int i=0; i<graphType.length && graphType!=null; i++) {

				if( graphType[i].equalsIgnoreCase("ontology") ){ graphTypes[0] = true; }
				if( graphType[i].equalsIgnoreCase("instance") ) { graphTypes[1] = true; }
			}

			String response = tdb_handler.execute_GraphSearchQuery(
																	graphTypes,
																	new LinkedList(Arrays.asList(keywords)),
																	getIntegerFormatValue(prefixFormat),
																	limit
			);

			return new ResponseEntity<String>(response, HttpStatus.OK);

		}else{
			return new ResponseEntity<String>(
										"UNAUTHORIZED: Either Username or Access Token is invalid.",
												HttpStatus.UNAUTHORIZED
			);
		}
	}
	
	
	
	
	  	  /////////////////////////////////////////////
		 //------------>GET Entity List
		/////////////////////////////////////////////	
		@RequestMapping(value = "/entities")
		@ResponseBody
		public ResponseEntity<String> getEntityList(
									@RequestParam(value = "keyword") String[] keywords,
									@RequestParam(value = "entityType") String[] entityTypes,
									@RequestParam(value = "prefixFormat", defaultValue = "normal") String prefixFormat,
									@RequestParam(value = "limit", defaultValue = "1000") long limit,
									@RequestParam(value = "user") String user,
									@RequestParam(value = "accessToken") String accessToken
									) {
			
			TDBHandler tdb_handler = new TDBHandler();


			// Access Control Policy Work:

			ACPHandler acp_handler = new ACPHandler();

			boolean access = false;

			try {
				access = acp_handler.postAccessDecision(user, accessToken, "R");

			} catch (IOException e) {
				e.printStackTrace();
			}

			if(access){

				String response = tdb_handler.execute_EntityListSearchQuery(
																			new LinkedList(Arrays.asList(keywords)),
																			new LinkedList(Arrays.asList(entityTypes)),
																			getIntegerFormatValue(prefixFormat),
																			limit
				);
				return new ResponseEntity<String>(response, HttpStatus.OK);

			}else{

				return new ResponseEntity<String>(
											"UNAUTHORIZED: Either Username or Access Token is invalid.",
													HttpStatus.UNAUTHORIZED
				);
			}
		}
		
		
		
		
		  /////////////////////////////////////////////
		 //---------->GET Individual List
		/////////////////////////////////////////////	
		@RequestMapping(value = "/individuals")
		@ResponseBody
		public ResponseEntity<String> getIndividualList(
									@RequestParam(value = "classId") String[] classIds,
									@RequestParam(value = "prefixFormat", defaultValue = "normal") String prefixFormat,
									@RequestParam(value = "limit", defaultValue = "1000") long limit,
									@RequestParam(value = "user") String user,
									@RequestParam(value = "accessToken") String accessToken
									) {
			
			TDBHandler tdb_handler = new TDBHandler();

			// Access Control Policy Work:

			ACPHandler acp_handler = new ACPHandler();

			boolean access = false;

			try {
				access = acp_handler.postAccessDecision(user, accessToken, "R");

			} catch (IOException e) {
				e.printStackTrace();
			}

			if(access){

				String response = tdb_handler.execute_IndividualSearchQuery(
																			new LinkedList(Arrays.asList(classIds)),
																			getIntegerFormatValue(prefixFormat),
																			limit
				);
				return new ResponseEntity<String>(response, HttpStatus.OK);

			}else{

				return new ResponseEntity<String>(
						"UNAUTHORIZED: Either Username or Access Token is invalid.",
						HttpStatus.UNAUTHORIZED
				);
			}
		}
		
		
		
		  /////////////////////////////////////////////
		 //------------>GET Entity Info
		/////////////////////////////////////////////	
		@RequestMapping(value = "/entities/{entityURI}")
		@ResponseBody
		public ResponseEntity<String> getEntityInfo(
									@PathVariable String entityURI, 
									@RequestParam (value = "responseType") String responseType,
									@RequestParam(value = "prefixFormat", defaultValue = "normal") String prefixFormat,
									@RequestParam (value = "limit", defaultValue = "1000") long limit,
									@RequestParam(value = "user") String user,
									@RequestParam(value = "accessToken") String accessToken
									) {
			
			TDBHandler tdb_handler = new TDBHandler();
			
			System.out.println("Retrieved Entity ID: " + entityURI);

			// Access Control Policy Work:

			ACPHandler acp_handler = new ACPHandler();

			boolean access = false;

			try {
				access = acp_handler.postAccessDecision(user, accessToken, "R");

			} catch (IOException e) {
				e.printStackTrace();
			}

			if(access){

				String response = tdb_handler.execute_GetEntityInfo(
																	entityURI,
																	getIntegerFormatValue(responseType),
																	getIntegerFormatValue(prefixFormat),
																	limit
				);
				return new ResponseEntity<String>(response, HttpStatus.OK);

			}else{

				return new ResponseEntity<String>(
						"UNAUTHORIZED: Either Username or Access Token is invalid.",
						HttpStatus.UNAUTHORIZED
				);
			}
		}
		
		
		
		
		  /////////////////////////////////////////////
		 //------------>GET Graph Info
		/////////////////////////////////////////////	
		@RequestMapping(value = "/graphs/{graphURI}")
		@CrossOrigin("*")
		@ResponseBody
		public ResponseEntity<String> getGraphInfo(
									@PathVariable String graphURI,
									@RequestParam(value = "prefixFormat", defaultValue = "normal") String prefixFormat,
									@RequestParam (value = "limit", defaultValue = "1000") long limit,
									@RequestParam (value = "asFile") String asFile,
									@RequestParam(value = "user") String user,
									@RequestParam(value = "accessToken") String accessToken
									) {
			
			boolean saveAsFile = false;
			
			asFile = asFile.toLowerCase();
			
			if( asFile.equals("y") || asFile.equals("yes") || asFile.equals("true") ) {
				
				saveAsFile = true;
			}
			
			TDBHandler tdb_handler = new TDBHandler();

			// Access Control Policy Work:

			ACPHandler acp_handler = new ACPHandler();

			boolean access = false;

			try {
				access = acp_handler.postAccessDecision(user, accessToken, "R");

			} catch (IOException e) {
				e.printStackTrace();
			}

			if(access){

				String response = tdb_handler.execute_GetGraphInfo( graphURI, getIntegerFormatValue(prefixFormat), limit, saveAsFile );
				return new ResponseEntity<String>(response, HttpStatus.OK);

			}else{

				return new ResponseEntity<String>(
						"UNAUTHORIZED: Either Username or Access Token is invalid.",
						HttpStatus.UNAUTHORIZED
				);
			}
		}
		
		
		
		
		  /////////////////////////////////////////////
		 //----------->GET Class Hierarchy
		/////////////////////////////////////////////	
		@RequestMapping(value = "/classhierarchy/{classURI}")
		@ResponseBody
		public ResponseEntity<String> getClassHierarchy(
									@PathVariable String classURI,
									@RequestParam(value = "prefixFormat", defaultValue = "normal") String prefixFormat,
									@RequestParam (value = "limit" , defaultValue = "1000") long limit,
									@RequestParam(value = "user") String user,
									@RequestParam(value = "accessToken") String accessToken
									) {
			
			TDBHandler tdb_handler = new TDBHandler();

			// Access Control Policy Work:

			ACPHandler acp_handler = new ACPHandler();

			boolean access = false;

			try {
				access = acp_handler.postAccessDecision(user, accessToken, "R");

			} catch (IOException e) {
				e.printStackTrace();
			}

			if(access){

				String response = tdb_handler.execute_GetClassHierarchy( classURI, getIntegerFormatValue(prefixFormat), limit );
				return new ResponseEntity<String>(response, HttpStatus.OK);

			}else{

				return new ResponseEntity<String>(
						"UNAUTHORIZED: Either Username or Access Token is invalid.",
						HttpStatus.UNAUTHORIZED
				);
			}
		}
	
		
		  /////////////////////////////////////////////
		 //----------->POST Insert Triple Data
		/////////////////////////////////////////////
		@RequestMapping(method=RequestMethod.POST, value="/insert")
	public void insertTripleData(@RequestBody Map<String, Object> triple_data) {
			
			TDBHandler tdb_handler = new TDBHandler();
			
			System.out.println(triple_data);
			
			tdb_handler.execute_InsertTripleData(triple_data);
	}
	
		
		
	  /////////////////////////////////////////////
	 //----------->DELETE Delete Graph
	/////////////////////////////////////////////
	
	@RequestMapping(
			method=RequestMethod.DELETE, 
			value="/delete",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
			)
	public void deleteGraph(@RequestParam (value = "graphURI") String graphURI) {
			
			TDBHandler tdb_handler = new TDBHandler();
			
			System.out.println(graphURI);
			
			tdb_handler.execute_DeleteGraph(graphURI);
	}
	
	
	
	int getIntegerFormatValue(String format) {
		
		int format_type = 0;
		
		if(format.equals("simple")) {
			
			format_type = 1;
			
		}else if(format.equals("normal")) {
			
			format_type = 2;
		}
		
		return format_type ;
	}
}

package semantics.cityhub.keti;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import semantics.cityhub.keti.dao.SWRLRuleDAO;
import semantics.cityhub.keti.model.SWRLRuleEntity;
import semantics.cityhub.keti.reasoner.SemanticReasoner;
import semantics.cityhub.keti.utils.CSVHandler;
import semantics.cityhub.keti.utils.JSONDataHandler;
import semantics.cityhub.keti.utils.NullOntologyModelerException;
import semantics.cityhub.keti.utils.SWRLRuleParser.NoPropertyFoundException;

@RestController
@RequestMapping(APIConfiguration.API_BASE_URI)
public class APIController {
	

	@Autowired
	private SemanticReasoner smReasoner;
	@Autowired
	private SWRLRuleDAO ruleDAO;
	
	
	@RequestMapping("/rules")
	@CrossOrigin("*")
	public ResponseEntity<String> generateSWRLRules(@RequestParam(value = "id") String swrl_SetId) {
		
		 try {
			 Optional<SWRLRuleEntity> optional = ruleDAO.findById(swrl_SetId);
			 
			 if(optional.isPresent()) {
				 
				 SWRLRuleEntity rule_Entity = optional.get();
				
				 smReasoner.composeModel(rule_Entity);
				 
				 JSONDataHandler json_handler = new JSONDataHandler();
					
				 return new ResponseEntity<String>(
											json_handler.toJSONString( rule_Entity ), 
											HttpStatus.OK
											);
				 
			 }else {
				 
				 throw new InvalidOntologyModelIdException("Provide valid Id."); 
			 }
			
//			"Here you will get the Stringify version of SWRL Set Id"
//			+ ", which is the OntologyModel id. If you are recieving this "
//			+ "message it means the API function is working property."
			
			
		} catch (InvalidOntologyModelIdException | NoPropertyFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return new ResponseEntity<String>("UNAUTHORIZED: Invalid SWRL Set Id.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/rules")
	@CrossOrigin("*")
	public ResponseEntity<String> generateSWRLRules(@RequestBody Map<String, Object> json_swrlRules) { 
		
		try {
			
			System.out.println( json_swrlRules );
			
			smReasoner.composeModel(json_swrlRules);
			
			ruleDAO.save( smReasoner.exportRuleEntity() );
			
			return new ResponseEntity<String>("", HttpStatus.CREATED);
			
		} catch (NoPropertyFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return new ResponseEntity<String>("NoPropertyFoundException: The given Data Property Not Found in the Rule Set.", HttpStatus.UNPROCESSABLE_ENTITY);
		
		} catch(Exception e1) {
			e1.printStackTrace();
			
			return new ResponseEntity<String>("Failed To Register the Rule! Possible Reason: Check if the rule_id is duplicate.", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/execute")
	@CrossOrigin("*")
	public void generateLabeling(
									HttpServletResponse servletResponse,						
									@RequestParam(value = "ruleId") String swrl_SetId, 
									@RequestParam("file") MultipartFile csv_file
									) {
		
		
		Optional<SWRLRuleEntity> optional = ruleDAO.findById(swrl_SetId);
			 
		try {
			if(optional.isPresent()) {
				 
				 SWRLRuleEntity rule_Entity = optional.get();
				 
				 smReasoner.composeModel(rule_Entity);
				
			}else {
				 
				 throw new InvalidOntologyModelIdException("Provide valid Id."); 
			 }
			
			if(!csv_file.isEmpty()) {
				
					BufferedReader br = new BufferedReader(
											new InputStreamReader(
												new ByteArrayInputStream( csv_file.getBytes() ) 
												)
											);
					
					CSVHandler csvHandler = smReasoner.performInferencing(br);
					
					br.close();
					
					servletResponse.setContentType("text/csv");
					servletResponse.addHeader("Content-Disposition","attachment; filename=\"labelled_dataset.csv\"");
					csvHandler.writeDatasetToCSV( servletResponse.getWriter() );
			}
				
		} catch (NoPropertyFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (InvalidOntologyModelIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullOntologyModelerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		try {
//			OntologyModel model_recieved = ontology_Modeler.getOntologyModel(swrl_SetId);
//			
//			ArrayList<String> properties = (ArrayList<String>) json_dataset.get("data-properties");
//			ArrayList<String> xsdTypes = (ArrayList<String>) json_dataset.get("xsd-types");
//			ArrayList<Object> data = (ArrayList<Object>) json_dataset.get("data");
//			
//			SemanticReasoner sReasoner = new SemanticReasoner(model_recieved);
//			
//			sReasoner.performInferencing(properties, xsdTypes, data);
//			
//			
//			return new ResponseEntity<String>("", HttpStatus.CREATED);
//			
////		} catch (InvalidOntologyModelIdException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////			
////			return new ResponseEntity<String>("UNAUTHORIZED: Invalid SWRL Set Id.", HttpStatus.UNAUTHORIZED);
//			
//		} catch (NullOntologyModelerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			
//			return new ResponseEntity<String>("UNPROCESSABLE: Null Ontology Model.", HttpStatus.UNPROCESSABLE_ENTITY);
//		}
		//return new ResponseEntity<String>("", HttpStatus.CREATED);
	}
	
	public class InvalidOntologyModelIdException extends Exception{
		
		String exception_Message ;
		
		public InvalidOntologyModelIdException(String message) {
			
			exception_Message = message;
		}
		
		public String toString() {
			
			return ( "Invalid Ontology Model ID!. " + exception_Message );
		}
	}
	
}

package semantics.cityhub.keti;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.Gson;

import semantics.cityhub.keti.reasoner.OntologyModel;
import semantics.cityhub.keti.utils.SWRLRuleParser;
import semantics.cityhub.keti.utils.SWRLRuleParser.NoPropertyFoundException;

@SpringBootApplication
public class SwrlRuleGeneratorApiApplication {

	static String swrl_rule = "";
	
	static List<String> properties;
	
	static List<String> swrl_vars;
	
	public static void main(String[] args) {
		SpringApplication.run(SwrlRuleGeneratorApiApplication.class, args);
		
		initialization();
		
		SWRLRuleParser parser;
		OntologyModel ont_Modeler = null;
		
//		try {
//			parser = new SWRLRuleParser( /*new OntologyModel("swrl1")*/ );
//			
//			parser.parseDataProperties( properties );
//			
//			parser.parseSWRLVariables( swrl_vars );
//			
//			ont_Modeler = parser.parseSWRLRule( swrl_rule );
//			
//		} catch (NoPropertyFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		

//		
//		Gson gson = new Gson();
//		
//		//Type jsonType = new TypeToken<LinkedList<String>>(){}.getType();
//		
//		LinkedList<String> in_vars = gson.fromJson(json_inputVariables.toJSONString(), LinkedList.class );
//		
//		LinkedList<String> in_rowValues = gson.fromJson(json_rowValues.toJSONString(), LinkedList.class );
//		
//		try {
//			SemanticReasoner sReasoner = new SemanticReasoner( ont_Modeler );
//				
//				sReasoner.constructAssertions(in_vars, in_rowValues);
//			
//			
//		} catch (NullOntologyModelerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	static void initialization() {
		
		swrl_rule = "sourceValue(?x,\"m\") -> targetValue(?x, \"meter\")";
		//swrl_rule = "hasParent(?x, ?y) ^ hasBrother(?y, ?z) -> hasUncle(?x, ?z)";
		//swrl_rule = "Person(Fred) ^ hasSibling(Fred, ?s) ^ Man(?s) -> hasBrother(Fred, ?s)";
		//swrl_rule = "Person(?p) ^ hasAge(?p,?age) ^ swrlb:greaterThan(?age,17) -> Adult(?p)";
		//swrl_rule = "Person(?p) ^ hasNumber(?p, ?number) ^ swrlb:startsWith(?number, \"+\") -> hasInternationalNumber(?p, true)";
		//swrl_rule = "Person(?p) ^ hasSalaryInPounds(?p, ?pounds) ^ swrlb:multiply(?dollars, ?pounds, 2.0) -> hasSalaryInDollars(?p, ?dollars) ";
		//swrl_rule = "Person(?p) ^ hasSalaryInPounds(?p, ?pounds) ^ swrlb:multiply(2.0, ?pounds, ?dollars) -> hasSalaryInDollars(?p, ?dollars) ";
		//swrl_rule = "(hasChild >= 1)(?x) -> Parent(?x)";
		//swrl_rule = "Parent(?x) -> (hasChild >= 1)(?x)";
		//swrl_rule = "Publication(?p) ^ hasAuthor(?p, ?y) ^ hasAuthor(?p, ?z) ^ differentFrom(?y, ?z) -> cooperatedWith(?y, ?z)";
		
		 //---->Invalid Syntax
		//swrl_rule = "Person(?p), integer[>= 18 , <= 65](?age), hasAge(?p, ?age) -> hasDriverAge(?p, true)";
		
		properties = new LinkedList<String>();
		
		properties.add("sourceValue");
		properties.add("targetValue");
		
		swrl_vars = new LinkedList<String>();
		
		swrl_vars.add("x");
		
	}

}

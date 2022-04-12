package semantics.cityhub.keti.model;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.sql.rowset.serial.SerialClob;


import org.hibernate.engine.jdbc.NonContextualLobCreator;
import org.json.simple.JSONArray;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="SWRLRule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SWRLRuleEntity {

	@Id
	@Column(name="swrl_rule_id",length=200)
	private String rule_Id;
	@Lob
	private Clob data_Properties;
	@Lob
	private Clob swrl_Variables;
	@Lob
	private Clob swrl_RuleString;
	@Lob
	private Clob ontology_OWLSerialized;
	
	
	
	public String clobToString(Clob clob) throws IOException, SQLException {
		
		Reader r = clob.getCharacterStream();
		
		StringBuffer buffer = new StringBuffer();
		int ch;
		while ((ch = r.read())!=-1) {
		   buffer.append(""+(char)ch);
		}
		
		return buffer.toString();
	}
	
	
	public java.sql.Clob stringToClob(String s) {
		
		return NonContextualLobCreator.INSTANCE.createClob(s);
	}
	
	
	  /////////////////
	 //---->Getters
	/////////////////
	
	public String getRule_Id() {
		return rule_Id;
	}

	public String getData_Properties() throws IOException, SQLException {
		return clobToString( data_Properties );
	}

	public String getSwrl_Variables() throws IOException, SQLException {
		return clobToString( swrl_Variables );
	}

	public String getSwrl_RuleString() throws IOException, SQLException {
		return clobToString( swrl_RuleString );
	}

	public String getOntology_OWLSerialized() throws IOException, SQLException {
		return clobToString( ontology_OWLSerialized );
	}
	
	public JSONArray getDataPropertiesAsJSON() throws IOException, SQLException {
		// TODO Auto-generated method stub
		
		String[] comaSeperated = clobToString( data_Properties ).split(",");

		JSONArray json_dataProperties = new JSONArray();
		
		for(int i=0; i<comaSeperated.length; i++) {
			
			json_dataProperties.add( comaSeperated[i] );
		}
		
		return json_dataProperties;
	}
	
	public Object getDataVariablesAsJSON() throws IOException, SQLException {
		// TODO Auto-generated method stub
		
		String[] comaSeperated = clobToString( swrl_Variables ).split(",");

		JSONArray json_swrlVariables = new JSONArray();
		
		for(int i=0; i<comaSeperated.length; i++) {
			
			json_swrlVariables.add( comaSeperated[i] );
		}
		
		return json_swrlVariables;
	}

	
	  /////////////////
	 //---->Setters
	/////////////////
	
	public void setRule_Id(String rule_Id) {
		this.rule_Id = rule_Id;
	}
	
	public void setData_Properties(String data_Properties) {
		this.data_Properties = stringToClob( data_Properties );
	}
	
	public void setSwrl_Variables(String swrl_Variables) {
		this.swrl_Variables = stringToClob( swrl_Variables );
	}
	
	public void setSwrl_RuleString(String swrl_RuleString) {
		this.swrl_RuleString = stringToClob( swrl_RuleString );
	}
	
	public void setOntology_OWLSerialized(String ontology_OWLSerialized) {
		this.ontology_OWLSerialized = stringToClob( ontology_OWLSerialized );
	}

}

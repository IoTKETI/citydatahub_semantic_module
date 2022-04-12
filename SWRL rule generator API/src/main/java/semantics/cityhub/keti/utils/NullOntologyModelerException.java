package semantics.cityhub.keti.utils;

public class NullOntologyModelerException extends Exception{

	String exception_Message ;
	
	public NullOntologyModelerException(String message) {
		
		exception_Message = message;
	}
	
	public String toString() {
		
		return ( "Ontology Modeler is NULL!. " + exception_Message );
	}

}

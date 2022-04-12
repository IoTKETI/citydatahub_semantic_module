package com.semantics.validation;


////////////////////////////////---------IMPORTANT NOTE---------////////////////////////////////////
//																						    	   //
//--> This Class provides RDFNode object based on XSD Type URI checking which is assumed    	  //
//---> to be included in the value string. However, it does not validate with the type      		 //
//----> definitions provided in the ontologies in the TDB. That part may be added later in  		//
//-----> the development....                                                                      //
//------> In addition, there is no exception added if the value doesn't match any type, because   //
//-------> the default type can be considered as string and we did not exclude any case in value  //
//--------> having type string.																    //
//																						           //
////////////////////////////////////////////////////////////////////////////////////////////////////

import org.apache.commons.lang3.math.NumberUtils;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

public class XSDTypeDataHandler {

    private OWLDatatype appropriate_DataType = null;
    private String appropriate_resourceValue = null;
    private boolean is_Resource = false;

    /////////////////////////////////////////////////////////////////////
    //--->Convert String value to OWLLiteral
    /////////////////////////////////////////////////////////////////////
    public OWLLiteral toOWLLiteral(String value, OWLDataFactory dfactory) {

        OWLLiteral owlLiteral = null;

        set_OWLDataType( value, dfactory );

        if( !is_Resource ) {

            owlLiteral = dfactory.getOWLLiteral(appropriate_resourceValue, appropriate_DataType);

        }else {

            //---->NOTE: This part is not handled here and left blank. Handled outside this class
            //---->      which is to identify the object properties...
            //rdfNode = ResourceFactory.createResource( appropriate_resourceValue );
            is_Resource = false;
        }

        return owlLiteral;
    }

    ///////////////////////////////////////////////////////////////////
    //---> Set the appropriate OWL Datatype and Resource Value
    //----> based on the value.
    ///////////////////////////////////////////////////////////////////
    public void set_OWLDataType(String value, OWLDataFactory dfactory) {

        String value_part[] = value.split("\\^\\^");

        appropriate_resourceValue = value_part[0];

        if( value_part.length >= 2 ) {

            if ( is_XSDanyURI(value_part[1]) ) {

                appropriate_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_ANY_URI.getIRI());
            }
            else if ( is_float(value_part[1]) ) {

                appropriate_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_FLOAT.getIRI());
            }
            else if ( is_double(value_part[1]) ) {

                appropriate_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_DOUBLE.getIRI());
            }
            else if (
                    is_decimal(value_part[1]) ||
                            ( NumberUtils.isNumber(appropriate_resourceValue) && appropriate_resourceValue.contains(".") )
            ) { //---> Value check with xsd:decimal URI OR A number having point (.)

                appropriate_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_DECIMAL.getIRI());
            }
            else if ( is_positiveInteger(value_part[1]) ) {

                appropriate_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_POSITIVE_INTEGER.getIRI());
            }
            else if ( is_unsignedInt(value_part[1]) ) {

                appropriate_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_UNSIGNED_INT.getIRI());
            }
            else if (
                    is_boolean(value_part[1]) ||
                            appropriate_resourceValue.equalsIgnoreCase("true") ||
                            appropriate_resourceValue.equalsIgnoreCase("false")
            ){

                appropriate_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_BOOLEAN.getIRI());
            }
            else if( is_dateTime(value_part[1]) ) {

                appropriate_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_DATE_TIME.getIRI());
            }
            else if( is_string(value_part[1]) ){ //---> Value check with xsd:string URI
                appropriate_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_STRING.getIRI());
            }

        }
        else if(
                appropriate_resourceValue.contains("#")
        ){ //---> Checking if the URI: owl:NamedIndividual OR a owl:Class

            if( isURI(appropriate_resourceValue) ) {
                // This will be checked in the RDFNode creation when there will be no need of
                // XSD Data Type Resource
                is_Resource = true;
            }
        }
        else { //---> default xsd:string condition.
            appropriate_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_STRING.getIRI());
        }
    }

    ///////////////////////////////////////////////////////////////////
    //---> Set the appropriate OWL Datatype
    //----> based on the received String XSD Datatype URI
    ///////////////////////////////////////////////////////////////////
    public OWLDatatype get_OWLXSDDataTypeOnly(String xsd_type, OWLDataFactory dfactory) {

        OWLDatatype parsed_DataType = null;

        if ( is_XSDanyURI(xsd_type) ) {

            parsed_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_ANY_URI.getIRI());
        }
        else if ( is_float(xsd_type) ) {

            parsed_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_FLOAT.getIRI());
        }
        else if ( is_double(xsd_type) ) {

            parsed_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_DOUBLE.getIRI());
        }
        else if ( is_decimal(xsd_type) ) { //---> Value check with xsd:decimal URI

            parsed_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_DECIMAL.getIRI());
        }
        else if ( is_positiveInteger(xsd_type) ) {

            parsed_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_POSITIVE_INTEGER.getIRI());
        }
        else if ( is_unsignedInt(xsd_type) ) {

            parsed_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_UNSIGNED_INT.getIRI());
        }
        else if ( is_boolean(xsd_type) ){

            parsed_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_BOOLEAN.getIRI());
        }
        else if( is_dateTime(xsd_type) ) {

            parsed_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_DATE_TIME.getIRI());
        }
        else if( is_string(xsd_type) ){ //---> Value check with xsd:string URI

            parsed_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_STRING.getIRI());
        }else {
            parsed_DataType = dfactory.getOWLDatatype(OWL2Datatype.XSD_STRING.getIRI());
        }

        return parsed_DataType;
    }

    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:anyURI
    /////////////////////////////////////////////////////////////////////
    public boolean is_XSDanyURI(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#anyURI");
    }

    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:boolean
    /////////////////////////////////////////////////////////////////////
    public boolean is_boolean(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#boolean");
    }

    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:byte
    /////////////////////////////////////////////////////////////////////
    public boolean is_byte(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#byte");
    }

    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:dateTime
    /////////////////////////////////////////////////////////////////////
    public boolean is_dateTime(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#dateTime") ;
    }

    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:dateTimeStamp
    /////////////////////////////////////////////////////////////////////
    public boolean is_dateTimeStamp(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#dateTimeStamp") ;
    }

    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:decimal
    /////////////////////////////////////////////////////////////////////
    public boolean is_decimal(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#decimal");
    }

    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:double
    /////////////////////////////////////////////////////////////////////
    public boolean is_double(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#double");
    }

    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:float
    /////////////////////////////////////////////////////////////////////
    public boolean is_float(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#float");
    }

    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:int
    /////////////////////////////////////////////////////////////////////
    public boolean is_int(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#int");
    }

    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:integer
    /////////////////////////////////////////////////////////////////////
    public boolean is_integer(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#integer");
    }

    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:long
    /////////////////////////////////////////////////////////////////////
    public boolean is_long(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#long");
    }

    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:Name
    /////////////////////////////////////////////////////////////////////
    public boolean is_Name(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#Name");
    }

    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:negativeInteger
    /////////////////////////////////////////////////////////////////////
    public boolean is_negativeInteger(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#negativeInteger");
    }



    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:nonNegativeInteger
    /////////////////////////////////////////////////////////////////////
    public boolean is_nonNegativeInteger(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
    }



    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:nonPositiveInteger
    /////////////////////////////////////////////////////////////////////
    public boolean is_nonPositiveInteger(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#nonPositiveInteger");
    }



    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:normalizedString
    /////////////////////////////////////////////////////////////////////
    public boolean is_normalizedString(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#normalizedString");
    }



    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:positiveInteger
    /////////////////////////////////////////////////////////////////////
    public boolean is_positiveInteger(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#positiveInteger");
    }



    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:short
    /////////////////////////////////////////////////////////////////////
    public boolean is_short(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#short");
    }



    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:string
    /////////////////////////////////////////////////////////////////////
    public boolean is_string(String value) {

        return (
                value.contains("http://www.w3.org/2001/XMLSchema#string") ||
                        ( !value.contains("^^http://www.w3.org/2001/XMLSchema#string") && (value.startsWith("\"") && value.endsWith("\"")) )
        );
    }



    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:token
    /////////////////////////////////////////////////////////////////////
    public boolean is_token(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#token");
    }



    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:unsignedInt
    /////////////////////////////////////////////////////////////////////
    public boolean is_unsignedInt(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#unsignedInt");
    }



    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:unsignedLong
    /////////////////////////////////////////////////////////////////////
    public boolean is_unsignedLong(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#unsignedLong");
    }



    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is xsd:unsignedShort
    /////////////////////////////////////////////////////////////////////
    public boolean is_unsignedShort(String value) {

        return value.contains("http://www.w3.org/2001/XMLSchema#unsignedShort");
    }



    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is rdfs:Literal
    /////////////////////////////////////////////////////////////////////
    public boolean isLiteral(String value) {

        return value.contains("http://www.w3.org/2000/01/rdf-schema#Literal");
    }



    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is rdf:PlainLiteral
    /////////////////////////////////////////////////////////////////////
    public boolean isPlainLiteral(String value) {

        return value.contains("http://www.w3.org/1999/02/22-rdf-syntax-ns#PlainLiteral");
    }



    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is URI
    /////////////////////////////////////////////////////////////////////
    public boolean isURI(String uri) {

        return uri.startsWith("http://") || uri.startsWith("https://");
    }



    /////////////////////////////////////////////////////////////////////
    //--->Check if the String value is rdf:XMLLiteral
    /////////////////////////////////////////////////////////////////////
    public boolean isXMLLiteral(String value) {

        return value.contains("http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral");
    }


    public String getAppropriate_resourceValue() {
        return appropriate_resourceValue;
    }


    public OWLDatatype getAppropriate_DataType() {
        return appropriate_DataType;
    }
}

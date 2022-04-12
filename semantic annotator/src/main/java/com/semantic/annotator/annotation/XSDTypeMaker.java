package com.semantic.annotator.annotation;

import org.apache.jena.datatypes.xsd.XSDDatatype;

public class XSDTypeMaker {

    public XSDDatatype getXSDType(String range) {

        XSDDatatype datatype = null;

        if(range.equals("\"http://www.w3.org/2001/XMLSchema#string\"")){
            datatype = XSDDatatype.XSDstring;
        } else if (range.equals("\"http://www.w3.org/2001/XMLSchema#positiveInteger\"")) {
            datatype = XSDDatatype.XSDpositiveInteger;
        } else if (range.equals("\"http://www.w3.org/2001/XMLSchema#unsignedInt\"")) {
            datatype = XSDDatatype.XSDunsignedInt;
        } else if (range.equals("\"http://www.w3.org/2001/XMLSchema#double\"")) {
            datatype = XSDDatatype.XSDdouble;
        } else if (range.equals("\"http://www.w3.org/2001/XMLSchema#dateTime\"")) {
            datatype = XSDDatatype.XSDdateTime;
        } else if (range.equals("\"http://www.w3.org/2001/XMLSchema#anyURI\"")) {
            datatype = XSDDatatype.XSDanyURI;
        }
        return datatype;
    }

}

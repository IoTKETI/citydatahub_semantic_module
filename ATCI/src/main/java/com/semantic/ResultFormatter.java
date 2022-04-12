package com.semantic;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

public class ResultFormatter {

	public static void formatMyOutput(ResultSet rs) {
		
		ResultSetFormatter.out(rs);
	}
}

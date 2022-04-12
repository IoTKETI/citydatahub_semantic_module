package com.semantic;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

public class VirtuosoDropGraph {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		// TODO Auto-generated method stub

				VirtGraph graph = new VirtGraph ("jdbc:virtuoso://localhost:1111", "exUser", "keti123");
		        
				QuerySet qs = new QuerySet();
				
				//System.out.println("\nexecute: INSERT Query .....");
		        
				//VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(qs.getQuery("insert_triple_query"), graph);
		        //vur.exec();
		        
				System.out.println("\nexecute: DROP GRAPH Query .....");
				
		        VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(qs.getQuery("drop_graph_query"), graph);
		        vur.exec();
	}

}

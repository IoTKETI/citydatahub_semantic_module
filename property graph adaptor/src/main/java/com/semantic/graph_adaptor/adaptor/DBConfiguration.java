package com.semantic.graph_adaptor.adaptor;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.CollectionType;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.model.CollectionCreateOptions;

import java.util.ArrayList;
import java.util.Collection;

public  class DBConfiguration {

    final String dbName = "ngsiLdDB";
    final String entityColl = "entityColl";
    final String propertyColl = "propertyColl";
    final String edgeColl = "edgeColl";
    final String graph = "NGSI-LD_graph";
//    final Collection<EdgeDefinition> edgeDefinitions = new ArrayList<>();
//    final EdgeDefinition edgeDefinition = new EdgeDefinition().collection(edgeColl).from(entityColl).to(entityColl).to(propertyColl);

    public ArangoDB dbConf() {
        ArangoDB arangoDB = new ArangoDB.Builder().host("localhost", 8529).user("root").password("0000").build();

        // create db
        try {
            arangoDB.db(dbName).drop();
            arangoDB.createDatabase(dbName);
            System.out.println("DB created: " + dbName);
        } catch (final ArangoDBException e) {
            System.err.println("Failed to create db: " + dbName + "; " + e.getMessage());
        }

        // create collection
        try {
            arangoDB.db(dbName).createCollection(entityColl);
            arangoDB.db(dbName).createCollection(propertyColl);
            System.out.println("Collection created: " + entityColl + ", " + propertyColl);
        } catch (final ArangoDBException e) {
            System.err.println("Failed to create collection: " + entityColl + "; " + e.getMessage());
            System.err.println("Failed to create collection: " + propertyColl + "; " + e.getMessage());
        }

//        edgeDefinitions.add(edgeDefinition);
//        try {
//            arangoDB.db(dbName).createGraph(graph, edgeDefinitions, null).getEdgeDefinitions();
//            System.out.println("Collection created: " + edgeColl);
//        } catch (final ArangoDBException e) {
//            System.err.println("Failed to create collection: " + edgeColl + "; " + e.getMessage());
//        }

        arangoDB.db(dbName).createCollection(edgeColl, new CollectionCreateOptions().type(CollectionType.EDGES));
        return arangoDB;

    }
}

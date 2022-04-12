package com.semantic.graph_adaptor.adaptor;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.model.DocumentCreateOptions;
import com.google.gson.JsonObject;

public class EntityAdaptor {

    String dbName = "ngsiLdDB";
    String entityColl = "entityColl";

    public void storeEntity(JsonObject body, ArangoDB arangoDB) {

        System.out.println("entity: "+body);

        BaseDocument myObject = new BaseDocument();
        myObject.setKey(body.get("_key").toString().replaceAll("\"", ""));
        myObject.addAttribute("createdAt", body.get("createdAt").toString().replaceAll("\"", ""));
        myObject.addAttribute("modifiedAt", body.get("modifiedAt").toString().replaceAll("\"", ""));
        myObject.addAttribute("id", body.get("id").toString().replaceAll("\"", ""));
        myObject.addAttribute("type", body.get("type").toString().replaceAll("\"", ""));

        try {
            BaseDocument result = arangoDB.db(dbName).collection(entityColl).insertDocument(myObject, new DocumentCreateOptions().returnNew(true)).getNew();
            System.out.println("Entity Vertex created: " + result.getId());
            System.out.println(result.getProperties());
        } catch (ArangoDBException e) {
            System.err.println("Failed to create entity vertex. " + e.getMessage());
        }

    }
}

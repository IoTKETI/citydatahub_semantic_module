package com.semantic.graph_adaptor.adaptor;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Iterator;
import java.util.Map;

public class RelationshipAdaptor {

    String dbName = "ngsiLdDB";
    String entityColl = "entityColl";
    final String edgeColl = "edgeColl";

    public void storeRelationship(JsonObject body, ArangoDB arangoDB) {

        System.out.println("relationship: "+body);

        Iterator<Map.Entry<String, JsonElement>> iterator = body.entrySet().iterator();
        Iterator<String> iteratorKey = body.keySet().iterator();

        while (iterator.hasNext()) {

            JsonElement attribute = iterator.next().getValue();
            String attName = iteratorKey.next();
//            System.out.println(attribute);

            if (attribute.isJsonObject() == true) {

                if (attribute.getAsJsonObject().get("type").toString().equals("\"Relationship\"")) {
//                    System.out.println(attribute.getAsJsonObject().get("object"));

                    try {
//                        BaseDocument document = arangoDB.db(dbName).collection(entityColl).getDocument(attribute.getAsJsonObject().get("object").toString().replaceAll("\"",""), BaseDocument.class);
//                        System.out.println(document.getKey());
                        String query = "INSERT { _from: " + body.get("_from") + ", _to: \"" + entityColl + "/" + attribute.getAsJsonObject().get("object").toString().replaceAll("\"", "") + "\", attributeName: \"" + attName + "\", attributeType: " + attribute.getAsJsonObject().get("type") + " } INTO " + edgeColl ;
                        ArangoCursor<String> cursor = arangoDB.db(dbName).query(query, null, null, String.class);
                    } catch (ArangoDBException e) {
                        System.out.println("Failed to get document: " + attribute.getAsJsonObject().get("object") + "; " + e.getMessage());
                    }
                }

            }

        }

    }
}

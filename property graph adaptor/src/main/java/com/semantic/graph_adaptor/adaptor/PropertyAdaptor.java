package com.semantic.graph_adaptor.adaptor;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.BaseEdgeDocument;
import com.arangodb.entity.DocumentCreateEntity;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.model.DocumentCreateOptions;
import com.arangodb.model.EdgeCreateOptions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Iterator;
import java.util.Map;

public class PropertyAdaptor {

    String dbName = "ngsiLdDB";
    String propertyColl = "propertyColl";
    final String edgeColl = "edgeColl";
    final String graph = "NGSI-LD_graph";

    public void storeProperty(JsonObject body, ArangoDB arangoDB) {

        System.out.println("property: "+body);

        Iterator<Map.Entry<String, JsonElement>> iterator = body.entrySet().iterator();
        Iterator<String> iteratorKey = body.keySet().iterator();
//        System.out.println(iterator);

        while (iterator.hasNext()) {

            JsonElement attribute = iterator.next().getValue();
            String attName = iteratorKey.next();
//            System.out.println(attribute);

            if (attribute.isJsonObject() == true) {
//                System.out.println(attribute);
                if (attribute.getAsJsonObject().get("type").toString().equals("\"Property\"") || attribute.getAsJsonObject().get("type").toString().equals("\"GeoProperty\"")) {
//                    System.out.println(attribute);
//                    String propertyId;

                    if (attribute.getAsJsonObject().get("type").toString().equals("\"GeoProperty\"")) {
//                        document.addAttribute("value", attribute.getAsJsonObject().get("value"));
                        DocumentCreateEntity<String> result = arangoDB.db(dbName).collection(propertyColl).insertDocument(attribute.getAsJsonObject().get("value").toString());
                        System.out.println("Property Vertex created: " + result.getId());

                        String query = "INSERT { _from: " + body.get("_from") + ", _to: \"" + result.getId() + "\", attributeName: \"" + attName + "\", attributeType: " + attribute.getAsJsonObject().get("type") + " } INTO " + edgeColl ;
//                        System.out.println(query);
                        ArangoCursor<String> cursor = arangoDB.db(dbName).query(query, null, null, String.class);
//                        System.out.println(cursor);

//                        BaseEdgeDocument edgeDocument = new BaseEdgeDocument(body.get("_from").toString(), result.getId());
//                        edgeDocument.addAttribute("attributeName", attName);
//                        edgeDocument.addAttribute("attributeType", attribute.getAsJsonObject().get("type"));
////                        arangoDB.db(dbName).graph(graph).edgeCollection(edgeColl).insertEdge(edgeDocument);
//                        arangoDB.db(dbName).collection(edgeColl).insertDocument(edgeDocument);
////                        arangoDB.db(dbName).graph(graph).getEdgeDefinitions()
////                        System.out.println();
////                        propertyId = result.getId();
                    } else {
                        BaseDocument document = new BaseDocument();
                        document.addAttribute("value", attribute.getAsJsonObject().get("value").toString());
                        try {
                            BaseDocument result = arangoDB.db(dbName).collection(propertyColl).insertDocument(document, new DocumentCreateOptions().returnNew(true)).getNew();
                            System.out.println("Property Vertex created: " + result.getId());
//                        String property = arangoDB.db(dbName).collection(propertyColl).insertDocument(document).getNew().getId();
                            System.out.println(result.getProperties());
                            String query = "INSERT { _from: " + body.get("_from") + ", _to: \"" + result.getId() + "\", attributeName: \"" + attName + "\", attributeType: " + attribute.getAsJsonObject().get("type") + " } INTO " + edgeColl ;
                            ArangoCursor<String> cursor = arangoDB.db(dbName).query(query, null, null, String.class);
//                            propertyId = result.getId();
                        } catch (ArangoDBException e) {
                            System.out.println("Failed to create property vertex. " + e.getMessage());
                        }
                    }

//                    BaseEdgeDocument edge = new BaseEdgeDocument(body.get("_from").toString(), propertyId);


                }
            }
        }

//        Iterator<String> iteratorKey = body.keySet().iterator();

//        BaseDocument document = new BaseDocument();
//        document.addAttribute("value", body.get(""));

//        BaseDocument myObject = new BaseDocument();
//        myObject.setKey(body.get("_key").toString().replaceAll("\"", ""));
//        myObject.addAttribute("createdAt", body.get("createdAt"));
//        myObject.addAttribute("modifiedAt", body.get("modifiedAt"));
//        myObject.addAttribute("id", body.get("id"));
//        myObject.addAttribute("type", body.get("type"));
//
//        try {
//            arangoDB.db(dbName).collection(entityColl).insertDocument(myObject);
//            System.out.println("Document created");
//        } catch (ArangoDBException e) {
//            System.err.println("Failed to create document. " + e.getMessage());
//        }

//        try {
//            String query = "INSERT ";
//            ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query, )
//        } catch (ArangoDBException e) {
//            System.err.println("Failed to execute query. " + e.getMessage());
//        }
    }
}

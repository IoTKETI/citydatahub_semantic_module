package com.semantic.graph_adaptor.adaptor;

import com.arangodb.ArangoDB;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;

public class NodeSeparator {

    String entityColl = "entityColl/";
//    Array entities, properties, relationships;
    JsonArray entities = new JsonArray();
    EntityAdaptor entityAdaptor = new EntityAdaptor();
    PropertyAdaptor propertyAdaptor = new PropertyAdaptor();
    RelationshipAdaptor relationshipAdaptor = new RelationshipAdaptor();

    public void nodeSeparator(JsonObject body, ArangoDB arangoDB) {

//        System.out.println(body);
//        Array entities, properties, relationships;

//        JsonArray property, entity, relationship = new JsonArray();

        JsonObject property = new JsonObject();
        property.addProperty("_from", entityColl + body.get("id").toString().replaceAll("\"", ""));
//        System.out.println(property.get("_from"));

        JsonObject entity = new JsonObject();
        entity.add("_key", body.get("id"));
//        System.out.println(entity);

        JsonObject relationship = new JsonObject();
        relationship.addProperty("_from", entityColl + body.get("id").toString().replaceAll("\"", ""));
//        System.out.println(relationship.get("_from"));

        Iterator<Map.Entry<String, JsonElement>> iterator = body.entrySet().iterator();
//        System.out.println(iterator);
        Iterator<String> iteratorKey = body.keySet().iterator();


        while (iterator.hasNext()) {
//            System.out.println(iterator.next());
//            JsonObject attribute = iterator.next().getValue().getAsJsonObject();
//            System.out.println(attribute);
//            System.out.println(iterator.next().getValue().getClass());
//            if(iterator.next().getValue().getAsString().substring(0)=="{") {
//                System.out.println(iterator.next().getValue());
//            }
            JsonElement attribute = iterator.next().getValue();
            String attName = iteratorKey.next();


//            String attName = iterator.p;
//            Iterator<Map.Entry<String, JsonElement>> attribute1 =
//            System.out.println(attribute.isJsonObject());
//            System.out.println(attribute);
            if (attribute.isJsonObject() == true) {
//                Iterator<Map.Entry<String, JsonElement>> subAttribute = attribute.getAsJsonObject().entrySet().iterator();

//                System.out.println(attribute.getAsJsonObject().get("type"));

                if (attribute.getAsJsonObject().get("type").toString().equals("\"Property\"") || attribute.getAsJsonObject().get("type").toString().equals("\"GeoProperty\"")) {
//                    System.out.println(attName);
//                    System.out.println(attribute);
                    property.add(attName, attribute);
                } else if (attribute.getAsJsonObject().get("type").toString().equals("\"Relationship\"")) {

                    relationship.add(attName, attribute);
                }

//                while (subAttribute.hasNext()) {
////                    System.out.println(subAttribute.next());
////                    System.out.println("sub end");
//                    if (subAttribute.next().getValue().equals("Property")) {
//                        System.out.println(attribute);
//                    }
//                }
//                System.out.println(subAttribute.next().getValue());
//                if(subAttribute.next().getValue().equals("Property")) {
//                    System.out.println(attribute);
//                }

            } else if (attribute.isJsonArray() == false){

//                System.out.println(attribute.getClass());
//                System.out.println(attribute);
                entity.add(attName, attribute);
            }
        }
//        entities.add(entity);

//        System.out.println(property);

//        System.out.println(relationship);
//        System.out.println(entity);
        entityAdaptor.storeEntity(entity, arangoDB);
        propertyAdaptor.storeProperty(property, arangoDB);
        relationshipAdaptor.storeRelationship(relationship, arangoDB);

    }

//    public void storeEntity(JsonObject body) {
//
//
//    }

}

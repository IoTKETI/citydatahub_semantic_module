package com.semantic.graph_api.mapper;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoGraph;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.BaseEdgeDocument;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.entity.GraphEntity;
import com.semantic.graph_api.model.Edge;
import com.semantic.graph_api.model.Graph;
import com.semantic.graph_api.model.Vertex;

import java.util.*;

public class ArangoDBHandler {

    final String dbName = "graphAPIDB";
    ArangoDB arangoDB = new ArangoDB.Builder().host("localhost", 8529).user("root").password("0000").build();

    // create DB
    public void createDB(ArangoDB arangoDB) {

        try {
            arangoDB.db(dbName).drop();
            arangoDB.createDatabase(dbName);
            System.out.println("DB created: " + dbName);
        } catch (final ArangoDBException e) {
            System.err.println("Failed to create db: " + dbName + "; " + e.getMessage());
        }
    }

    public void insertGraph(Graph graph) {

        List<EdgeDefinition> definitions = new ArrayList<EdgeDefinition>();
        EdgeDefinition edgeDefinition = new EdgeDefinition();
        edgeDefinition.collection(graph.getId()+"EdgeColl");

        edgeDefinition.from(graph.getId()+"EntityColl");
        edgeDefinition.to(graph.getId()+"EntityColl");
        definitions.add(edgeDefinition);
        arangoDB.db(dbName).createGraph(graph.getId(), definitions);

    }

    public Graph retrieveGraph (String graphId) {

        ArangoGraph arangoGraph = arangoDB.db(dbName).graph(graphId);
        ArangoCollection vertexCollection = arangoDB.db(dbName).collection(arangoGraph.getVertexCollections().toArray()[0].toString());
        ArangoCollection edgeCollection = arangoDB.db(dbName).collection(arangoGraph.getEdgeDefinitions().toArray()[0].toString());

        Graph graph = new Graph();
        graph.setId(graphId);
        graph.setVertexCount(vertexCollection.count().getCount());
        graph.setEdgeCount(edgeCollection.count().getCount());

        return graph;
    }

    public List<Graph> retrieveGraphs() {

        List<Graph> graphs = new ArrayList<Graph>(); // find how to make it json

        Iterator<GraphEntity> iterator = arangoDB.db(dbName).getGraphs().iterator();

        while (iterator.hasNext()) {
            GraphEntity graphEntity = iterator.next();
            graphs.add(retrieveGraph(graphEntity.getName()));
        }

        return graphs;
    }

    public void insertVertices(String graphId, ArrayList<Vertex> vertices) {

        ArangoGraph arangoGraph = arangoDB.db(dbName).graph(graphId);
        ArangoCollection vertexCollection = arangoDB.db(dbName).collection(arangoGraph.getVertexCollections().toArray()[0].toString());

        for(int i=0; i<vertices.size(); i++) {
            Vertex vertex = vertices.get(i);

            BaseDocument document = new BaseDocument();
            document.setKey(vertex.getId());
            document.addAttribute("properties", vertex.getProperties());

            try {
                vertexCollection.insertDocument(document);
            } catch(ArangoDBException e) {
                System.err.println("Failed to create document. " + e.getMessage());
            }
        }
    }

    public Vertex retrieveVertex(String graphId, String vertexId) {

        ArangoGraph arangoGraph = arangoDB.db(dbName).graph(graphId);
        ArangoCollection vertexCollection = arangoDB.db(dbName).collection(arangoGraph.getVertexCollections().toArray()[0].toString());

        BaseDocument document = vertexCollection.getDocument(vertexId, BaseDocument.class);
        Vertex vertex = new Vertex();
        vertex.setId(document.getKey());
        vertex.setProperties(document.getAttribute("properties"));

        return vertex;
    }

    public void updateVertices(String graphId, ArrayList<Vertex> vertices) {

        ArangoGraph arangoGraph = arangoDB.db(dbName).graph(graphId);
        ArangoCollection vertexCollection = arangoDB.db(dbName).collection(arangoGraph.getVertexCollections().toArray()[0].toString());

        for(int i=0; i<vertices.size(); i++) {
            Vertex vertex = vertices.get(i);

            BaseDocument document = new BaseDocument();
            document.addAttribute("properties", vertex.getProperties());

            try {
                vertexCollection.updateDocument(vertex.getId(), document);
            } catch(ArangoDBException e) {
                System.err.println("Failed to update document. " + e.getMessage());
            }
        }
    }

    public void deleteVertex(String graphId, String vertexId) {

        ArangoGraph arangoGraph = arangoDB.db(dbName).graph(graphId);
        ArangoCollection vertexCollection = arangoDB.db(dbName).collection(arangoGraph.getVertexCollections().toArray()[0].toString());
        vertexCollection.deleteDocument(vertexId);

    }

    public void insertEdges(String graphId, ArrayList<Edge> edges) {

        ArangoGraph arangoGraph = arangoDB.db(dbName).graph(graphId);
        ArangoCollection edgeCollection = arangoDB.db(dbName).collection(arangoGraph.getEdgeDefinitions().toArray()[0].toString());
        String vertexColl = arangoGraph.getVertexCollections().toArray()[0].toString();

        for(int i=0; i<edges.size(); i++) {
            Edge edge = edges.get(i);

            BaseEdgeDocument document = new BaseEdgeDocument();
            document.setKey(edge.getId());
            document.setFrom(vertexColl+"/"+edge.getFrom());
            document.setTo(vertexColl+"/"+edge.getTo());
            document.addAttribute("name", edge.getName());
            document.addAttribute("properties", edge.getProperties());

            try {
                edgeCollection.insertDocument(document);
            } catch(ArangoDBException e) {
                System.err.println("Failed to create document. " + e.getMessage());
            }
        }
    }

    public Edge retrieveEdge(String graphId, String edgeId) {

        ArangoGraph arangoGraph = arangoDB.db(dbName).graph(graphId);
        ArangoCollection edgeCollection = arangoDB.db(dbName).collection(arangoGraph.getEdgeDefinitions().toArray()[0].toString());

        BaseEdgeDocument edgeDocument = edgeCollection.getDocument(edgeId, BaseEdgeDocument.class);
        Edge edge = new Edge();
        edge.setId(edgeDocument.getKey());
        edge.setFrom(edgeDocument.getFrom());
        edge.setTo(edgeDocument.getTo());
        edge.setProperties(edgeDocument.getAttribute("properties"));

        return edge;
    }

    public void updateEdges(String graphId, ArrayList<Edge> edges) {

        ArangoGraph arangoGraph = arangoDB.db(dbName).graph(graphId);
        ArangoCollection edgeCollection = arangoDB.db(dbName).collection(arangoGraph.getEdgeDefinitions().toArray()[0].toString());

        for(int i=0; i<edges.size(); i++) {
            Edge edge = edges.get(i);

            BaseDocument document = new BaseDocument();
            document.addAttribute("properties", edge.getProperties());

            try {
                edgeCollection.updateDocument(edge.getId(), document);
            } catch(ArangoDBException e) {
                System.err.println("Failed to create document. " + e.getMessage());
            }
        }
    }

    public void deleteEdge(String graphId, String edgeId) {

        ArangoGraph arangoGraph = arangoDB.db(dbName).graph(graphId);
        ArangoCollection edgeCollection = arangoDB.db(dbName).collection(arangoGraph.getEdgeDefinitions().toArray()[0].toString());
        edgeCollection.deleteDocument(edgeId);

    }

}

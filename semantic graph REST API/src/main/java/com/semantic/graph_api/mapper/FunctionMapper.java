package com.semantic.graph_api.mapper;

import com.semantic.graph_api.model.Edge;
import com.semantic.graph_api.model.Graph;
import com.semantic.graph_api.model.Vertex;

import java.util.ArrayList;
import java.util.List;

public class FunctionMapper {

    private ArangoDBHandler dbHandler = new ArangoDBHandler();

    public void createGraph(Graph graph) {
        try {
            dbHandler.insertGraph(graph);
        } catch (Exception e) {

        }
    }

    public Graph retrieveGraph (String graphId) {
        Graph graph = dbHandler.retrieveGraph(graphId);
        return graph;
    }

    public List<Graph> retrieveGraphs() {
        List<Graph> graphs = dbHandler.retrieveGraphs();
        return graphs;
    }

    public void createVertices(String graphId, ArrayList<Vertex> vertices) {
        try {
            dbHandler.insertVertices(graphId, vertices);
        } catch (Exception e) {

        }
    }

    public Vertex retrieveVertex (String graphId, String vertexId) {
        Vertex vertex = dbHandler.retrieveVertex(graphId, vertexId);
        return vertex;
    }

    public void updateVertices(String graphId, ArrayList<Vertex> vertices) {
        try {
            dbHandler.updateVertices(graphId, vertices);
        } catch (Exception e) {

        }
    }

    public void deleteVertex (String graphId, String vertexId) {
        dbHandler.deleteVertex(graphId, vertexId);
    }

    public void createEdges(String graphId, ArrayList<Edge> edges) {
        try {
            dbHandler.insertEdges(graphId, edges);
        } catch (Exception e) {

        }
    }

    public Edge retrieveEdge (String graphId, String edgeId) {
        Edge edge = dbHandler.retrieveEdge(graphId, edgeId);
        return edge;
    }

    public void updateEdges(String graphId, ArrayList<Edge> edges) {
        try {
            dbHandler.updateEdges(graphId, edges);
        } catch (Exception e) {

        }
    }

    public void deleteEdge (String graphId, String edgeId) {
        dbHandler.deleteEdge(graphId, edgeId);
    }
}

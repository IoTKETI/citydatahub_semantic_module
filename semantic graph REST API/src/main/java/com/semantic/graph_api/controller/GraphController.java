package com.semantic.graph_api.controller;

import com.semantic.graph_api.mapper.ArangoDBHandler;
import com.semantic.graph_api.mapper.FunctionMapper;
import com.semantic.graph_api.model.Edge;
import com.semantic.graph_api.model.Graph;
import com.semantic.graph_api.model.Vertex;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/graphs")
public class GraphController {

    ArangoDBHandler dbHandler = new ArangoDBHandler();
    FunctionMapper functionMapper = new FunctionMapper();

    // create graph
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    private void createGraph(
            @RequestBody Graph graph)
            throws Exception {

        functionMapper.createGraph(graph);
        System.out.println("'createGraph' has been called");
    }

    // retrieve graph with graph id
    @RequestMapping(value = "/{graphId}", method = RequestMethod.GET)
    private Graph selectGraph(
            @PathVariable String graphId)
            throws Exception {

        System.out.println("'selectGraph' has been called");

        return functionMapper.retrieveGraph(graphId);
    }

    // retrieve all graph
    @RequestMapping(value = "", method = RequestMethod.GET)
    private List<Graph> selectGraphs()
            throws Exception {

        System.out.println("'selectGraphs' has been called");

        return functionMapper.retrieveGraphs();
    }

    @RequestMapping(value = "/{graphId}/vertices", method = RequestMethod.POST)
    @ResponseBody
    private void createVertices(
            @PathVariable String graphId,
            @RequestBody ArrayList<Vertex> vertex)
            throws Exception {

        functionMapper.createVertices(graphId, vertex);
        System.out.println("'createVertices' has been called");
    }

    // retrieve graph with graph id
    @RequestMapping(value = "/{graphId}/vertices/{vertexId}", method = RequestMethod.GET)
    private Vertex selectVertex(
            @PathVariable String graphId,
            @PathVariable String vertexId)
            throws Exception {

        System.out.println("'selectVertex' has been called");

        return functionMapper.retrieveVertex(graphId, vertexId);
    }

    @RequestMapping(value = "/{graphId}/vertices", method = RequestMethod.PATCH)
    private void updateVertices(
            @PathVariable String graphId,
            @RequestBody ArrayList<Vertex> vertices)
            throws Exception {

        functionMapper.updateVertices(graphId, vertices);
        System.out.println("'updateVertices' has been called");
    }

    @RequestMapping(value = "/{graphId}/vertices/{vertexId}", method = RequestMethod.DELETE)
    private void deleteVertex(
            @PathVariable String graphId,
            @PathVariable String vertexId)
            throws Exception {

        functionMapper.deleteVertex(graphId, vertexId);
        System.out.println("'deleteVertex' has been called");
    }

    @RequestMapping(value = "/{graphId}/edges", method = RequestMethod.POST)
    @ResponseBody
    private void createEdges(
            @PathVariable String graphId,
            @RequestBody ArrayList<Edge> edges)
            throws Exception {

        functionMapper.createEdges(graphId, edges);
        System.out.println("'createEdges' has been called");
    }

    // retrieve edge with edge id
    @RequestMapping(value = "/{graphId}/edges/{edgeId}", method = RequestMethod.GET)
    private Edge selectEdge(
            @PathVariable String graphId,
            @PathVariable String edgeId)
            throws Exception {

        System.out.println("'selectEdge' has been called");

        return functionMapper.retrieveEdge(graphId, edgeId);
    }

    @RequestMapping(value = "/{graphId}/edges", method = RequestMethod.PATCH)
    private void updateEdges(
            @PathVariable String graphId,
            @RequestBody ArrayList<Edge> edges)
            throws Exception {

        functionMapper.updateEdges(graphId, edges);
        System.out.println("'updateEdges' has been called");
    }

    @RequestMapping(value = "/{graphId}/edges/{edgeId}", method = RequestMethod.DELETE)
    private void deleteEdge(
            @PathVariable String graphId,
            @PathVariable String edgeId)
            throws Exception {

        functionMapper.deleteEdge(graphId, edgeId);
        System.out.println("'deleteEdge' has been called");
    }

}

package com.semantic.graph_api.model;

import lombok.Data;

@Data
public class Edge {

    private String id;
    private String from;
    private String to;
    private Object properties;
    private String name;
}

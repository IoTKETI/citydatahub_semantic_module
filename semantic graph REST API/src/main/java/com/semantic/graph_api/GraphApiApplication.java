package com.semantic.graph_api;

import com.arangodb.ArangoDB;
import com.semantic.graph_api.mapper.ArangoDBHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GraphApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphApiApplication.class, args);

        ArangoDBHandler dbHandler = new ArangoDBHandler();


    }

}

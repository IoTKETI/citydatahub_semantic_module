package com.semantic.graph_adaptor;

import com.arangodb.ArangoDB;
import com.semantic.graph_adaptor.adaptor.DBConfiguration;
import com.semantic.graph_adaptor.controller.HttpController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GraphAdaptorApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphAdaptorApplication.class, args);

        DBConfiguration dbConfiguration = new DBConfiguration();
        ArangoDB arangoDB = dbConfiguration.dbConf();

        HttpController httpController = new HttpController();
        httpController.createSubscription();
        httpController.getEntities(arangoDB);
    }

}

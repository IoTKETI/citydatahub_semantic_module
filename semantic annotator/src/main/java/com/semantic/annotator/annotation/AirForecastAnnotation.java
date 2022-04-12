package com.semantic.annotator.annotation;

import com.semantic.annotator.correlationSeeker.AirQualityForecastSeeker;
import com.semantic.annotator.resource.AirQualityForecast;
import com.semantic.annotator.resourceDTO.AirQualityForecastDTO;
import com.semantic.annotator.validation.Validator;

import java.util.ArrayList;

public class AirForecastAnnotation {

    String graph_name = "http://www.city-hub.kr/ontologies/2019/1/air-quality#";
    String template = "\\src\\main\\java\\com\\semantic\\annotator\\template\\AirQualityEstimation.json";

    public AirForecastAnnotation(AirQualityForecast data, Validator validator) {

        AirQualityForecastSeeker airQualityForecastSeeker = new AirQualityForecastSeeker();
        AirQualityForecastDTO mappedDTO = airQualityForecastSeeker.map(data, AirQualityForecastDTO.class);

        ArrayList<String> hub_data = new ArrayList<>();
        for(int i=0; i<23; i++) {
            hub_data.add(mappedDTO.getOrder(i));
        }

        graph_name = graph_name + data.getId().split(":")[2] + "_" + data.getId().split(":")[3];
        String entity_id = data.getId().split(":")[2] + "_" + data.getId().split(":")[3];

        Annotator annotator = new Annotator(graph_name, template, entity_id, hub_data, validator);
    }
}

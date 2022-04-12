package com.semantic.annotator.annotation;

import com.semantic.annotator.correlationSeeker.WeatherObservedSeeker;
import com.semantic.annotator.resource.WeatherObserved;
import com.semantic.annotator.resourceDTO.WeatherObservedDTO;
import com.semantic.annotator.validation.Validator;

import java.util.ArrayList;

public class WeatherObservedAnnotation {

    String graph_name = "http://www.city-hub.kr/ontologies/2019/1/weather#";
    String template = "\\src\\main\\java\\com\\semantic\\Annotator\\template\\WeatherObserved.json";

    public WeatherObservedAnnotation(WeatherObserved data, Validator validator) {

        WeatherObservedSeeker weatherObservedSeeker = new WeatherObservedSeeker();
        WeatherObservedDTO mappedDTO = weatherObservedSeeker.map(data, WeatherObservedDTO.class);

        ArrayList<String> hub_data = new ArrayList<>();
        for(int i=0; i<19; i++) {
            hub_data.add(mappedDTO.getOrder(i));
        }

        graph_name = graph_name + data.getId().split(":")[2] + "_" + data.getId().split(":")[3];
        String entity_id = data.getId().split(":")[2] + "_" + data.getId().split(":")[3];

        Annotator annotator = new Annotator(graph_name, template, entity_id, hub_data, validator);

    }
}

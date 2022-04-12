package com.semantic.annotator.annotation;

import com.semantic.annotator.correlationSeeker.AirQualityObservedSeeker;
import com.semantic.annotator.resource.AirQualityObserved;
import com.semantic.annotator.resourceDTO.AirQualityObservedDTO;
import com.semantic.annotator.validation.Validator;

import java.util.ArrayList;

public class AirObservedAnnotation {

    String graph_name = "http://www.city-hub.kr/ontologies/2019/1/air-quality#";
    String template = "\\src\\main\\java\\com\\semantic\\annotator\\template\\AirQualityObserved.json";

    public AirObservedAnnotation(AirQualityObserved data, Validator validator) {

        AirQualityObservedSeeker seeker = new AirQualityObservedSeeker();
        AirQualityObservedDTO mappedDTO = seeker.map(data, AirQualityObservedDTO.class);

        ArrayList<String> hub_data = new ArrayList<>();
        for(int i=0; i<20; i++) {
            hub_data.add(mappedDTO.getOrder(i));
        }

        graph_name = graph_name + data.getId().split(":")[3];
        String entity_id = data.getId().split(":")[3];

        Annotator annotator = new Annotator(graph_name, template, entity_id, hub_data, validator);
    }
}

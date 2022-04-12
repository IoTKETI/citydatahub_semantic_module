package com.semantic.annotator.annotation;

import com.semantic.annotator.correlationSeeker.OffStreetParkingSeeker;
import com.semantic.annotator.resource.OffStreetParking;
import com.semantic.annotator.resourceDTO.OffStreetParkingDTO;
import com.semantic.annotator.validation.Validator;

import java.util.*;

public class OffStreetParkingAnnotation {

    String graph_name = "http://www.city-hub.kr/ontologies/2019/1/parking#";
    String template = "\\src\\main\\java\\com\\semantic\\Annotator\\template\\OffStreetParking.json";


    public OffStreetParkingAnnotation(OffStreetParking data, Validator validator) {

        OffStreetParkingSeeker correlationSeeker = new OffStreetParkingSeeker();
        OffStreetParkingDTO mappedOffStreetParkingDTO = correlationSeeker.map(data, OffStreetParkingDTO.class);
//        System.out.println(mappedOffStreetParkingDTO);

        ArrayList<String> hub_data = new ArrayList<>();
        for(int i=0; i<38; i++) {
            hub_data.add(mappedOffStreetParkingDTO.getOrder(i));
        }

        graph_name = graph_name + data.getId().split(":")[3];
        String entity_id = data.getId().split(":")[3];

        Annotator annotator = new Annotator(graph_name, template, entity_id, hub_data, validator);

    }
}

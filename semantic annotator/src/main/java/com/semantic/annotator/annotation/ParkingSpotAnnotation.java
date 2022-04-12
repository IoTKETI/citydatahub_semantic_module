package com.semantic.annotator.annotation;

import com.semantic.annotator.correlationSeeker.ParkingSpotSeeker;
import com.semantic.annotator.resource.ParkingSpot;
import com.semantic.annotator.resourceDTO.ParkingSpotDTO;
import com.semantic.annotator.validation.Validator;

import java.util.ArrayList;

public class ParkingSpotAnnotation {

    String graph_name = "http://www.city-hub.kr/ontologies/2019/1/parking#";
    String template = "\\src\\main\\java\\com\\semantic\\Annotator\\template\\ParkingSpot.json";

    public ParkingSpotAnnotation(ParkingSpot data, Validator validator) {

        ParkingSpotSeeker spotSeeker = new ParkingSpotSeeker();
        ParkingSpotDTO mappedParkingSpotDTO = spotSeeker.map(data, ParkingSpotDTO.class);
//        System.out.println(mappedParkingSpotDTO);

        ArrayList<String> hub_data = new ArrayList<>();
        for(int i=0; i<22; i++) {
            hub_data.add(mappedParkingSpotDTO.getOrder(i));
        }

        graph_name = graph_name + data.getRefParkingLot().getValue().split(":")[3] + "_" + data.getId().split(":")[3];
        String entity_id = data.getRefParkingLot().getValue().split(":")[3] + "_" + data.getId().split(":")[3];

        Annotator annotator = new Annotator(graph_name, template, entity_id, hub_data, validator);
    }
}

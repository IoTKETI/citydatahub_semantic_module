package com.semantic.annotator.correlationSeeker;

import com.semantic.annotator.resource.ParkingSpot;
import com.semantic.annotator.resourceDTO.ParkingSpotDTO;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class ParkingSpotSeeker {
    private MapperFactory mapperFactory;

    public ParkingSpotSeeker() {
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
        this.mapperFactory.classMap(ParkingSpot.class, ParkingSpotDTO.class).
                mapNulls(false).
                mapNullsInReverse(false).
                field("name.value", "name").
                field("location.value.type", "locationType").
                field("location.value.coordinates[0]", "longitude").
                field("location.value.coordinates[1]", "latitute").
                field("address.value.addressCountry", "addressCountry").
                field("address.value.addressRegion", "addressRegion").
                field("address.value.addressLocality", "addressLocality").
                field("address.value.streetAddress", "streetAddress").
                field("address.value.addressTown", "addressTown").
                field("category.value[0]","category").
                field("refParkingLot.value","refParkingLot").
                field("status.value", "status").
                field("width.value","width").
                field("length.value","length").
                byDefault().
                register();

        //CHECK
        //resource : type
        //DTO : addressName1~5
        //byDefault : id, name, createAt, modifiedAt, category, width, length, status, refParkingLot

    }

    public <S, D> D map(S s, Class<D> type) {
        return this.mapperFactory.getMapperFacade().map(s, type);
    }
}

package com.semantic.annotator.correlationSeeker;

import com.semantic.annotator.resource.OffStreetParking;
import com.semantic.annotator.resourceDTO.OffStreetParkingDTO;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class OffStreetParkingSeeker {

    private MapperFactory mapperFactory;

    public OffStreetParkingSeeker() {
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
        this.mapperFactory.classMap(OffStreetParking.class, OffStreetParkingDTO.class).
                mapNulls(false).
                mapNullsInReverse(false).
                byDefault().
                field("name.value", "name").
                field("locationTag.value", "locationTag").
                field("category.value[0]","category0").
                field("category.value[1]","category1").
                field("totalSpotNumber.value", "totalSpotNumber").
                field("availableSpotNumber.value", "availableSpotNumber").
                field("status.value[0]", "status0").
                field("status.value[1]", "status1").
                field("contactPoint.value.telephone", "telephone").
                field("contactPoint.value.email", "email").
                field("contactPoint.value.contactType", "contactType").
                field("paymentAccepted.value[0]","paymentType0").
                field("paymentAccepted.value[1]","paymentType1").
                field("priceRate.value", "priceRate").
                field("priceCurrency.value[0]", "priceCurrency").
                field("image.value","image").
                field("location.value.type","locationType").
                field("location.value.coordinates[0]","longitude").
                field("location.value.coordinates[1]","latitute").
                field("address.value.addressCountry", "addressCountry").
                field("address.value.addressRegion", "addressRegion").
                field("address.value.addressLocality", "addressLocality").
                field("address.value.streetAddress", "streetAddress").
                field("address.value.addressTown", "addressTown").
                field("congestionIndexPrediction.value.index","estimatedValue").
                field("congestionIndexPrediction.value.predictedAt[0]","predictedAt").
                register();
    }

    public <S, D> D map(S s, Class<D> type) {
        return this.mapperFactory.getMapperFacade().map(s, type);
    }
}

package com.semantic.annotator.correlationSeeker;

import com.semantic.annotator.resource.AirQualityObserved;
import com.semantic.annotator.resourceDTO.AirQualityObservedDTO;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class AirQualityObservedSeeker {

    private MapperFactory mapperFactory;

    public AirQualityObservedSeeker() {
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
        this.mapperFactory.classMap(AirQualityObserved.class, AirQualityObservedDTO.class).
                mapNulls(false).
                mapNullsInReverse(false).
                byDefault().
//                field("so2.value", "observedValue1.so2").
//                field("so2.value", "evaluationValue1.so2").
//                field("co.value", "observedValue1.co").
//                field("co.value", "evaluationValue1.co").
//                field("o3.value", "observedValue1.o3").
//                field("o3.value", "evaluationValue1.o3").
//                field("no2.value", "observedValue1.no2").
//                field("no2.value", "evaluationValue1.no2").
//                field("pm10.value", "observedValue").
                field("pm10.value", "evaluationValue").
//                field("pm25.value", "observedValue").
//                field("pm25.value", "evaluationValue1.pm25").
//                field("pm1.value", "observedValue1.pm1").
//                field("pm1.value", "evaluationValue1.pm1").
//                field("co2.value", "observedValue1.co2").
//                field("co2.value", "evaluationValue1.co2").
//                field("airQualityIndexObservation.value", "observedValue2").
                field("refDevice.object", "indexRef").
//                field("airQualityIndexObservation.value", "evaluationValue2").
                field("address.value.addressCountry", "addressCountry").
                field("address.value.addressRegion", "addressRegion").
                field("address.value.addressLocality", "addressLocality").
                field("address.value.streetAddress", "streetAddress").
                field("address.value.addressTown", "addressTown").
                field("location.value.type","locationType").
                field("location.value.coordinates[0]","longitude").
                field("location.value.coordinates[1]","latitude").
                field("pm10.observedAt","observedAt").
//                field("airQualityObservation.observedAt","observedAt1").
//                field("airQualityIndexObservation.observedAt","observedAt2").
                register();

    }

    public <S, D> D map(S s, Class<D> type)
    {
//        System.out.println("resource: "+s);
        return this.mapperFactory.getMapperFacade().map(s, type);
    }
}

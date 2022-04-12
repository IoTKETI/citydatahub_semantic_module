package com.semantic.annotator.correlationSeeker;

import com.semantic.annotator.resource.WeatherObserved;
import com.semantic.annotator.resourceDTO.WeatherObservedDTO;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class WeatherObservedSeeker {
    private MapperFactory mapperFactory;

    public WeatherObservedSeeker() {
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
        this.mapperFactory.classMap(WeatherObserved.class, WeatherObservedDTO.class).
                mapNulls(false).
                mapNullsInReverse(false).
                field("weatherObservation.value","weatherObservation").
                field("weatherObservation.value","weatherEvaluationHasRecord").
                field("address.value.addressCountry", "addressCountry").
                field("address.value.addressRegion", "addressRegion").
                field("address.value.addressLocality", "addressLocality").
                field("address.value.streetAddress", "streetAddress").
                field("address.value.addressTown", "addressTown").
                field("location.value.type", "locationType").
                field("location.value.coordinates[0]", "longitude").
                field("location.value.coordinates[1]", "latitute").
                field("weatherObservation.observedAt","observedAt").
                byDefault().
                register();

    }

    public <S, D> D map(S s, Class<D> type) {
        return this.mapperFactory.getMapperFacade().map(s, type);
    }
}

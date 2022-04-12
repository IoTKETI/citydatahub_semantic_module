package com.semantic.annotator.correlationSeeker;

import com.semantic.annotator.resource.AirQualityForecast;
import com.semantic.annotator.resourceDTO.AirQualityForecastDTO;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class AirQualityForecastSeeker {

    private MapperFactory mapperFactory;

    public AirQualityForecastSeeker() {
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
        this.mapperFactory.classMap(AirQualityForecast.class, AirQualityForecastDTO.class).
                mapNulls(false).
                mapNullsInReverse(false).
                byDefault().
                field("airQualityPrediction.value", "estimatedValue1").
                field("airQualityIndexPrediction.value", "estimatedValue2").
                field("airQualityPrediction.value", "evaluationValue1").
                field("airQualityIndexPrediction.value", "evaluationValue2").
                field("address.value.addressCountry", "addressCountry").
                field("address.value.addressRegion", "addressRegion").
                field("address.value.addressLocality", "addressLocality").
                field("address.value.streetAddress", "streetAddress").
                field("address.value.addressTown", "addressTown").
                field("location.value.type", "locationType").
                field("location.value.coordinates[0]", "longitude").
                field("location.value.coordinates[1]", "latitute").
                field("airQualityPrediction.observedAt","observedAt1").
                field("airQualityIndexPrediction.observedAt","observedAt2").
                register();

                // CHECK : mapping string type , estimatedValue1, estimatedValue2, evaluationValue1, evaluationValue2

    }

    public <S, D> D map(S s, Class<D> type) {
        return this.mapperFactory.getMapperFacade().map(s, type);
    }
}

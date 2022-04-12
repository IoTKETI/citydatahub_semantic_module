package com.semantic.annotator.resource;

public class AirQualityForecast {

    private String id;
    private String type;
    private Location location;
    private Address address;
    private String createdAt;
    private String modifiedAt;
    private IndexRef indexRef;
    private AirQualityPrediction airQualityPrediction;
    private AirQualityIndexPrediction airQualityIndexPrediction;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Location getLocation() {
        return location;
    }

    public Address getAddress() {
        return address;
    }

    public class AirQualityPrediction {
        private String observedAt;
        private String type;
        private Object value;
//        private AirQualityPredictionValue[] value;

        public Object getValue() {
            return value;
        }

        public String getObservedAt () {
            return observedAt;
        }

    }

    public AirQualityPrediction getAirQualityPrediction() {
        return airQualityPrediction;
    }

    public class AirQualityIndexPrediction {
        private String observedAt;
        private String type;
//        private AirQualityIndexPredictionValue[] value;
        private Object value;

        public Object getValue() {
            return value;
        }

        public String getObservedAt() {
            return observedAt;
        }


    }

    public AirQualityIndexPrediction getAirQualityIndexPrediction() {
        return airQualityIndexPrediction;
    }

    public class IndexRef {
        private String type;
        private String value;

        public Object getValue() {
            return value;
        }
    }

    public IndexRef getIndexRef() {
        return indexRef;
    }

}

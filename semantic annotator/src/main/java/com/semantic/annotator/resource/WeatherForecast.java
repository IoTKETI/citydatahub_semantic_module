package com.semantic.annotator.resource;

public class WeatherForecast {

    private String id;
    private String type;
    private Location location;
    private Address address;
    private String createdAt;
    private String modifiedAt;
    private WeatherPrediction weatherPrediction;

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

    public class WeatherPrediction {
        private String observedAt;
        private String type;
        private Object value;

        public String getObservedAt () {
            return observedAt;
        }

        public Object getValue() {
            return value;
        }
    }

    public WeatherPrediction getWeatherPrediction() {
        return weatherPrediction;
    }

}

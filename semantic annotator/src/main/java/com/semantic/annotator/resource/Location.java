package com.semantic.annotator.resource;

public class Location {
    //GeoProperty
    private String type;
    private LocationValue value;

    public class LocationValue {
        //Point, MultiPolygon
        private String type;
        private String coordinates[];

        public String[] getCoordinates() {
            return coordinates;
        }

        public String getType() {
            return type;
        }
    }

    public String getType() {
        return type;
    }

    public LocationValue getValue() {
        return value;
    }
}

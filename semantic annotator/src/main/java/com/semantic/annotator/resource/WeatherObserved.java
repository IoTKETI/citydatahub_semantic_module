package com.semantic.annotator.resource;

public class WeatherObserved {

    private String id;
    private String type;
    private String createdAt;
    private String modifiedAt;
    private Location location;
    private Address address;
    private WeatherObservation weatherObservation;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public Location getLocation() {
        return location;
    }

    public Address getAddress() {
        return address;
    }

    public static class WeatherObservation {
        private String type;
        private String observedAt;
//        private WeatherObservationValue value;
//        private String weatherObservation;
//        private String weatherEvaluationHasRecord;
        private Object value;

        public Object getValue() {
            return value;
        }

//        public String getType() {
//            return type;
//        }

        public String getObservedAt() {
            return observedAt;
        }

//        private class WeatherObservationValue {
//            private Number temperature;
//            private Number windSpeed;
//            private Number humidity;
//            private Number rainfall;
//            private Number hourlyRainfall;
//            private String rainType;
//            private Number snowfall;
//            private Number visibility;
//
//            public Number getTemperature() {
//                return temperature;
//            }
//
//            public Number getWindSpeed() {
//                return windSpeed;
//            }
//
//            public Number getHumidity() {
//                return humidity;
//            }
//
//            public Number getRainfall() {
//                return rainfall;
//            }
//
//            public Number getHourlyRainfall() {
//                return hourlyRainfall;
//            }
//
//            public String getRainType() {
//                return rainType;
//            }
//
//            public Number getSnowfall() {
//                return snowfall;
//            }
//
//            public Number getVisibility() {
//                return visibility;
//            }
//        }
//        public WeatherObservationValue getValue() {
//            return value;
//        }
//        public String getWeatherObservation() {
//            return weatherObservation;
//        }
//        public String getWeatherEvaluationHasRecord() {
//            return weatherEvaluationHasRecord;
//        }
    }

    public WeatherObservation getWeatherObservation() {
        return weatherObservation;
    }
}

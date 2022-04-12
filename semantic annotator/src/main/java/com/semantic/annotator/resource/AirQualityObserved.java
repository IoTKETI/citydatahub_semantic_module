package com.semantic.annotator.resource;
import com.google.gson.JsonElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.Map;

@Data
public class AirQualityObserved {

    private String id;
    private String type;
    private Location location;
    private Address address;
    private String createdAt;
    private String modifiedAt;
    private Source source;
    private RefDevice refDevice;
    private So2 so2;
    private Co co;
    private O3 o3;
    private No2 no2;
    private Pm10 pm10;
    private Pm25 pm25;
    private Pm1 pm1;
    private Co2 co2;
    private AirQualityIndex airQualityIndex;
    private String observedValue;
//    private IndexRef indexRef;
//    private AirQualityObservation airQualityObservation;
//    private AirQualityIndexObservation airQualityIndexObservation;

    @Data
    public class Source {
        private String type;
        private String value;
    }

    @Data
    public class RefDevice {
        private String type;
        private String object;
    }

    @Data
    public class So2 {
        private String observedAt;
        private String type;
        private Double value;
    }

    @Data
    public class Co {
        private String observedAt;
        private String type;
        private Double value;
    }

    @Data
    public class O3 {
        private String observedAt;
        private String type;
        private Double value;
    }

    @Data
    public class No2 {
        private String observedAt;
        private String type;
        private Double value;
    }

    @Data
    public class Pm10 {
        private String observedAt;
        private String type;
        private String value;
    }

    @Data
    public class Pm25 {
        private String observedAt;
        private String type;
        private Integer value;
    }

    @Data
    public class Pm1 {
        private String observedAt;
        private String type;
        private Integer value;
    }

    @Data
    public class Co2 {
        private String observedAt;
        private String type;
        private Double value;
    }

    @Data
    public class AirQualityIndex {
        private String observedAt;
        private String type;
        private Integer value;
    }

    public String getObservedValue() {

//        ObservedValue hub_data;
//        hub_data.pm10 = pm10.getValue();
        ArrayList<Object> hub_data = new ArrayList<>();
        if (so2!=null)
            hub_data.add("\"so2\": " + so2.getValue());
        if (co!=null)
            hub_data.add("\"so2\": " + co.getValue());
        if (o3!=null)
            hub_data.add("\"so2\": " + o3.getValue());
        if (no2!=null)
            hub_data.add("\"so2\": " + no2.getValue());
        if (pm10!=null)
            hub_data.add("\"pm10\": " + pm10.getValue());
        if (pm25!=null)
            hub_data.add("\"pm25\": " + pm25.getValue());
        if (pm1!=null)
            hub_data.add("\"pm10\": " + pm1.getValue());
        if (co2!=null)
            hub_data.add("\"so2\": " + co2.getValue());
//        return "[\"so2\": " + pm25.getValue() + ", pm10\": " + pm10.getValue() + ", ";
        return hub_data.toString();
    }


    public class ObservedValue {
        //        private Double so2;
//        private Double co;
//        private Double o3;
//        private Double no2;
        private String pm10;
//        private Integer pm25;
//        private Integer pm1;
//        private Double co2;
    }

////    public String getId() {
////        return id;
////    }
////
////    public String getType() {
////        return type;
////    }
////
////    public String getModifiedAt() {
////        return modifiedAt;
////    }
////
////    public String getCreatedAt() {
////        return createdAt;
////    }
////
////    public Location getLocation() {
////        return location;
////    }
////
////    public Address getAddress() {
////        return address;
////    }
//
//    public class AirQualityObservation {
//        private String observedAt;
//        private String type;
//        private Object value;
////        private AirQualityObservationValue value;
////        private String airQualityObservation;
////
////        public String getObservedAt () {
////            return observedAt;
////        }
////
////        public Object getValue() {
////            return value;
////        }
//        //        public class AirQualityObservationValue {
////            private Number no2;
////            private Number o3;
////            private Number pm25;
////            private Number so2;
////            private Number pm10;
////
////            private Number co;
////
////            public Number getNO2() {
////                return no2;
////            }
////
////            public Number getO3() {
////                return o3;
////            }
////
////            public Number getPM25() {
////                return pm25;
////            }
////
////            public Number getSO2() {
////                return so2;
////            }
////
////            public Number getPM10() {
////                return pm10;
////            }
////            public Number getCO() {
////                return co;
////            }
////
////        }
////
////        public AirQualityObservationValue getValue() {
////            return value;
////        }
////
////        public String getAirQualityObservation() {
////            return airQualityObservation;
////        }
//    }
//
////    public AirQualityObservation getAirQualityObservation() {
////        return airQualityObservation;
////    }
//
//    public class AirQualityIndexObservation {
//        private String observedAt;
//        private String type;
//        private Object value;
////        private AirQualityIndexObservationValue value;
////        private String airQualityIndexObservation;
////
////        public String getObservedAt() {
////            return observedAt;
////        }
////
////        public Object getValue() {
////            return value;
////        }
////
//        //        public class AirQualityIndexObservationValue {
////            private Number totalIndex;
////            private String totalCategory;
////            private String pm10Category;
////            private String o3Category;
////            private String pm25Category;
////            private String no2Category;
////            private String coCategory;
////            private String so2Category;
////
////            public Number getTotalIndex() {
////                return totalIndex;
////            }
////
////            public String getTotalCategory() {
////                return totalCategory;
////            }
////
////            public String getPM10Category() {
////                return pm10Category;
////            }
////
////            public String getO3Category() {
////                return o3Category;
////            }
////
////            public String getPM25Category() {
////                return pm25Category;
////            }
////
////            public String getNO2Category() {
////                return no2Category;
////            }
////
////            public String getCOCategory() {
////                return coCategory;
////            }
////
////            public String getSO2Category() {
////                return so2Category;
////            }
////
////        }
////
////        public AirQualityIndexObservationValue getValue() {
////            return value;
////        }
////
////        public String getAirQualityIndexObservation() {
////            return airQualityIndexObservation;
////        }
//    }
//
////    public AirQualityIndexObservation getAirQualityIndexObservation() {
////        return airQualityIndexObservation;
////    }
////
////    public class IndexRef {
////        private String type;
////        private String value;
////
////        public Object getValue() {
////            return value;
////        }
////    }
////
////    public IndexRef getIndexRef() {
////        return indexRef;
////    }
}

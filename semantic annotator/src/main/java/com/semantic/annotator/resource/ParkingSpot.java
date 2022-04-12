package com.semantic.annotator.resource;

public class ParkingSpot {

    private String id;
    private String type;
    private String createdAt;
    private String modifiedAt;
    private Name name;
    private Location location;
    private Address address;
    private Category category;
    private Width width;
    private Length length;
    private Status status;
    private RefParkingLot refParkingLot;

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

    public class Name {
        private String type;
        private String value;

        public String getValue() {
            return value;
        }
    }

    public Name getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public Address getAddress() {
        return address;
    }

    public class Category {
        private String type;
        private Object[] value;

        public Object[] getValue() {
            return value;
        }

    }

    public Category getCategory() {
        return category;
    }

    public class Width {
        private String type;
        private String value;

//        public String getType() {
//            return type;
//        }

        public String getValue() {
            return value;
        }
    }

    public Width getWidth() {
        return width;
    }

    public class Length {
        private String type;
        private String value;

//        public String getType() {
//            return type;
//        }

        public String getValue() {
            return value;
        }
    }

    public Length getLength() {
        return length;
    }

    public class Status {
        private String type;
        private String value;
//        private String observedAt;

//        public String getType() {
//            return type;
//        }

        public String getValue() {
            return value;
        }

//        public String getObservedAt() {
//            return observedAt;
//        }
    }

    public Status getStatus(){ return status; }

    public class RefParkingLot {
        private String type;
        private String value;

//        public String getType() {
//            return type;
//        }

        public String getValue() {
            return value;
        }
    }

    public RefParkingLot getRefParkingLot() {
        return refParkingLot;
    }
}


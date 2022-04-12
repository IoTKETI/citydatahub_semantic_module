package com.semantic.annotator.resource;

public class Address {
    private String type;
    private AddressValue value;

    public class AddressValue {
        private String addressCountry;
        private String addressRegion;
        private String addressLocality;
        private String streetAddress;
        private String addressTown;

        public String getAddressCountry() {
            return addressCountry;
        }

        public String getAddressRegion() {
            return addressRegion;
        }

        public String getAddressLocality() {
            return addressLocality;
        }

        public String getStreetAddress() {
            return streetAddress;
        }

        public String getAddressTown() {
            return addressTown;
        }
    }

    public String getType()  {
        return type;

    }

    public AddressValue getValue() {
        return value;
    }

//    public String getAddressCountry(){
//        return value.addressCountry;
//    }
//
//    public String getAddressRegion() {
//        return value.addressRegion;
//    }
//
//    public String getAddressLocality() {
//        return value.addressLocality;
//    }
//
//    public String getStreetAddress() {
//        return value.streetAddress;
//    }
//
//    public String getAddressTown() {
//        return value.addressTown;
//    }
}

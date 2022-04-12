package com.semantic.annotator.resourceDTO;

public class ParkingSpotDTO {
    private String id;
    private String name;
    private String createdAt;
    private String modifiedAt;

    private String locationType;
    private String latitute;
    private String longitude;

    private String addressName1 = "addressCountry";
    private String addressCountry;      //getAddress().getValue().getAddressCountry()
    private String addressName2 = "addressRegion";
    private String addressRegion;       //getAddress().getValue().getAddressRegion()
    private String addressName3 = "addressLocality";
    private String addressLocality;     //getAddress().getValue().getAddressLocality()
    private String addressName4 = "streetAddress";
    private String streetAddress;       //getAddress().getValue().getStreetAddress()
    private String addressName5 = "addressTown";
    private String addressTown;     //getAddress().getValue().getAddressTown()

    private String category;
    private String refParkingLot;
    private String status;
    private String width;
    private String length;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setcreatedAt(String createdAt) {
        this.createdAt = createdAt.split(",")[0]+"+09:00";
    }

    public void setmodifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt.split(",")[0]+"+09:00";
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public void setLatitute(String latitute) {
        this.latitute = latitute;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setAddressName1(String addressName1) {
        this.addressName1 = addressName1;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public void setAddressName2(String addressName2) {
        this.addressName2 = addressName2;
    }

    public void setAddressRegion(String addressRegion) {
        this.addressRegion = addressRegion;
    }

    public void setAddressName3(String addressName3) {
        this.addressName3 = addressName3;
    }

    public void setAddressLocality(String addressLocality) {
        this.addressLocality = addressLocality;
    }

    public void setAddressName4(String addressName4) {
        this.addressName4 = addressName4;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setAddressName5(String addressName5) {
        this.addressName5 = addressName5;
    }

    public void setAddressTown(String addressTown) {
        this.addressTown = addressTown;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRefParkingLot(String refParkingLot) {
        this.refParkingLot = refParkingLot;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getOrder(int i) {
        String result = "";
        switch (i) {
            case 0: result = id; break;
            case 1: result = name; break;
            case 2: result = createdAt; break;
            case 3: result = modifiedAt; break;
            case 4: result = locationType; break;
            case 5: result = latitute; break;
            case 6: result = longitude; break;
            case 7: result = addressName1; break;
            case 8: result = addressCountry; break;
            case 9: result = addressName2; break;
            case 10: result = addressRegion; break;
            case 11: result = addressName3; break;
            case 12: result = addressLocality; break;
            case 13: result = addressName4; break;
            case 14: result = streetAddress; break;
            case 15: result = addressName5; break;
            case 16: result = addressTown; break;
            case 17: result = category; break;
            case 18: result = refParkingLot; break;
            case 19: result = status; break;
            case 20: result = width; break;
            case 21: result = length; break;
        }
        return result;
    }
}

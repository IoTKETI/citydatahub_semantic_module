package com.semantic.annotator.resourceDTO;

public class OffStreetParkingDTO {

    private String id;
    private String name;
    private String locationTag;
    private String category0;
    private String category1;
    private String  totalSpotNumber;
    private String availableSpotNumber;
    private String status0;
    private String status1;
    private String telephone;
    private String email;
    private String contactType;
    private String paymentType0;
    private String paymentType1;
    private String priceRate;
    private String priceCurrency;
    private String image;
    private String locationType;
    private String latitute;
    private String longitude;
    private String locationName1 = "addressCountry";
    private String addressCountry;
    private String locationName2 = "addressRegion";
    private String addressRegion;
    private String locationName3 = "addressLocality";
    private String addressLocality;
    private String locationName4 = "streetAddress";
    private String streetAddress;
    private String locationName5 = "addressTown";
    private String addressTown;
    private String createdAt;
    private String modifiedAt;
    private String openingHours = "1111-11-11T00:00:00";
    private String endingHours = "1111-11-11T23:59:59";
    private String openingType = "OpeningHours";
    private String endingType = "OpeningHours";
    private String estimatedValue;    //getCongestionIndexPrediction().getValue().getIndex().toString()
    private String predictedAt;     //getCongestionIndexPrediction().getValue().getPredictedAt(0).toString().split(",")[0]+"+09:00"

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocationTag(String locationTag) {
        this.locationTag = locationTag;
    }

    public void setCategory0(String category0) {
        this.category0 = category0;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public void setTotalSpotNumber(String  totalSpotNumber) {
        this.totalSpotNumber = totalSpotNumber;
    }

    public void setAvailableSpotNumber(String availableSpotNumber) {
        this.availableSpotNumber = availableSpotNumber;
    }

    public void setStatus0(String status0) {
        this.status0 = status0;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public void setPaymentType0(String paymentType0) {
        this.paymentType0 = paymentType0;
    }

    public void setPaymentType1(String paymentType1) {
        this.paymentType1 = paymentType1;
    }

    public void setPriceRate(String priceRate) {
        this.priceRate = priceRate;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public void setImage(String image) {
        this.image = image;
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

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public void setAddressRegion(String addressRegion) {
        this.addressRegion = addressRegion;
    }

    public void setAddressLocality(String addressLocality) {
        this.addressLocality = addressLocality;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setAddressTown(String addressTown) {
        this.addressTown = addressTown;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt.split(",")[0]+"+09:00";
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt.split(",")[0]+"+09:00";
    }

    public void setEstimatedValue(String estimatedValue) {
        this.estimatedValue = estimatedValue.toString();
    }

    public void setPredictedAt(String predictedAt) {
        this.predictedAt = predictedAt.split(",")[0]+"+09:00";
    }

    public String getOrder(int i) {
        String result = "";
        switch (i) {
            case 0: result = id; break;
            case 1: result = name; break;
            case 2: result = locationTag; break;
            case 3: result = category0; break;
            case 4: result = category1; break;
            case 5: result = totalSpotNumber; break;
            case 6: result = availableSpotNumber; break;
            case 7: result = status0; break;
            case 8: result = status1; break;
            case 9: result = telephone; break;
            case 10: result = email; break;
            case 11: result = contactType; break;
            case 12: result = paymentType0; break;
            case 13: result = paymentType1; break;
            case 14: result = priceRate; break;
            case 15: result = priceCurrency; break;
            case 16: result = image; break;
            case 17: result = locationType; break;
            case 18: result = latitute; break;
            case 19: result = longitude; break;
            case 20: result = locationName1; break;
            case 21: result = addressCountry; break;
            case 22: result = locationName2; break;
            case 23: result = addressRegion; break;
            case 24: result = locationName3; break;
            case 25: result = addressLocality; break;
            case 26: result = locationName4; break;
            case 27: result = streetAddress; break;
            case 28: result = locationName5; break;
            case 29: result = addressTown; break;
            case 30: result = createdAt; break;
            case 31: result = modifiedAt; break;
            case 32: result = openingHours; break;
            case 33: result = endingHours; break;
            case 34: result = openingType; break;
            case 35: result = endingType; break;
            case 36: result = estimatedValue; break;
            case 37: result = predictedAt; break;
        }
        return result;
    }
}

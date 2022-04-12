package com.semantic.annotator.resourceDTO;

public class WeatherForecastDTO {

    private String id;                          //getId()   
    private String estimatedValue;              //getWeatherPrediction().getValue(0)
    private String evaluationValue;             //getWeatherPrediction().getValue(0)
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
    private String locationType;               
    private String latitute;
    private String longitude;
    private String modifiedAt;
    private String createdAt; 
    private String observedAt;                 //getWeatherPrediction().getObservedAt()

    public void setId(String id) {
        this.id = id;
    }

    public void setEstimatedValue(String estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public void setEvaluationValue(String evaluationValue) {
        this.evaluationValue = evaluationValue;
    }

    public void setLocationName1(String locationName1) {
        this.locationName1 = locationName1;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public void setLocationName2(String locationName2) {
        this.locationName2 = locationName2;
    }

    public void setAddressRegion(String addressRegion) {
        this.addressRegion = addressRegion;
    }

    public void setLocationName3(String locationName3) {
        this.locationName3 = locationName3;
    }
    
    public void setAddressLocality(String addressLocality) {
        this.addressLocality = addressLocality;
    }

    public void setLocationName4(String locationName4) {
        this.locationName4 = locationName4;
    }
    
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setLocationName5(String locationName5) {
        this.locationName5 = locationName5;
    }
    
    public void setAddressTown(String addressTown) {
        this.addressTown = addressTown;
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

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt.split(",")[0]+"+09:00";
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt.split(",")[0]+"+09:00";
    }

    public void setObservedAt(String observedAt) {
        this.observedAt = observedAt.split(",")[0]+"+09:00";
    }

    public String getOrder(int i) {
        String result = "";
        switch (i) {
            case 0: result = id; break;
            case 1: result = estimatedValue; break;
            case 2: result = evaluationValue; break;
            case 3: result = locationName1; break;
            case 4: result = addressCountry; break;
            case 5: result = locationName2; break;
            case 6: result = addressRegion; break;
            case 7: result = locationName3; break;
            case 8: result = addressLocality; break;
            case 9: result = locationName4; break;
            case 10: result = streetAddress; break;
            case 11: result = locationName5; break;
            case 12: result = addressTown; break;
            case 13: result = locationType; break;
            case 14: result = latitute; break;
            case 15: result = longitude; break;
            case 16: result = modifiedAt; break;
            case 17: result = createdAt; break;
            case 18: result = observedAt; break;
        }
        return result;
    }
}

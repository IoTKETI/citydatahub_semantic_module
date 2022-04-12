package com.semantic.annotator.controller;

import com.google.gson.*;
import com.semantic.annotator.annotation.*;
import com.semantic.annotator.configuration.Configuration;
import com.semantic.annotator.resource.*;
import com.semantic.annotator.validation.Validator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.List;

@Controller
@Component
public class HttpController {

    OffStreetParking[] offStreetParkings;
    List<OffStreetParking> offStreetParkingList;
    ParkingSpot[] parkingSpots;
    List<ParkingSpot> parkingSpotList;
    AirQualityObserved[] airQualityObserveds;
    List<AirQualityObserved> airQualityObservedList;
    AirQualityForecast[] airQualityForecasts;
    List<AirQualityForecast> airQualityForecastList;
    WeatherObserved[] weatherObserveds;
    List<WeatherObserved> weatherObservedList;
    WeatherForecast[] weatherForecasts;
    List<WeatherForecast> weatherForecastList;

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    Validator validator = new Validator();

    public void getEntities() {

        List<String> type = new ArrayList<>();
        type.add("/entities?type=" + Configuration.OFF_STREET_PARKING_TYPE);
        type.add("/entities?type=" + Configuration.PARKING_SPOT_TYPE);
        type.add("/entities?type=" + Configuration.AIR_QUALITY_OBSERVED_TYPE);
        type.add("/entities?type=" + Configuration.AIR_QUALITY_FORECAST_TYPE);
        type.add("/entities?type=" + Configuration.WEATHER_OBSERVED_TYPE);
        type.add("/entities?type=" + Configuration.WEATHER_FORECAST_TYPE);

        headers.set("accept", "application/ld+json");
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        for(int i = 0; i < type.size(); i++ ) {
            String result = restTemplate.exchange(Configuration.DATA_CORE_URL+type.get(i), HttpMethod.GET, entity, String.class).getBody();

            switch (i) {
                case 0:
                    offStreetParkings = gson.fromJson(result, OffStreetParking[].class);
                    offStreetParkingList = Arrays.asList(offStreetParkings);
                    break;
                case 1:
                    parkingSpots = gson.fromJson(result, ParkingSpot[].class);
                    parkingSpotList = Arrays.asList(parkingSpots);
                    break;
                case 2:
                    airQualityObserveds = gson.fromJson(result, AirQualityObserved[].class);
                    airQualityObservedList = Arrays.asList(airQualityObserveds);
                    break;
                case 3:
                    airQualityForecasts = gson.fromJson(result, AirQualityForecast[].class);
                    airQualityForecastList = Arrays.asList(airQualityForecasts);
                    break;
                case 4:
                    weatherObserveds = gson.fromJson(result, WeatherObserved[].class);
                    weatherObservedList = Arrays.asList(weatherObserveds);
                    break;
                case 5:
                    weatherForecasts = gson.fromJson(result, WeatherForecast[].class);
                    weatherForecastList = Arrays.asList(weatherForecasts);
            }
        }

        for(int i = 0; i< offStreetParkingList.size(); i++) {
            OffStreetParkingAnnotation parkingAnnotation = new OffStreetParkingAnnotation(offStreetParkingList.get(i), validator);
        }

        for(int i = 0; i<parkingSpotList.size(); i++) {
            ParkingSpotAnnotation parkingSpotAnnotation = new ParkingSpotAnnotation(parkingSpotList.get(i), validator);
        }

        for(int i = 0; i<airQualityObservedList.size(); i++) {
            AirObservedAnnotation airObservedAnnotation = new AirObservedAnnotation(airQualityObservedList.get(i), validator);
        }

        for(int i = 0; i<airQualityForecastList.size(); i++) {
            AirForecastAnnotation airForecastAnnotation = new AirForecastAnnotation(airQualityForecastList.get(i), validator);
        }

        for (int i = 0; i<weatherObservedList.size(); i++) {
            WeatherObservedAnnotation weatherObservedAnnotation = new WeatherObservedAnnotation(weatherObservedList.get(i), validator);
        }

        for(int i = 0; i<weatherForecastList.size(); i++) {
            WeatherForecastAnnotation weatherForecastAnnotation = new WeatherForecastAnnotation(weatherForecastList.get(i), validator);
        }

    }

    public void createGraph(JsonObject insertBody) {

        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(insertBody.toString(), headers);
        restTemplate.exchange(Configuration.SEMANTIC_API_URL+"/insert", HttpMethod.POST, entity, String.class);

    }

    public void deleteGraph(String graphURI) {

        headers.set("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<>(graphURI, headers);
        restTemplate.exchange(Configuration.SEMANTIC_API_URL+"/delete?graphURI={id}", HttpMethod.DELETE, entity, String.class, graphURI);

    }

    public void createSubscription() {

        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/ld+json");

        String subscripntion = "\\src\\main\\java\\com\\semantic\\Annotator\\controller\\SubscriptionList.json";

        FileReader reader = null;
        List<String> subList = null;

        try {
            reader = new FileReader(System.getProperty("user.dir") + subscripntion);
        } catch (FileNotFoundException e) {

        }

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(reader);
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        for (int i = 0; i<jsonArray.size(); i++) {
            HttpEntity<String> entity = new HttpEntity<String>(jsonArray.get(i).toString(), headers);
            String result = restTemplate.exchange(Configuration.DATA_CORE_URL+"/subscriptions", HttpMethod.POST, entity, String.class).getBody();
            System.out.println("create"+result);
        }

    }

    @RequestMapping(value = "/notify")
    public String notification(@RequestBody String requestbody) {

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(requestbody).getAsJsonObject();
        JsonObject jsonObject = (JsonObject)jsonElement;
        JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();

        if(jsonObject.get("subscriptionId").toString().split(":")[3].equals("OffStreetParking\"")) {

            offStreetParkings = gson.fromJson(jsonArray, OffStreetParking[].class);
            offStreetParkingList = Arrays.asList(offStreetParkings);
            OffStreetParkingAnnotation parkingAnnotation = new OffStreetParkingAnnotation(offStreetParkingList.get(0), validator);

        } else if(jsonObject.get("subscriptionId").toString().split(":")[3].equals("ParkingSpot\"")) {

            parkingSpots = gson.fromJson(jsonArray, ParkingSpot[].class);
            parkingSpotList = Arrays.asList(parkingSpots);
            ParkingSpotAnnotation parkingSpotAnnotation = new ParkingSpotAnnotation(parkingSpotList.get(0), validator);

        } else if(jsonObject.get("subscriptionId").toString().split(":")[3].equals("AirQualityObserved\"")) {

            airQualityObserveds = gson.fromJson(jsonArray, AirQualityObserved[].class);
            airQualityObservedList = Arrays.asList(airQualityObserveds);
            AirObservedAnnotation airObservedAnnotation = new AirObservedAnnotation(airQualityObservedList.get(0), validator);

        } else if(jsonObject.get("subscriptionId").toString().split(":")[3].equals("AirQualityForecast\"")) {

            airQualityForecasts = gson.fromJson(jsonArray, AirQualityForecast[].class);
            airQualityForecastList = Arrays.asList(airQualityForecasts);
            AirForecastAnnotation airForecastAnnotation = new AirForecastAnnotation(airQualityForecastList.get(0), validator);

        } else if(jsonObject.get("subscriptionId").toString().split(":")[3].equals("WeatherObserved\"")) {

            weatherObserveds = gson.fromJson(jsonArray, WeatherObserved[].class);
            weatherObservedList = Arrays.asList(weatherObserveds);
            WeatherObservedAnnotation weatherObservedAnnotation = new WeatherObservedAnnotation(weatherObservedList.get(0), validator);

        } else if(jsonObject.get("subscriptionId").toString().split(":")[3].equals("WeatherForecast\"")) {

            weatherForecasts = gson.fromJson(jsonArray, WeatherForecast[].class);
            weatherForecastList = Arrays.asList(weatherForecasts);
            WeatherForecastAnnotation weatherForecastAnnotation = new WeatherForecastAnnotation(weatherForecastList.get(0), validator);

        }

        return "";
    }
}

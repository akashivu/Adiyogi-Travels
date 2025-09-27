package com.example.Adiyogi_Travels.service;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GoogleMapsService {
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${google.api.key}")
    private String apiKey;

    public int getDistance(String pickup, String dropoff) {
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="
                + pickup + "&destinations=" + dropoff + "&key=" + apiKey;

        Map response = restTemplate.getForObject(url, Map.class);
        List rows = (List) response.get("rows");
        Map elements = (Map) ((List) ((Map) rows.get(0)).get("elements")).get(0);
        Map distance = (Map) elements.get("distance");

        return ((Number) distance.get("value")).intValue() / 1000;
    }
}

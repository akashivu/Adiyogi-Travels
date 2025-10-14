package com.example.Adiyogi_Travels.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class GoogleMapsService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.api.key}")
    private String apiKey;

    public int getDistance(String pickup, String dropoff) {
        try {

            if (pickup == null || dropoff == null || pickup.isBlank() || dropoff.isBlank()) {
                System.err.println("⚠️ Invalid pickup/drop: pickup=" + pickup + ", dropoff=" + dropoff);
                return 0;
            }


            String url = String.format(
                    "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&key=%s",
                    URLEncoder.encode(pickup, StandardCharsets.UTF_8),
                    URLEncoder.encode(dropoff, StandardCharsets.UTF_8),
                    apiKey
            );


            Map response = restTemplate.getForObject(url, Map.class);
            if (response == null) {
                System.err.println("⚠️ Google API returned null response");
                return 0;
            }


            Object status = response.get("status");
            if (status == null || !"OK".equals(status.toString())) {
                System.err.println("⚠️ Google API status not OK: " + status);
                return 0;
            }


            List rows = (List) response.get("rows");
            if (rows == null || rows.isEmpty()) {
                System.err.println("⚠️ No rows found in Google API response");
                return 0;
            }

            Map firstRow = (Map) rows.get(0);
            List elements = (List) firstRow.get("elements");
            if (elements == null || elements.isEmpty()) {
                System.err.println("⚠️ No elements found in Google API response");
                return 0;
            }

            Map firstElement = (Map) elements.get(0);
            String elementStatus = (String) firstElement.get("status");
            if (!"OK".equals(elementStatus)) {
                System.err.println("⚠️ Element status not OK: " + elementStatus);
                return 0;
            }

            Map distance = (Map) firstElement.get("distance");
            if (distance == null || distance.get("value") == null) {
                System.err.println("⚠️ Distance missing in Google API response");
                return 0;
            }

           
            int meters = ((Number) distance.get("value")).intValue();
            int km = (int) Math.ceil(meters / 1000.0);

            System.out.println("✅ Distance calculated: " + km + " km (" + pickup + " → " + dropoff + ")");
            return km;

        } catch (Exception e) {
            System.err.println("❌ Error getting distance: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}

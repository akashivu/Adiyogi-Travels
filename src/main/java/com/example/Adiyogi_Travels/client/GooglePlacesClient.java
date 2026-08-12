package com.example.Adiyogi_Travels.client;

import com.example.Adiyogi_Travels.dto.google.GooglePlace;
import com.example.Adiyogi_Travels.dto.google.GooglePlaceSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.example.Adiyogi_Travels.dto.google.GooglePlaceDetails;
import java.util.List;
import java.util.Map;

@Component
public class GooglePlacesClient {

    private final RestClient restClient;
    private final String apiKey;

    public GooglePlacesClient(
            RestClient googlePlacesRestClient,
            @Value("${google.api.key}") String apiKey
    ) {
        this.restClient = googlePlacesRestClient;
        this.apiKey = apiKey;
    }

    public GooglePlace searchDestination(
            String destinationName,
            String country
    ) {

        String textQuery =
                destinationName + ", " + country;

        Map<String, Object> requestBody = Map.of(
                "textQuery", textQuery,
                "pageSize", 1
        );

        GooglePlaceSearchResponse response =
                restClient.post()
                        .uri("/places:searchText")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(
                                "X-Goog-Api-Key",
                                apiKey
                        )
                        .header(
                                "X-Goog-FieldMask",
                                "places.id,places.displayName,places.formattedAddress"
                        )
                        .body(requestBody)
                        .retrieve()
                        .body(GooglePlaceSearchResponse.class);

        if (response == null ||
                response.places() == null ||
                response.places().isEmpty()) {

            return null;
        }

        return response.places().get(0);
    }
    public GooglePlaceDetails getPlaceDetails(
            String googlePlaceId
    ) {

        return restClient.get()
                .uri("/places/{placeId}", googlePlaceId)
                .header(
                        "X-Goog-Api-Key",
                        apiKey
                )
                .header(
                        "X-Goog-FieldMask",
                        "id,displayName,formattedAddress,location,rating,userRatingCount,websiteUri"
                )
                .retrieve()
                .body(GooglePlaceDetails.class);
    }
}
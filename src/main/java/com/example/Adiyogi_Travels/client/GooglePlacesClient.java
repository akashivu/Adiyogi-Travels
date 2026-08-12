package com.example.Adiyogi_Travels.client;

import com.example.Adiyogi_Travels.dto.google.GoogleNearbyPlace;
import com.example.Adiyogi_Travels.dto.google.GoogleNearbySearchResponse;
import com.example.Adiyogi_Travels.dto.google.GooglePlace;
import com.example.Adiyogi_Travels.dto.google.GooglePlaceDetails;
import com.example.Adiyogi_Travels.dto.google.GooglePlaceSearchResponse;
import com.example.Adiyogi_Travels.dto.google.GooglePhoto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

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
                        "id,displayName,formattedAddress,location,rating,userRatingCount,websiteUri,photos"
                )
                .retrieve()
                .body(GooglePlaceDetails.class);
    }



    public List<GoogleNearbyPlace> searchNearbyAttractions(
            double latitude,
            double longitude
    ) {

        Map<String, Object> requestBody = Map.of(
                "includedTypes",
                List.of("tourist_attraction"),
                "maxResultCount",
                10,
                "rankPreference",
                "POPULARITY",
                "locationRestriction",
                Map.of(
                        "circle",
                        Map.of(
                                "center",
                                Map.of(
                                        "latitude",
                                        latitude,
                                        "longitude",
                                        longitude
                                ),
                                "radius",
                                10000.0
                        )
                )
        );

        GoogleNearbySearchResponse response =
                restClient.post()
                        .uri("/places:searchNearby")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(
                                "X-Goog-Api-Key",
                                apiKey
                        )
                        .header(
                                "X-Goog-FieldMask",
                                "places.id," +
                                        "places.displayName," +
                                        "places.formattedAddress," +
                                        "places.location," +
                                        "places.primaryType," +
                                        "places.types," +
                                        "places.photos"
                        )
                        .body(requestBody)
                        .retrieve()
                        .body(GoogleNearbySearchResponse.class);

        if (response == null ||
                response.places() == null) {

            return List.of();
        }

        return response.places();
    }



    public List<GoogleNearbyPlace> searchNearby(
            double latitude,
            double longitude,
            String placeType
    ) {

        Map<String, Object> requestBody = Map.of(
                "includedTypes",
                List.of(placeType),
                "maxResultCount",
                10,
                "rankPreference",
                "POPULARITY",
                "locationRestriction",
                Map.of(
                        "circle",
                        Map.of(
                                "center",
                                Map.of(
                                        "latitude",
                                        latitude,
                                        "longitude",
                                        longitude
                                ),
                                "radius",
                                10000.0
                        )
                )
        );

        GoogleNearbySearchResponse response =
                restClient.post()
                        .uri("/places:searchNearby")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(
                                "X-Goog-Api-Key",
                                apiKey
                        )
                        .header(
                                "X-Goog-FieldMask",
                                "places.id," +
                                        "places.displayName," +
                                        "places.formattedAddress," +
                                        "places.location," +
                                        "places.primaryType," +
                                        "places.types," +
                                        "places.photos"
                        )
                        .body(requestBody)
                        .retrieve()
                        .body(GoogleNearbySearchResponse.class);

        if (response == null ||
                response.places() == null) {

            return List.of();
        }

        return response.places();
    }



    public String getPlacePhotoUri(
            String photoResourceName,
            int maxWidthPx
    ) {
        if (photoResourceName == null ||
                photoResourceName.isBlank()) {
            return null;
        }

        GooglePhotoMediaResponse response =
                restClient.get()
                        .uri(uriBuilder ->
                                uriBuilder
                                        .path("/{photoName}/media")
                                        .queryParam("maxWidthPx", maxWidthPx)
                                        .queryParam("skipHttpRedirect", "true")
                                        .build(photoResourceName)
                        )
                        .header("X-Goog-Api-Key", apiKey)
                        .retrieve()
                        .body(GooglePhotoMediaResponse.class);

        if (response == null) {
            return null;
        }

        return response.photoUri();
    }

    private record GooglePhotoMediaResponse(
            String name,
            String photoUri
    ) {
    }
}
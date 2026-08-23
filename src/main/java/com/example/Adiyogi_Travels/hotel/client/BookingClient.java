package com.example.Adiyogi_Travels.hotel.client;

import com.example.Adiyogi_Travels.hotel.dto.HotelResult;
import com.example.Adiyogi_Travels.hotel.dto.HotelSearchRequest;
import com.example.Adiyogi_Travels.hotel.dto.HotelSearchResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class BookingClient {

    private final RestClient restClient;
    private final String apiKey;

    public BookingClient(
            @Value("${stay22.api-key:}") String apiKey
    ) {
        this.apiKey = apiKey;

        this.restClient = RestClient.builder()
                .baseUrl("https://api.stay22.com")
                .build();
    }

    public HotelSearchResponse search(
            HotelSearchRequest request
    ) {

        String url = UriComponentsBuilder
                .fromPath("/v2/accommodations")
                .queryParam("address", request.getDestination())
                .queryParam("checkin", request.getCheckIn())
                .queryParam("checkout", request.getCheckOut())
                .queryParam("pageSize", 20)
                .build()
                .encode()
                .toUriString();

        RestClient.RequestHeadersSpec<?> requestSpec =
                restClient.get()
                        .uri(url);

        /*
         * API key is optional for Stay22 demo mode.
         * For production, always configure your API key.
         */
        if (apiKey != null && !apiKey.isBlank()) {
            requestSpec.header(
                    "X-API-KEY",
                    apiKey
            );
        }

        JsonNode response = requestSpec
                .retrieve()
                .body(JsonNode.class);

        if (response == null) {
            return new HotelSearchResponse(
                    UUID.randomUUID().toString(),
                    "FAILED",
                    List.of()
            );
        }

        String currency = response
                .path("meta")
                .path("currency")
                .asText(request.getCurrency());

        int nights = response
                .path("meta")
                .path("nights")
                .asInt(1);

        if (nights <= 0) {
            nights = 1;
        }

        List<HotelResult> hotels = new ArrayList<>();

        JsonNode results = response.path("results");

        if (results.isArray()) {
            for (JsonNode hotelNode : results) {

                HotelResult hotel =
                        mapHotel(
                                hotelNode,
                                currency,
                                nights,
                                request.getDestination()
                        );

                if (hotel != null) {
                    hotels.add(hotel);
                }
            }
        }

        return new HotelSearchResponse(
                UUID.randomUUID().toString(),
                "SUCCESS",
                hotels
        );
    }


    private HotelResult mapHotel(
            JsonNode hotelNode,
            String currency,
            int nights,
            String fallbackLocation
    ) {

        String id = hotelNode
                .path("id")
                .asText(UUID.randomUUID().toString());

        String name = hotelNode
                .path("name")
                .asText("Accommodation");

        String location = hotelNode
                .path("location")
                .path("address")
                .asText(fallbackLocation);

        String bookingUrl = hotelNode
                .path("url")
                .asText(null);

        String roomType = hotelNode
                .path("type")
                .asText("Accommodation");

        String imageUrl = extractImage(hotelNode);

        Double rating = extractRating(hotelNode);

        Integer reviewCount = extractReviewCount(hotelNode);

        PriceData priceData =
                extractBestPrice(hotelNode, nights);

        return new HotelResult(
                id,
                name,
                location,
                imageUrl,
                rating,
                reviewCount,
                priceData.pricePerNight(),
                currency,
                roomType,
                bookingUrl
        );
    }


    /*
     * Stay22 can return multiple suppliers for one property.
     * We select the lowest available supplier price.
     */
    private PriceData extractBestPrice(
            JsonNode hotelNode,
            int nights
    ) {

        List<Double> prices = new ArrayList<>();

        JsonNode suppliers =
                hotelNode.path("suppliers");

        if (suppliers.isObject()) {

            suppliers.fields().forEachRemaining(entry -> {

                JsonNode total =
                        entry.getValue()
                                .path("price")
                                .path("total");

                if (total.isNumber()
                        && total.asDouble() > 0) {

                    prices.add(total.asDouble());
                }
            });
        }

        Double totalPrice = prices.stream()
                .min(Comparator.naturalOrder())
                .orElse(null);

        if (totalPrice == null) {
            return new PriceData(null);
        }

        return new PriceData(
                totalPrice / nights
        );
    }


    private String extractImage(
            JsonNode hotelNode
    ) {

        JsonNode media =
                hotelNode.path("media");

        if (media.isArray()
                && !media.isEmpty()) {

            JsonNode firstImage = media.get(0);

            if (firstImage.isTextual()) {
                return firstImage.asText();
            }

            if (firstImage.has("url")) {
                return firstImage
                        .path("url")
                        .asText(null);
            }
        }

        return null;
    }


    private Double extractRating(
            JsonNode hotelNode
    ) {

        JsonNode rating =
                hotelNode.path("rating");

        if (rating.isNumber()) {
            return rating.asDouble();
        }

        if (rating.has("value")) {
            return rating
                    .path("value")
                    .asDouble();
        }

        return null;
    }


    private Integer extractReviewCount(
            JsonNode hotelNode
    ) {

        JsonNode reviewCount =
                hotelNode.path("reviewCount");

        if (reviewCount.isInt()) {
            return reviewCount.asInt();
        }

        return null;
    }


    private record PriceData(
            Double pricePerNight
    ) {
    }
}
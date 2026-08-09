package com.example.Adiyogi_Travels.client;

import com.example.Adiyogi_Travels.flight.dto.FlightResult;
import com.example.Adiyogi_Travels.flight.dto.FlightSearchRequest;
import com.example.Adiyogi_Travels.dto.flight.FlightSearchResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class TravelpayoutsClient {

    public FlightSearchResponse search(
            FlightSearchRequest request
    ) {

        /*
         * Temporary mock response.
         *
         * We will replace this with the actual
         * Travelpayouts API call after confirming
         * your enabled API/product and credentials.
         */

        FlightResult mockFlight =
                new FlightResult(
                        UUID.randomUUID().toString(),
                        "Demo Airline",
                        "ELX101",
                        request.getOrigin(),
                        request.getDestination(),
                        "10:30",
                        "12:45",
                        "2h 15m",
                        true,
                        8500,
                        request.getCurrency(),
                        null
                );

        return new FlightSearchResponse(
                UUID.randomUUID().toString(),
                "MOCK",
                List.of(mockFlight)
        );
    }
}
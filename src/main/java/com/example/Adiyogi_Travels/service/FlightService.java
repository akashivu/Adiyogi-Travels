package com.example.Adiyogi_Travels.service;

import com.example.Adiyogi_Travels.client.TravelpayoutsClient;
import com.example.Adiyogi_Travels.flight.dto.FlightSearchRequest;
import com.example.Adiyogi_Travels.dto.flight.FlightSearchResponse;
import org.springframework.stereotype.Service;

@Service
public class FlightService {

    private final TravelpayoutsClient travelpayoutsClient;

    public FlightService(
            TravelpayoutsClient travelpayoutsClient
    ) {
        this.travelpayoutsClient =
                travelpayoutsClient;
    }

    public FlightSearchResponse search(
            FlightSearchRequest request
    ) {
        return travelpayoutsClient.search(
                request
        );
    }
}
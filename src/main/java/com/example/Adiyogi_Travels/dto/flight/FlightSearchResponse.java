package com.example.Adiyogi_Travels.dto.flight;

import java.util.List;

public class FlightSearchResponse {

    private String searchId;

    private String status;

    private List<com.example.Adiyogi_Travels.flight.dto.FlightResult> flights;

    public FlightSearchResponse() {
    }

    public FlightSearchResponse(
            String searchId,
            String status,
            List<com.example.Adiyogi_Travels.flight.dto.FlightResult> flights
    ) {
        this.searchId = searchId;
        this.status = status;
        this.flights = flights;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<com.example.Adiyogi_Travels.flight.dto.FlightResult> getFlights() {
        return flights;
    }

    public void setFlights(
            List<com.example.Adiyogi_Travels.flight.dto.FlightResult> flights
    ) {
        this.flights = flights;
    }
}
package com.example.Adiyogi_Travels.hotel.dto;

import java.util.List;

public class HotelSearchResponse {

    private String searchId;

    private String status;

    private List<HotelResult> hotels;

    public HotelSearchResponse() {
    }

    public HotelSearchResponse(
            String searchId,
            String status,
            List<HotelResult> hotels
    ) {
        this.searchId = searchId;
        this.status = status;
        this.hotels = hotels;
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

    public List<HotelResult> getHotels() {
        return hotels;
    }

    public void setHotels(List<HotelResult> hotels) {
        this.hotels = hotels;
    }
}
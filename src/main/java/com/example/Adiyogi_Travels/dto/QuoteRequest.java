package com.example.Adiyogi_Travels.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuoteRequest {
    private double pickupLat;
    private double pickupLng;
    private double dropLat;
    private double dropLng;
    private String vehicleCategory;
    private LocalDateTime pickupTime;
    private Long vehicleId;
    private double distanceKm;
    private String tripType;
}

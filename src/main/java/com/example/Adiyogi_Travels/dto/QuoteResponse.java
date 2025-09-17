package com.example.Adiyogi_Travels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuoteResponse {
    private String vehicleName;
    private int capacity;
    private boolean ac;
    private double ratePerKm;
    private double distanceKm;
    private double totalFare;
    private String imageUrl;
    private String features;
}

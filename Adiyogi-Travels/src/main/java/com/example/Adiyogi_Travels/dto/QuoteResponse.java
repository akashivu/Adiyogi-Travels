package com.example.Adiyogi_Travels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuoteResponse {
    private double distanceKm;
    private int durationMin;
    private double totalFare;
}


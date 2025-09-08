package com.example.Adiyogi_Travels.service;

import com.example.Adiyogi_Travels.dto.QuoteResponse;

public class FareService {
    private static final double BASE = 100;
    private static final double PER_KM = 12;
    private static final double PER_MIN = 1;


    public QuoteResponse compute(double distanceKm, int durationMin, String category) {
        double fare = BASE + (PER_KM * distanceKm) + (PER_MIN * durationMin);
        return new QuoteResponse(distanceKm, durationMin, fare);
    }
}

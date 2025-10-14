package com.example.Adiyogi_Travels.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuoteResponse {

    private long id;
    private String vehicleName;
    private String imageUrl;
    private int capacity;
    private int bags;
    private boolean ac;

    private double pricePerKm;
    private double distanceKm;
    private double driverAllowance;
    private double gstPercent;
    private double totalFare;

    private List<String> inclusion;
    private List<String> exclusion;

    private FareBreakdown fareBreakdown;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FareBreakdown {
        private double distance;
        private double farePerKm;
        private double driverAllowance;
        private double gst;
        private double total;
    }
}

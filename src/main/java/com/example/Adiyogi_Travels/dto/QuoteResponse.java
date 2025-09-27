package com.example.Adiyogi_Travels.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class QuoteResponse {
    private long id;
    private String vehicleName;
    private int capacity;
    private boolean ac;
    private double pricePerKm;
    private double distanceKm;
    private double totalFare;
    private String imageUrl;




    private int bags;
    private double driverAllowance;
    private double gstPercent;
    private double extraKmFare;
    private int includedKm;

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

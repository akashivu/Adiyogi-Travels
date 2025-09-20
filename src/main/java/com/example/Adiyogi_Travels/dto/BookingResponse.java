package com.example.Adiyogi_Travels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private String vehicleName;
    private String pickup;
    private String dropoff;
    private String tripType;
    private double distanceKm;
    private double fare;
    private String pickupDate;
    private String pickupTime;
    private String mobile;
}

package com.example.Adiyogi_Travels.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {

    private String name;
    private String email;

    private String tripCategory;
    private String tripType;

    private String fromLocation;
    private String toLocation;
    private String city;
    private String pickupLocation;

    private String pickupDate;
    private String pickupTime;

    private String mobile;
    private String vehicleName;

    private double distanceKm;
    private double fare;
}


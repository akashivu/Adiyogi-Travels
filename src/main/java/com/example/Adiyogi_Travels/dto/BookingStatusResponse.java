package com.example.Adiyogi_Travels.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingStatusResponse {
    private Long bookingId;
    private String status;
    private String vehicleName;
    private String fromLocation;
    private String toLocation;
    private double fare;
    private String pickupDate;
}
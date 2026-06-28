package com.example.Adiyogi_Travels.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponse {

    private Long bookingId;
    private String vehicleName;

    private String fromLocation;
    private String toLocation;
    private String tripType;

    private double distanceKm;
    private double fare;

    private String pickupDate;
    private String pickupTime;
    private String mobileNo;
    private String status;

   

    public BookingResponse(Long id, String vehicleName, String fromLocation, String toLocation, String tripType, double distanceKm, double fare, String string, String string1, String mobileNo, Object o, String name) {
    }

    public BookingResponse(Long id, String vehicleName, String fromLocation, String toLocation, String tripType, double distanceKm, double fare, String string, String string1, String mobileNo) {
    }

    public BookingResponse(Long id, String name, String vehicleName, String fromLocation, String toLocation, String tripType, double distanceKm, double fare, String string, String string1, String mobileNo) {
    }
}

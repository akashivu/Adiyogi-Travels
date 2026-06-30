package com.example.Adiyogi_Travels.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Please enter a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    private String tripCategory;
    private String tripType;

    private String fromLocation;
    private String toLocation;
    private String city;
    private String pickupLocation;

    @NotBlank(message = "Pickup date is required")
    private String pickupDate;

    @NotBlank(message = "Pickup time is required")
    private String pickupTime;

    @NotBlank(message = "Mobile number is required")
    private String mobile;

    @NotBlank(message = "Vehicle name is required")
    private String vehicleName;

    private double distanceKm;
    private double fare;
}
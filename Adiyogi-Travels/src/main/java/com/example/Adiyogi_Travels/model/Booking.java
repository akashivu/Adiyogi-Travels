package com.example.Adiyogi_Travels.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tripCategory;
    private String tripType;

    private String fromLocation;
    private String toLocation;
    private String city;
    private String pickupLocation;

    private LocalDate pickupDate;
    private String pickupTime;

    private String mobileNo;
    private double distanceKm;
    private double fare;
    private String status;


    private String pickup;
    private String dropoff;
}

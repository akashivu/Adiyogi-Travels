package com.example.Adiyogi_Travels.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String customerEmail;
    private String tripCategory;
    private String tripType;

    private String fromLocation;
    private String toLocation;
    private String city;
    private String pickupLocation;

    private LocalDate pickupDate;
    private LocalTime pickupTime;

    private String mobileNo;
    private String vehicleName;
    private double distanceKm;
    private double fare;
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}

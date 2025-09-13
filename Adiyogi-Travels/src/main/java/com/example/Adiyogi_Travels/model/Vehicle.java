package com.example.Adiyogi_Travels.model;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int capacity;
    private boolean ac;

    private double oneWayRatePerKm;
    private double roundTripRatePerKm;
    private String imageUrl;
    private String features;
}

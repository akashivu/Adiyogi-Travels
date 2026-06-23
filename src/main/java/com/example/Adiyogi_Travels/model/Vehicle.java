package com.example.Adiyogi_Travels.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String imageUrl;
    private int capacity;
    private int bags;
    private boolean ac;

    @Column(name = "price_per_km")
    private Double pricePerKm;

    @Column(name = "driver_allowance")
    private Double driverAllowance;

    @Column(name = "gst_percent")
    private Double gstPercent;

    @Column(name = "extra_km_fare")
    private Double extraKmFare;

    @Column(name = "included_km")
    private Integer includedKm;
}


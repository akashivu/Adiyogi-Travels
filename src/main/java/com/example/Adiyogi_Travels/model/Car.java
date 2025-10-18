package com.example.Adiyogi_Travels.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rental_car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String carType;
    private String imageUrl;
    private int seats;
    private String fuelType;
    private boolean ac;

    @Enumerated(EnumType.STRING)
    private AvailableFor availableFor;

    public enum AvailableFor {
        RENTAL, AIRPORT, BOTH
    }


}


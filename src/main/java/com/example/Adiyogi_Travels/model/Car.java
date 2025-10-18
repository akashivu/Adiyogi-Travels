package com.example.Adiyogi_Travels.model;

import jakarta.persistence.*;

@Entity
@Table(name = "airport_car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String carType;
    private String imageUrl;
    private int seats;
    private int perKmRate;
    private String fuelType;
    private boolean ac;


}


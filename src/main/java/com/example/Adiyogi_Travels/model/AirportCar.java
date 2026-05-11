package com.example.Adiyogi_Travels.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "airport_cars")
public class AirportCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int capacity;

    private double pricePerKm;

    private double baseFare;

    private String imageUrl;

    private int luggage;

    public AirportCar() {
    }

    public AirportCar(String name,
                      int capacity,
                      double pricePerKm,
                      double baseFare,
                      String imageUrl) {

        this.name = name;
        this.capacity = capacity;
        this.pricePerKm = pricePerKm;
        this.baseFare = baseFare;
        this.imageUrl = imageUrl;
    }

}
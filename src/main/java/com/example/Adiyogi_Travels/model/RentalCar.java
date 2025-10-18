package com.example.Adiyogi_Travels.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class RentalCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String carType;
    private String imageUrl;
    private int seats;
    private double fare4H;
    private double fare8H;
    private double fare12H;
    private String fuelType;
    private boolean ac;
}

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

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String imageUrl;
    private int capacity;
    private int bags;
    private boolean ac;

    private double pricePerKm;
    private double driverAllowance;
    private double gstPercent;
    private double extraKmFare;
    private Integer includedKm;
}

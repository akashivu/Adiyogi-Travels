package com.example.Adiyogi_Travels.model;

public class AirportCar {

    private String name;
    private int seats;
    private double pricePerKm;
    private double baseFare;
    private String imageUrl;

    public AirportCar(String name, int seats, double pricePerKm, double baseFare, String imageUrl) {
        this.name = name;
        this.seats = seats;
        this.pricePerKm = pricePerKm;
        this.baseFare = baseFare;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public int getSeats() { return seats; }
    public double getPricePerKm() { return pricePerKm; }
    public double getBaseFare() { return baseFare; }
    public String getImageUrl() { return imageUrl; }
}
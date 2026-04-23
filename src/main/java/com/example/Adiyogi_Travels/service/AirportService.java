package com.example.Adiyogi_Travels.service;

import com.example.Adiyogi_Travels.model.AirportCar;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AirportService {

    private final List<AirportCar> carList = new ArrayList<>();

    public AirportService() {

        carList.add(new AirportCar("Mini", 4, 12, 300, "img1"));
        carList.add(new AirportCar("Sedan", 4, 15, 400, "img2"));
        carList.add(new AirportCar("SUV", 6, 20, 500, "img3"));
        carList.add(new AirportCar("Innova", 6, 22, 600, "img4"));
        carList.add(new AirportCar("Tempo Traveller", 12, 25, 800, "img5"));
    }

    public List<AirportCar> getAllCars() {
        return carList;
    }

    public double calculateFare(String carName, double distance) {

        for (AirportCar car : carList) {
            if (car.getName().equalsIgnoreCase(carName)) {
                return car.getBaseFare() + (distance * car.getPricePerKm());
            }
        }

        throw new RuntimeException("Car not found");
    }
}
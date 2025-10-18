package com.example.Adiyogi_Travels.service;

import com.example.Adiyogi_Travels.model.Car;
import com.example.Adiyogi_Travels.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    private final CarRepository carRepository;

    @Autowired
    public AirportService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAvailableAirportCars() {
        return carRepository.findByAvailableForIn(List.of(Car.AvailableFor.AIRPORT, Car.AvailableFor.BOTH));
    }

    public double calculateFare(double distanceInKm) {
        return distanceInKm * 26;
    }
}


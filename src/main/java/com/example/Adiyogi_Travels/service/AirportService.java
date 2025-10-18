package com.example.Adiyogi_Travels.service;

import com.example.Adiyogi_Travels.model.Car;
import com.example.Adiyogi_Travels.repository.CarRepository;
import com.example.Adiyogi_Travels.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AirportService {

    private final CarRepository airportCarRepository;

    @Autowired
    public AirportService(CarRepository airportCarRepository) {
        this.airportCarRepository = airportCarRepository;
    }


    public List<Car> getAvailableAirportCars() {
        return airportCarRepository.findAll();
    }


    public double calculateFare(double distanceInKm) {
        return distanceInKm * 26;
    }
}

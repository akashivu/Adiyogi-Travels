package com.example.Adiyogi_Travels.service;

import com.example.Adiyogi_Travels.model.AirportCar;
import com.example.Adiyogi_Travels.repository.AirportCarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    private final AirportCarRepository airportCarRepository;

    public AirportService(AirportCarRepository airportCarRepository) {
        this.airportCarRepository = airportCarRepository;
    }

    public List<AirportCar> getAllCars() {

        return airportCarRepository.findAll();
    }

    public double calculateFare(String carName, double distance) {

        List<AirportCar> cars = airportCarRepository.findAll();

        for (AirportCar car : cars) {

            if (car.getName().equalsIgnoreCase(carName)) {

                return car.getBaseFare()
                        + (distance * car.getPricePerKm());
            }
        }

        throw new RuntimeException("Car not found");
    }
}
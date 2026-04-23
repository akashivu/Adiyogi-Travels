package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.model.AirportCar;
import com.example.Adiyogi_Travels.service.AirportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/airport")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping("/vehicles")
    public ResponseEntity<List<AirportCar>> getVehicles() {
        return ResponseEntity.ok(airportService.getAllCars());
    }

    @GetMapping("/fare")
    public ResponseEntity<Double> calculateFare(
            @RequestParam String carName,
            @RequestParam double distance) {

        return ResponseEntity.ok(
                airportService.calculateFare(carName, distance)
        );
    }
}


package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.model.Car;
import com.example.Adiyogi_Travels.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airport")
public class AirportController {

    private final AirportService airportService;

    @Autowired
    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getAirportCars() {
        return ResponseEntity.ok(airportService.getAvailableAirportCars());
    }

    @GetMapping("/fare")
    public ResponseEntity<Double> calculateFare(@RequestParam double distance) {
        return ResponseEntity.ok(airportService.calculateFare(distance));
    }
}


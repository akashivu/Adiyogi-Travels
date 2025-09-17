package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.dto.QuoteRequest;
import com.example.Adiyogi_Travels.dto.QuoteResponse;
import com.example.Adiyogi_Travels.repository.VehicleRepository;
import com.example.Adiyogi_Travels.service.FareService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class QuoteController {
    private final FareService fareService;
    private final VehicleRepository vehicleRepository;

    @PostMapping("/quotes")
    public List<QuoteResponse> getQuotes(@RequestBody QuoteRequest req) {
        var vehicles = vehicleRepository.findAll();
        return vehicles.stream().map(v -> {
            double ratePerKm = "roundtrip".equalsIgnoreCase(req.getTripType())
                    ? v.getRoundTripRatePerKm()
                    : v.getOneWayRatePerKm();

            double total = ratePerKm * req.getDistanceKm();

            return new QuoteResponse(
                    v.getName(),
                    v.getCapacity(),
                    v.isAc(),
                    ratePerKm,
                    req.getDistanceKm(),
                    total,
                    v.getImageUrl(),
                    v.getFeatures()
            );
        }).toList();
    }
}

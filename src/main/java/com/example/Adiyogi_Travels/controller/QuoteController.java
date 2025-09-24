package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.dto.QuoteRequest;
import com.example.Adiyogi_Travels.dto.QuoteResponse;
import com.example.Adiyogi_Travels.repository.VehicleRepository;
import com.example.Adiyogi_Travels.service.FareService;
import com.example.Adiyogi_Travels.service.GoogleMapsService;
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
    private final GoogleMapsService googleMapsService;
    @PostMapping("/quotes")
    public List<QuoteResponse> getQuotes(@RequestBody QuoteRequest req) {
        double distanceKm = googleMapsService.getDistance(req.getPickup(), req.getDropoff());

        var vehicles = vehicleRepository.findAll();
        System.out.println("Vehicles fetched: " + vehicles.size());
        System.out.println("Distance calculated: " + distanceKm);
        return vehicles.stream().map(v -> {
            double ratePerKm = "roundtrip".equalsIgnoreCase(req.getTripType())
                    ? v.getRoundTripRatePerKm()
                    : v.getOneWayRatePerKm();

            double total = ratePerKm * distanceKm;

            return new QuoteResponse(
                    v.getName(),
                    v.getCapacity(),
                    v.isAc(),
                    ratePerKm,
                    distanceKm,
                    total,
                    v.getImageUrl(),
                    v.getFeatures()
            );
        }).toList();
    }
}

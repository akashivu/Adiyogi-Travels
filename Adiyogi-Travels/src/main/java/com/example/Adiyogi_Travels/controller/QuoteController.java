package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.dto.QuoteRequest;
import com.example.Adiyogi_Travels.dto.QuoteResponse;
import com.example.Adiyogi_Travels.service.FareService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class QuoteController {
    private final FareService fareService;


    @PostMapping("/quote")
    public QuoteResponse getQuote(@RequestBody QuoteRequest req) {

        double distanceKm = 25.4;
        int durationMin = 45;
        return fareService.compute(distanceKm, durationMin, req.getVehicleCategory());
    }
}

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
    @PostMapping("/quotes")
    public List<QuoteResponse> getQuotes(@RequestBody QuoteRequest req) {

        return fareService.search(req.getPickup(), req.getDropoff(), req.getTripType());
    }
}

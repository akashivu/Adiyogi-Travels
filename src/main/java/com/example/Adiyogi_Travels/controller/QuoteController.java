package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.dto.QuoteRequest;
import com.example.Adiyogi_Travels.dto.QuoteResponse;
import com.example.Adiyogi_Travels.repository.VehicleRepository;
import com.example.Adiyogi_Travels.service.FareService;
import com.example.Adiyogi_Travels.service.GoogleMapsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class QuoteController {
    private final FareService fareService;
    @PostMapping("/api/quotes")
    public List<QuoteResponse> getQuotes(@RequestBody Map<String, Object> body) {
        String pickup = (String) body.get("pickup");
        String dropoff = (String) body.get("dropoff");
        String tripType = (String) body.get("tripType");

        Double pickupLat = body.get("pickupLat") != null ? ((Number) body.get("pickupLat")).doubleValue() : null;
        Double pickupLng = body.get("pickupLng") != null ? ((Number) body.get("pickupLng")).doubleValue() : null;
        Double dropLat = body.get("dropLat") != null ? ((Number) body.get("dropLat")).doubleValue() : null;
        Double dropLng = body.get("dropLng") != null ? ((Number) body.get("dropLng")).doubleValue() : null;

        return fareService.search(pickup, dropoff, tripType, pickupLat, pickupLng, dropLat, dropLng);
    }

}

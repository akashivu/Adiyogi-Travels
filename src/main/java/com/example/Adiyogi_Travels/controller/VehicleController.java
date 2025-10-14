package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.dto.QuoteResponse;
import com.example.Adiyogi_Travels.service.FareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quotes")
@CrossOrigin(origins = {"http://localhost:5173", "https://adiyogi-travels.onrender.com"})
public class VehicleController {

    @Autowired
    private FareService fareService;

    @PostMapping
    public List<QuoteResponse> getQuotes(@RequestBody Map<String, Object> body) {
        String pickup = (String) body.get("pickup");
        String dropoff = (String) body.get("dropoff");
        String tripType = (String) body.get("tripType");


        Double pickupLat = body.get("pickupLat") != null ? ((Number) body.get("pickupLat")).doubleValue() : null;
        Double pickupLng = body.get("pickupLng") != null ? ((Number) body.get("pickupLng")).doubleValue() : null;
        Double dropLat = body.get("dropLat") != null ? ((Number) body.get("dropLat")).doubleValue() : null;
        Double dropLng = body.get("dropLng") != null ? ((Number) body.get("dropLng")).doubleValue() : null;

        System.out.println("VehicleController Request:");
        System.out.println("Pickup: " + pickup + " | Drop: " + dropoff);
        System.out.println("Pickup Coords: " + pickupLat + "," + pickupLng);
        System.out.println("Drop Coords: " + dropLat + "," + dropLng);


        return fareService.search(pickup, dropoff, tripType, pickupLat, pickupLng, dropLat, dropLng);
    }
}

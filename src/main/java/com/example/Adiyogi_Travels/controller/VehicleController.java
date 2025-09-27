package com.example.Adiyogi_Travels.controller;
import com.example.Adiyogi_Travels.dto.QuoteResponse;
import com.example.Adiyogi_Travels.service.FareService;
import org.springframework.web.bind.annotation.*;
import com.example.Adiyogi_Travels.model.Vehicle;
import com.example.Adiyogi_Travels.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.List;


@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleRepository repo;
    private final FareService fareService;

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return repo.findAll();
    }


    @GetMapping("/name/{name}")
    public ResponseEntity<Vehicle> getVehicleByName(@PathVariable String name) {
        return repo.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/search")
    public List<QuoteResponse> searchRide(
            @RequestParam String pickup,
            @RequestParam String drop,
            @RequestParam(defaultValue = "oneway") String tripType) {
        return fareService.search(pickup, drop, tripType);
    }
}

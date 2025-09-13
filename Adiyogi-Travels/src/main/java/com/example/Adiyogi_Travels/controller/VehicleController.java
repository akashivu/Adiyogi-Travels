package com.example.Adiyogi_Travels.controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Adiyogi_Travels.model.Vehicle;
import com.example.Adiyogi_Travels.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleRepository repo;


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
}

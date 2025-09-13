package com.example.Adiyogi_Travels.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Adiyogi_Travels.model.Vehicle;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByName(String name);
}

package com.example.Adiyogi_Travels.repository;

import com.example.Adiyogi_Travels.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByName(String name);

    @Query("SELECT AVG(v.pricePerKm) FROM Vehicle v")
    Double findAveragePricePerKm();

    @Query("SELECT v FROM Vehicle v WHERE v.capacity >= :minCapacity")
    List<Vehicle> findByMinCapacity(int minCapacity);
}


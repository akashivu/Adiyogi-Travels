package com.example.Adiyogi_Travels.service;


import com.example.Adiyogi_Travels.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FareService {
    private final VehicleRepository vehicleRepo;

    public Fare compute(Long vehicleId, double distanceKm, String tripType) {
        var vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        double ratePerKm;

        if ("roundtrip".equalsIgnoreCase(tripType)) {
            ratePerKm = vehicle.getRoundTripRatePerKm();
        } else {
            ratePerKm = vehicle.getOneWayRatePerKm();
        }


        double total = ratePerKm * distanceKm;

        return new Fare(vehicle.getName(), vehicle.getCapacity(), vehicle.isAc(), ratePerKm, distanceKm, total);
    }


    public static record Fare(
            String vehicleName,
            int capacity,
            boolean ac,
            double ratePerKm,
            double distanceKm,
            double total
    ) {}
}

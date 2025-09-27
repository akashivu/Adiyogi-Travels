package com.example.Adiyogi_Travels.service;


import com.example.Adiyogi_Travels.dto.QuoteResponse;
import com.example.Adiyogi_Travels.model.Vehicle;
import com.example.Adiyogi_Travels.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FareService {
    private final VehicleRepository vehicleRepository;
    private final GoogleMapsService googleMapsService;

    public List<QuoteResponse> search(String pickup, String drop, String tripType) {
        int distance = googleMapsService.getDistance(pickup, drop);
        List<Vehicle> vehicles = vehicleRepository.findAll();

        return vehicles.stream().map(v -> mapToFareResponse(v, distance)).toList();
    }

    private QuoteResponse mapToFareResponse(Vehicle v, int distance) {
        int includedKm = (v.getIncludedKm() == null || v.getIncludedKm() == 0)
                ? distance
                : v.getIncludedKm();

        double baseFare = Math.min(distance, includedKm) * v.getPricePerKm();
        double extraFare = distance > includedKm ? (distance - includedKm) * v.getExtraKmFare() : 0;
        double gst = (baseFare + extraFare + v.getDriverAllowance()) * (v.getGstPercent() / 100);
        double total = baseFare + extraFare + v.getDriverAllowance() + gst;

        return QuoteResponse.builder()
                .id(v.getId())
                .vehicleName(v.getName())
                .imageUrl(v.getImageUrl())
                .capacity(v.getCapacity())
                .bags(v.getBags())
                .ac(v.isAc())
                .includedKm(includedKm)
                .pricePerKm(v.getPricePerKm())
                .driverAllowance(v.getDriverAllowance())
                .gstPercent(v.getGstPercent())
                .distanceKm(distance)
                .totalFare(total)
                .inclusion(List.of(
                        "Base fare for " + includedKm + " km",
                        "Driver allowance ₹" + v.getDriverAllowance(),
                        "GST (" + v.getGstPercent() + "%)"
                ))
                .exclusion(List.of(
                        "₹" + v.getExtraKmFare() + "/km after " + includedKm + " km",
                        "Parking, Toll, State Tax, Night charges if applicable"
                ))
                .fareBreakdown(new QuoteResponse.FareBreakdown(
                        distance,
                        v.getPricePerKm(),
                        v.getDriverAllowance(),
                        gst,
                        total
                ))
                .build();
    }
}

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

    private static final double AIRPORT_RATE_PER_KM = 26.0;

    public List<QuoteResponse> search(String pickup, String drop, String tripType) {
        int distance = googleMapsService.getDistance(pickup, drop);
        List<Vehicle> vehicles = vehicleRepository.findAll();

        boolean isAirportTrip = containsAirport(pickup) || containsAirport(drop);

        return vehicles.stream()
                .map(v -> mapToFareResponse(v, distance, isAirportTrip))
                .toList();
    }

    private boolean containsAirport(String location) {
        return location != null && location.toLowerCase().contains("airport");
    }

    private QuoteResponse mapToFareResponse(Vehicle v, int distance, boolean isAirportTrip) {
        double baseFare, gst, total;

        if (isAirportTrip) {
            // 🛫 Airport special rate
            baseFare = distance * AIRPORT_RATE_PER_KM;
            gst = (baseFare + v.getDriverAllowance()) * (v.getGstPercent() / 100);
            total = baseFare + v.getDriverAllowance() + gst;
        } else {
            // 🚗 Regular fare calculation
            int includedKm = (v.getIncludedKm() == null || v.getIncludedKm() == 0) ? distance : v.getIncludedKm();
            double extraFare = distance > includedKm ? (distance - includedKm) * v.getExtraKmFare() : 0;
            baseFare = Math.min(distance, includedKm) * v.getPricePerKm();
            gst = (baseFare + extraFare + v.getDriverAllowance()) * (v.getGstPercent() / 100);
            total = baseFare + extraFare + v.getDriverAllowance() + gst;
        }

        return QuoteResponse.builder()
                .id(v.getId())
                .vehicleName(v.getName())
                .imageUrl(v.getImageUrl())
                .capacity(v.getCapacity())
                .bags(v.getBags())
                .ac(v.isAc())
                .pricePerKm(isAirportTrip ? AIRPORT_RATE_PER_KM : v.getPricePerKm())
                .driverAllowance(v.getDriverAllowance())
                .gstPercent(v.getGstPercent())
                .distanceKm(distance)
                .totalFare(total)
                .inclusion(List.of(
                        isAirportTrip
                                ? "₹26 per km (excluding toll & parking charges)"
                                : "Base fare for " + v.getIncludedKm() + " km",
                        "Driver allowance ₹" + v.getDriverAllowance(),
                        "GST (" + v.getGstPercent() + "%)"
                ))
                .exclusion(List.of(
                        "Parking, Toll, State Tax, Night charges if applicable"
                ))
                .fareBreakdown(new QuoteResponse.FareBreakdown(
                        distance,
                        isAirportTrip ? AIRPORT_RATE_PER_KM : v.getPricePerKm(),
                        v.getDriverAllowance(),
                        gst,
                        total
                ))
                .build();
    }
}
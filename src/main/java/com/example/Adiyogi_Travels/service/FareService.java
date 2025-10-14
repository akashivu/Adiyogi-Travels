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

    private static final int MIN_OUTSTATION_KM = 130;
    private static final double AIRPORT_RATE_PER_KM = 26.0;

    public List<QuoteResponse> search(
            String pickup,
            String drop,
            String tripType,
            Double pickupLat,
            Double pickupLng,
            Double dropLat,
            Double dropLng
    ) {
        int distance = googleMapsService.getDistance(pickup, drop, pickupLat, pickupLng, dropLat, dropLng);


        if (distance <= 0) {
            distance = MIN_OUTSTATION_KM;
        }

        List<Vehicle> vehicles = vehicleRepository.findAll();
        boolean isAirportTrip = containsAirport(pickup) || containsAirport(drop);

        int finalDistance = distance;
        return vehicles.stream()
                .map(v -> mapToFareResponse(v, finalDistance, isAirportTrip))
                .toList();
    }

    private boolean containsAirport(String location) {
        if (location == null) return false;
        String lower = location.toLowerCase();
        return lower.contains("airport")
                || lower.contains("terminal")
                || lower.contains("kempegowda")
                || lower.contains("blr");
    }

    private QuoteResponse mapToFareResponse(Vehicle v, int distance, boolean isAirportTrip) {
        double baseFare, gst, total;
        int chargedKm;

        if (isAirportTrip) {

            chargedKm = distance;
            baseFare = chargedKm * AIRPORT_RATE_PER_KM;
            gst = baseFare * (v.getGstPercent() / 100);
            total = baseFare + gst;

            return QuoteResponse.builder()
                    .id(v.getId())
                    .vehicleName(v.getName())
                    .imageUrl(v.getImageUrl())
                    .capacity(v.getCapacity())
                    .bags(v.getBags())
                    .ac(v.isAc())
                    .pricePerKm(AIRPORT_RATE_PER_KM)
                    .distanceKm(distance)
                    .driverAllowance(0)
                    .gstPercent(v.getGstPercent())
                    .totalFare(Math.round(total))
                    .inclusion(List.of(
                            "₹26 per km (excluding toll & parking)",
                            "GST (" + v.getGstPercent() + "%)"
                    ))
                    .exclusion(List.of(
                            "Toll, Parking, and Airport entry charges not included"
                    ))
                    .fareBreakdown(new QuoteResponse.FareBreakdown(
                            chargedKm,
                            AIRPORT_RATE_PER_KM,
                            0,
                            gst,
                            total
                    ))
                    .build();

        } else {

            chargedKm = Math.max(distance, MIN_OUTSTATION_KM);
            baseFare = chargedKm * v.getPricePerKm();
            gst = (baseFare + v.getDriverAllowance()) * (v.getGstPercent() / 100);
            total = baseFare + v.getDriverAllowance() + gst;

            return QuoteResponse.builder()
                    .id(v.getId())
                    .vehicleName(v.getName())
                    .imageUrl(v.getImageUrl())
                    .capacity(v.getCapacity())
                    .bags(v.getBags())
                    .ac(v.isAc())
                    .pricePerKm(v.getPricePerKm())
                    .distanceKm(distance)
                    .driverAllowance(v.getDriverAllowance())
                    .gstPercent(v.getGstPercent())
                    .totalFare(Math.round(total))
                    .inclusion(List.of(
                            "Minimum billing for 130 km",
                            "Driver allowance ₹" + v.getDriverAllowance(),
                            "GST (" + v.getGstPercent() + "%)"
                    ))
                    .exclusion(List.of(
                            "Toll, Parking, State Tax, Night charges if applicable"
                    ))
                    .fareBreakdown(new QuoteResponse.FareBreakdown(
                            chargedKm,
                            v.getPricePerKm(),
                            v.getDriverAllowance(),
                            gst,
                            total
                    ))
                    .build();
        }
    }
}


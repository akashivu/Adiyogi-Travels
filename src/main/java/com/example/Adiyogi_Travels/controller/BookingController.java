package com.example.Adiyogi_Travels.controller;
import com.example.Adiyogi_Travels.dto.BookingRequest;
import com.example.Adiyogi_Travels.dto.BookingResponse;
import com.example.Adiyogi_Travels.model.Booking;
import com.example.Adiyogi_Travels.repository.BookingRepository;
import com.example.Adiyogi_Travels.service.GoogleMapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private GoogleMapsService mapsService;

    @PostMapping
    public Map<String, Object> createBooking(@RequestBody Booking booking) {
        Booking savedBooking = bookingRepository.save(booking);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Booking request received successfully");
        response.put("bookingId", savedBooking.getId());
        return response;
    }

    @CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
    @PostMapping("/estimate")
    public Map<String, Object> estimateFare(@RequestBody Map<String, String> body) {
        double distance = mapsService.getDistance(body.get("pickup"), body.get("dropoff"));
        double fare = distance * 20;

        return Map.of(
                "distanceKm", distance,
                "fare", fare
        );
    }

    @PostMapping("/confirm")
    public BookingResponse confirmBooking(@RequestBody BookingRequest req) {


                Booking booking = Booking.builder()
                .pickup(req.getPickup())
                .dropoff(req.getDropoff())
                .tripType(req.getTripType())
                .distanceKm(req.getDistanceKm())
                .fare(req.getFare())
                .pickupDate(java.time.LocalDate.parse(req.getPickupDate()))
                .pickupTime(req.getPickupTime())
                .mobileNo(req.getMobile())
                .status("CONFIRMED")
                .build();

        Booking saved = bookingRepository.save(booking);

        return new BookingResponse(
                saved.getId(),
                req.getVehicleName(),
                saved.getPickup(),
                saved.getDropoff(),
                saved.getTripType(),
                saved.getDistanceKm(),
                saved.getFare(),
                saved.getPickupDate().toString(),
                saved.getPickupTime(),
                saved.getMobileNo()
        );
    }


}

package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.model.Booking;


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
    public Booking confirmBooking(@RequestBody Map<String, Object> body) {
        Booking booking = new Booking();
        booking.setPickup((String) body.get("pickup"));
        booking.setDropoff((String) body.get("dropoff"));
        booking.setDistanceKm(((Number) body.get("distanceKm")).doubleValue());
        booking.setFare(((Number) body.get("fare")).doubleValue());
        booking.setStatus("CONFIRMED");
        return bookingRepository.save(booking);
    }


}

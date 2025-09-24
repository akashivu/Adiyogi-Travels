package com.example.Adiyogi_Travels.controller;
import com.example.Adiyogi_Travels.dto.BookingRequest;
import com.example.Adiyogi_Travels.dto.BookingResponse;
import com.example.Adiyogi_Travels.model.Booking;
import com.example.Adiyogi_Travels.model.User;
import com.example.Adiyogi_Travels.repository.BookingRepository;
import com.example.Adiyogi_Travels.repository.UserRepository;
import com.example.Adiyogi_Travels.service.GoogleMapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private GoogleMapsService mapsService;
   @Autowired
   private UserRepository userRepository;

    @GetMapping("/my-bookings/{userId}")
    public List<BookingResponse> getMyBookings(@PathVariable Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);

        return bookings.stream()
                .map(b -> new BookingResponse(
                        b.getId(),
                        b.getVehicleName(),
                        b.getFromLocation(),
                        b.getToLocation(),
                        b.getTripType(),
                        b.getDistanceKm(),
                        b.getFare(),
                        b.getPickupDate().toString(),
                        b.getPickupTime().toString(),
                        b.getMobileNo()
                ))
                .toList();
    }

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
                Booking booking = Booking.builder()
                        .tripCategory(req.getTripCategory())
                        .tripType(req.getTripType())
                        .fromLocation(req.getFromLocation())
                        .toLocation(req.getToLocation())
                        .city(req.getCity())
                        .pickupLocation(req.getPickupLocation())
                        .pickupDate(LocalDate.parse(req.getPickupDate()))
                        .pickupTime(LocalTime.parse(req.getPickupTime()))
                        .mobileNo(req.getMobile())
                        .vehicleName(req.getVehicleName())
                        .distanceKm(req.getDistanceKm())
                        .fare(req.getFare())
                        .status("CONFIRMED")
                        .user(user)
                        .build();

        Booking saved = bookingRepository.save(booking);

        return new BookingResponse(
                saved.getId(),
                saved.getVehicleName(),
                saved.getFromLocation(),
                saved.getToLocation(),
                saved.getTripType(),
                saved.getDistanceKm(),
                saved.getFare(),
                saved.getPickupDate().toString(),
                saved.getPickupTime().toString(),
                saved.getMobileNo()
        );
    }


}

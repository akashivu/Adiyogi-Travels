package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.dto.BookingRequest;
import com.example.Adiyogi_Travels.dto.BookingResponse;
import com.example.Adiyogi_Travels.model.Booking;
import com.example.Adiyogi_Travels.model.BookingStatus;
import com.example.Adiyogi_Travels.model.User;
import com.example.Adiyogi_Travels.repository.BookingRepository;
import com.example.Adiyogi_Travels.repository.UserRepository;
import com.example.Adiyogi_Travels.service.EmailService;
import com.example.Adiyogi_Travels.service.GoogleMapsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @Autowired
    private EmailService emailService;

    @Value("${app.admin.email:vijaytourstravels6158@gmail.com}")
    private String adminEmail;

    @GetMapping("/my-bookings")
    public List<BookingResponse> getMyBookings() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        List<Booking> bookings = bookingRepository.findByUserId(user.getId());

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
        double fare;


        if (body.get("pickup").toLowerCase().contains("airport") ||
                body.get("dropoff").toLowerCase().contains("airport")) {
            fare = distance * 26;
        } else {
            fare = distance * 20;
        }

        return Map.of("distanceKm", distance, "fare", fare);
    }

    @PostMapping("/confirm")
    public BookingResponse confirmBooking(@RequestBody BookingRequest req) {
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
                .status(BookingStatus.CONFIRMED)
                .customerName(req.getName())
                .customerEmail(req.getEmail())
                .build();

        Booking saved = bookingRepository.save(booking);

        // Send confirmation emails
        String userSubject = "Your booking is confirmed — " + saved.getVehicleName();
        String userHtml = "<h3>Booking Confirmed</h3>"
                + "<p><b>Name:</b> " + req.getName() + "</p>"
                + "<p><b>Pickup:</b> " + saved.getFromLocation() + "</p>"
                + "<p><b>Drop:</b> " + saved.getToLocation() + "</p>"
                + "<p><b>Fare:</b> ₹" + saved.getFare() + "</p>"
                + "<p><b>Pickup Date:</b> " + saved.getPickupDate() + " " + saved.getPickupTime() + "</p>"
                + "<hr><p>Thank you for choosing Vijay Travels!</p>";

        String adminSubject = "New Booking — " + saved.getVehicleName();
        String adminHtml = "<h3>New Booking Alert</h3>"
                + "<p><b>Name:</b> " + req.getName() + "</p>"
                + "<p><b>Email:</b> " + req.getEmail() + "</p>"
                + "<p><b>Pickup:</b> " + saved.getFromLocation() + "</p>"
                + "<p><b>Drop:</b> " + saved.getToLocation() + "</p>"
                + "<p><b>Fare:</b> ₹" + saved.getFare() + "</p>";

        emailService.sendBookingNotifications(req.getEmail(), adminEmail, userSubject, userHtml, adminSubject, adminHtml);

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

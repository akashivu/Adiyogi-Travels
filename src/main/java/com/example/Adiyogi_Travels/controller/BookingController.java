package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.dto.BookingRequest;
import com.example.Adiyogi_Travels.dto.BookingResponse;
import com.example.Adiyogi_Travels.dto.BookingStatusResponse;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = null;

        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {

            user = userRepository.findByEmail(authentication.getName())
                    .orElse(null);
        }

        List<Booking> bookings = bookingRepository.findByUserId(user.getId());

        return bookings.stream()
                .map(b -> new BookingResponse(
                        b.getId(),
                        b.getStatus().name(),
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
    public Map<String, Object> estimateFare(@RequestBody Map<String, Object> body) {
        String pickup = (String) body.get("pickup");
        String dropoff = (String) body.get("dropoff");

        Double pickupLat = body.get("pickupLat") != null ? ((Number) body.get("pickupLat")).doubleValue() : null;
        Double pickupLng = body.get("pickupLng") != null ? ((Number) body.get("pickupLng")).doubleValue() : null;
        Double dropLat = body.get("dropLat") != null ? ((Number) body.get("dropLat")).doubleValue() : null;
        Double dropLng = body.get("dropLng") != null ? ((Number) body.get("dropLng")).doubleValue() : null;

        int distance = mapsService.getDistance(pickup, dropoff, pickupLat, pickupLng, dropLat, dropLng);

        double fare;
        boolean isAirportTrip = (pickup != null && pickup.toLowerCase().contains("airport")) ||
                (dropoff != null && dropoff.toLowerCase().contains("airport"));

        if (isAirportTrip) {
            fare = distance * 26;
        } else {
            int chargedKm = Math.max(distance, 130);
            fare = chargedKm * 20;
        }

        return Map.of("distanceKm", distance, "fare", fare);
    }

    @PostMapping("/confirm")
    public BookingResponse confirmBooking(@RequestBody BookingRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + email));
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
                .user(user)
                .build();

        Booking saved = bookingRepository.save(booking);

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

        emailService.sendBookingNotifications(
                req.getEmail(),
                adminEmail,
                userSubject,
                userHtml,
                adminSubject,
                adminHtml
        );

        return new BookingResponse(
                saved.getId(),
                saved.getStatus().name(),
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

    @GetMapping("/{bookingId}")
    public BookingStatusResponse getBookingStatus(
            @PathVariable Long bookingId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));


        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized.");
        }

        return new BookingStatusResponse(
                booking.getId(),
                booking.getStatus().name(),
                booking.getVehicleName(),
                booking.getFromLocation(),
                booking.getToLocation(),
                booking.getFare(),
                booking.getPickupDate().toString()
        );
    }
}


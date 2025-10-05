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
import com.example.Adiyogi_Travels.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private EmailService emailService;
    @Autowired
    private NotificationService notificationService;

    @Value("${app.admin.email:vijaytourstravels6158@gmail.com}")
    private String adminEmail;
    @Value("${admin.phone}")
    private String adminPhone;

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
        double fare = distance * 20;

        return Map.of(
                "distanceKm", distance,
                "fare", fare
        );
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


        String userSubject = "Your booking is confirmed — " + saved.getVehicleName();
        String userHtml = "<h3>Booking Confirmed </h3>"
                + "<p><b>Name:</b> " + req.getName() + "</p>"
                + "<p><b>Vehicle:</b> " + saved.getVehicleName() + "</p>"
                + "<p><b>Pickup:</b> " + saved.getFromLocation() + "</p>"
                + "<p><b>Drop:</b> " + saved.getToLocation() + "</p>"
                + "<p><b>Pickup Date:</b> " + saved.getPickupDate() + " " + saved.getPickupTime() + "</p>"
                + "<p><b>Fare:</b> ₹" + saved.getFare() + "</p>"
                + "<hr><p>Thank you for choosing Vijay Travels!</p>";

        String adminSubject = "New booking — " + saved.getVehicleName();
        String adminHtml = "<h3>New Booking Alert</h3>"
                + "<p><b>Name:</b> " + req.getName() + "</p>"
                + "<p><b>Email:</b> " + req.getEmail() + "</p>"
                + "<p><b>Mobile:</b> " + saved.getMobileNo() + "</p>"
                + "<p><b>Vehicle:</b> " + saved.getVehicleName() + "</p>"
                + "<p><b>Pickup:</b> " + saved.getFromLocation() + " — " + saved.getPickupDate() + " " + saved.getPickupTime() + "</p>"
                + "<p><b>Drop:</b> " + saved.getToLocation() + "</p>"
                + "<p><b>Fare:</b> ₹" + saved.getFare() + "</p>";

        emailService.sendBookingNotifications(req.getEmail(), adminEmail, userSubject, userHtml, adminSubject, adminHtml);
        String userMsg = "Hi " + saved.getCustomerName() + ", your booking #" + saved.getId() +
                " is confirmed \nPickup: " + saved.getPickupDate() + " " + saved.getPickupTime() +
                " from " + saved.getFromLocation() + " to " + saved.getToLocation() +
                ". Fare: ₹" + saved.getFare() + ". Thank you for choosing Vijay Travels!";

        String adminMsg = " New Booking Alert\n" +
                "Name: " + saved.getCustomerName() + "\n" +
                "Mobile: " + saved.getMobileNo() + "\n" +
                "Vehicle: " + saved.getVehicleName() + "\n" +
                "Pickup: " + saved.getFromLocation() + " — " + saved.getPickupDate() + " " + saved.getPickupTime() + "\n" +
                "Drop: " + saved.getToLocation() + "\nFare: ₹" + saved.getFare();


        notificationService.sendSms(saved.getMobileNo(), userMsg);
        notificationService.sendWhatsapp(saved.getMobileNo(), userMsg);

        // Send to admin (replace with your admin phone in E.164 format)
        String adminPhone = "+91XXXXXXXXXX"; // <-- put your admin's number here
        notificationService.sendSms(adminPhone, adminMsg);
        notificationService.sendWhatsapp(adminPhone, adminMsg);

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
    @PutMapping("/{id}/cancel")
    public Map<String, String> cancelBooking(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if ("CANCELLED".equalsIgnoreCase(String.valueOf(booking.getStatus()))) {
            return Map.of("message", "Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        // ---- Notify User ----
        String userMsg = "Hi " + booking.getCustomerName() +
                ", your booking #" + booking.getId() + " has been CANCELLED.\n" +
                "Pickup was: " + booking.getPickupDate() + " " + booking.getPickupTime() +
                " from " + booking.getFromLocation() + " to " + booking.getToLocation() +
                ". We hope to serve you next time — Vijay Travels.";

        notificationService.sendSms(booking.getMobileNo(), userMsg);
        notificationService.sendWhatsapp(booking.getMobileNo(), userMsg);

        // ---- Notify Admin ----
        String adminMsg = "Booking #" + booking.getId() + " CANCELLED by customer.\n" +
                "Name: " + booking.getCustomerName() + "\n" +
                "Mobile: " + booking.getMobileNo() + "\n" +
                "Vehicle: " + booking.getVehicleName() + "\n" +
                "Pickup: " + booking.getFromLocation() + " — " + booking.getPickupDate() + " " + booking.getPickupTime() + "\n" +
                "Drop: " + booking.getToLocation() + "\nFare: ₹" + booking.getFare();

        notificationService.sendSms(adminPhone, adminMsg);
        notificationService.sendWhatsapp(adminPhone, adminMsg);

        return Map.of("message", "Booking cancelled successfully");
    }




}

package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.model.RentalBooking;
import com.example.Adiyogi_Travels.model.RentalCar;
import com.example.Adiyogi_Travels.repository.RentalBookingRepository;
import com.example.Adiyogi_Travels.service.EmailService;
import com.example.Adiyogi_Travels.service.RentalCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rental")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RentalCarController {

    private final RentalCarService service;
    private final RentalBookingRepository bookingRepo;
    private final EmailService emailService;

    @GetMapping("/cars")
    public List<RentalCar> getCars() {
        return service.getAllCars();
    }

    @PostMapping("/book")
    public ResponseEntity<String> confirmBooking(@RequestBody RentalBooking booking) {
        bookingRepo.save(booking);


        String subjectUser = "Your Adiyogi Travels Booking Confirmation";
        String subjectAdmin = "New Rental Booking Received";

        String htmlUser = """
                <div style="font-family:Arial, sans-serif; background:#f9f9f9; padding:20px;">
                    <div style="max-width:600px; background:white; margin:auto; border-radius:10px; padding:20px; box-shadow:0 2px 10px rgba(0,0,0,0.1);">
                        <h2 style="color:#2c3e50;">Hi %s,</h2>
                        <p>Thank you for choosing <b>Adiyogi Travels</b>! Your booking has been confirmed.</p>
                        <table style="width:100%%; margin-top:15px; border-collapse:collapse;">
                            <tr><td><b>Car Type:</b></td><td>%s</td></tr>
                            <tr><td><b>Package:</b></td><td>%s</td></tr>
                            <tr><td><b>Total Fare:</b></td><td>₹%.2f</td></tr>
                            <tr><td><b>Pickup Location:</b></td><td>%s</td></tr>
                            <tr><td><b>City:</b></td><td>%s</td></tr>
                            <tr><td><b>Date:</b></td><td>%s</td></tr>
                            <tr><td><b>Time:</b></td><td>%s</td></tr>
                        </table>
                        <p style="margin-top:20px;">We’ll see you soon! </p>
                        <hr/>
                        <p style="font-size:12px; color:#777;">Adiyogi Travels © 2025</p>
                    </div>
                </div>
                """.formatted(
                booking.getName(),
                booking.getCarType(),
                booking.getPackageType(),
                booking.getTotalFare(),
                booking.getPickup(),
                booking.getCity(),
                booking.getPickupDate(),
                booking.getPickupTime()
        );

        String htmlAdmin = """
                <div style="font-family:Arial, sans-serif; background:#f3f4f6; padding:20px;">
                    <div style="max-width:600px; background:white; margin:auto; border-radius:10px; padding:20px; box-shadow:0 2px 10px rgba(0,0,0,0.1);">
                        <h2 style="color:#2c3e50;">New Rental Booking Received</h2>
                        <p>Here are the details:</p>
                        <table style="width:100%%; margin-top:15px; border-collapse:collapse;">
                            <tr><td><b>Name:</b></td><td>%s</td></tr>
                            <tr><td><b>Email:</b></td><td>%s</td></tr>
                            <tr><td><b>Mobile:</b></td><td>%s</td></tr>
                            <tr><td><b>Car Type:</b></td><td>%s</td></tr>
                            <tr><td><b>Package:</b></td><td>%s</td></tr>
                            <tr><td><b>Fare:</b></td><td>₹%.2f</td></tr>
                            <tr><td><b>Pickup:</b></td><td>%s</td></tr>
                            <tr><td><b>City:</b></td><td>%s</td></tr>
                            <tr><td><b>Date:</b></td><td>%s</td></tr>
                            <tr><td><b>Time:</b></td><td>%s</td></tr>
                        </table>
                        <p style="margin-top:20px;">Check dashboard for more details.</p>
                    </div>
                </div>
                """.formatted(
                booking.getName(),
                booking.getEmail(),
                booking.getMobile(),
                booking.getCarType(),
                booking.getPackageType(),
                booking.getTotalFare(),
                booking.getPickup(),
                booking.getCity(),
                booking.getPickupDate(),
                booking.getPickupTime()
        );


        emailService.sendBookingNotifications(
                booking.getEmail(),
                null,
                subjectUser,
                htmlUser,
                subjectAdmin,
                htmlAdmin
        );

        return ResponseEntity.ok("Booking confirmed & email sent!");
    }
}

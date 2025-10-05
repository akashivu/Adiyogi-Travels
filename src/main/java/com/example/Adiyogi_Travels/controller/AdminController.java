package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.model.Booking;
import com.example.Adiyogi_Travels.model.BookingStatus;
import com.example.Adiyogi_Travels.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final BookingRepository repo;


    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        return repo.findAll();
    }


    @PatchMapping("/bookings/{id}/status")
    public Booking updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Booking booking = repo.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.CONFIRMED);
        return repo.save(booking);
    }


    @DeleteMapping("/bookings/{id}")
    public Map<String, String> deleteBooking(@PathVariable Long id) {
        repo.deleteById(id);
        return Map.of("message", "Booking deleted successfully");
    }
}

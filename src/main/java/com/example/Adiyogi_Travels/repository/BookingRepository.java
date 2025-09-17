package com.example.Adiyogi_Travels.repository;

import com.example.Adiyogi_Travels.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}

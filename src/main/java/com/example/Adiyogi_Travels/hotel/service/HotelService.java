package com.example.Adiyogi_Travels.hotel.service;

import com.example.Adiyogi_Travels.hotel.client.BookingClient;
import com.example.Adiyogi_Travels.hotel.dto.HotelSearchRequest;
import com.example.Adiyogi_Travels.hotel.dto.HotelSearchResponse;
import org.springframework.stereotype.Service;

@Service
public class HotelService {

    private final BookingClient bookingClient;

    public HotelService(
            BookingClient bookingClient
    ) {
        this.bookingClient = bookingClient;
    }

    public HotelSearchResponse search(
            HotelSearchRequest request
    ) {
        return bookingClient.search(request);
    }
}
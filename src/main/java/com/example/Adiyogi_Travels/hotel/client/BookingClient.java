package com.example.Adiyogi_Travels.hotel.client;

import com.example.Adiyogi_Travels.hotel.dto.HotelResult;
import com.example.Adiyogi_Travels.hotel.dto.HotelSearchRequest;
import com.example.Adiyogi_Travels.hotel.dto.HotelSearchResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class BookingClient {

    public HotelSearchResponse search(
            HotelSearchRequest request
    ) {

        HotelResult hotel =
                new HotelResult(
                        UUID.randomUUID().toString(),
                        "Elixway Grand Hotel",
                        request.getDestination(),
                        "https://images.unsplash.com/photo-1566073771259-6a8506099945",
                        4.6,
                        1284,
                        6500.0,
                        request.getCurrency(),
                        "Deluxe Room",
                        null
                );

        return new HotelSearchResponse(
                UUID.randomUUID().toString(),
                "MOCK",
                List.of(hotel)
        );
    }
}
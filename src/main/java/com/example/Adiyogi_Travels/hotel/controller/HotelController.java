package com.example.Adiyogi_Travels.hotel.controller;

import com.example.Adiyogi_Travels.hotel.dto.HotelSearchRequest;
import com.example.Adiyogi_Travels.hotel.dto.HotelSearchResponse;
import com.example.Adiyogi_Travels.hotel.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotels")
@CrossOrigin
public class HotelController {

    private final HotelService hotelService;

    public HotelController(
            HotelService hotelService
    ) {
        this.hotelService = hotelService;
    }

    @PostMapping("/search")
    public ResponseEntity<HotelSearchResponse> search(
            @Valid
            @RequestBody
            HotelSearchRequest request
    ) {

        HotelSearchResponse response =
                hotelService.search(request);

        return ResponseEntity.ok(response);
    }
}
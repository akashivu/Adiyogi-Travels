package com.example.Adiyogi_Travels.controller;



import com.example.Adiyogi_Travels.flight.dto.FlightSearchRequest;
import com.example.Adiyogi_Travels.dto.flight.FlightSearchResponse;
import com.example.Adiyogi_Travels.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin
public class FlightController {

    private final FlightService flightService;

    public FlightController(
            FlightService flightService
    ) {
        this.flightService =
                flightService;
    }

    @PostMapping("/search")
    public ResponseEntity<FlightSearchResponse> search(
            @Valid
            @RequestBody
            FlightSearchRequest request
    ) {

        FlightSearchResponse response =
                flightService.search(request);

        return ResponseEntity.ok(response);
    }
}
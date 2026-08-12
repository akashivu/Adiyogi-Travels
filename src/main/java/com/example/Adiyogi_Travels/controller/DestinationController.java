package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.dto.google.GoogleNearbyPlace;
import com.example.Adiyogi_Travels.dto.google.GooglePlaceDetails;
import com.example.Adiyogi_Travels.model.Destination;
import com.example.Adiyogi_Travels.service.DestinationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(
            DestinationService destinationService
    ) {
        this.destinationService =
                destinationService;
    }

    @GetMapping
    public ResponseEntity<List<Destination>>
    getAllDestinations() {

        return ResponseEntity.ok(
                destinationService.getAllDestinations()
        );
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Destination>
    getDestination(
            @PathVariable String slug
    ) {

        return ResponseEntity.ok(
                destinationService
                        .getDestinationBySlug(slug)
        );
    }

    @PostMapping("/{slug}/google-sync")
    public ResponseEntity<Destination>
    syncGooglePlace(
            @PathVariable String slug
    ) {

        return ResponseEntity.ok(
                destinationService
                        .syncGooglePlace(slug)
        );
    }

    @GetMapping("/{slug}/google-details")
    public ResponseEntity<GooglePlaceDetails>
    getGooglePlaceDetails(
            @PathVariable String slug
    ) {

        return ResponseEntity.ok(
                destinationService
                        .getGooglePlaceDetails(slug)
        );
    }

    @GetMapping("/{slug}/nearby-attractions")
    public ResponseEntity<List<GoogleNearbyPlace>>
    getNearbyAttractions(
            @PathVariable String slug
    ) {

        return ResponseEntity.ok(
                destinationService
                        .getNearbyAttractions(slug)
        );
    }

    @GetMapping("/{slug}/nearby")
    public ResponseEntity<List<GoogleNearbyPlace>>
    getNearbyPlaces(
            @PathVariable String slug,
            @RequestParam String type
    ) {

        return ResponseEntity.ok(
                destinationService.getNearbyPlaces(
                        slug,
                        type
                )
        );
    }

    @GetMapping("/place-photo")
    public ResponseEntity<String> getPlacePhoto(
            @RequestParam String photoName
    ) {

        String photoUri =
                destinationService.getPlacePhoto(
                        photoName
                );

        if (photoUri == null ||
                photoUri.isBlank()) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(photoUri);
    }

}
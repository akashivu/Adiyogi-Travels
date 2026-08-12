package com.example.Adiyogi_Travels.service;

import com.example.Adiyogi_Travels.client.GooglePlacesClient;
import com.example.Adiyogi_Travels.dto.google.GooglePlace;
import com.example.Adiyogi_Travels.dto.google.GooglePlaceDetails;
import com.example.Adiyogi_Travels.model.Destination;
import com.example.Adiyogi_Travels.repository.DestinationRepository;
import org.springframework.stereotype.Service;
import com.example.Adiyogi_Travels.dto.google.GoogleNearbyPlace;

import java.util.List;
import java.util.List;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final GooglePlacesClient googlePlacesClient;

    public DestinationService(
            DestinationRepository destinationRepository,
            GooglePlacesClient googlePlacesClient
    ) {
        this.destinationRepository =
                destinationRepository;

        this.googlePlacesClient =
                googlePlacesClient;
    }

    public List<Destination> getAllDestinations() {

        return destinationRepository.findAll();
    }

    public Destination getDestinationBySlug(
            String slug
    ) {

        return destinationRepository
                .findBySlug(slug)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Destination not found: " + slug
                        )
                );
    }

    public Destination syncGooglePlace(
            String slug
    ) {

        Destination destination =
                getDestinationBySlug(slug);

        GooglePlace googlePlace =
                googlePlacesClient.searchDestination(
                        destination.getName(),
                        destination.getCountry()
                );

        if (googlePlace == null) {

            throw new RuntimeException(
                    "No Google Place found for destination: "
                            + destination.getName()
            );
        }

        destination.setGooglePlaceId(
                googlePlace.id()
        );

        return destinationRepository.save(
                destination
        );
    }
    public GooglePlaceDetails getGooglePlaceDetails(
            String slug
    ) {

        Destination destination =
                getDestinationBySlug(slug);

        if (destination.getGooglePlaceId() == null ||
                destination.getGooglePlaceId().isBlank()) {

            throw new RuntimeException(
                    "Google Place ID not available for: "
                            + destination.getName()
            );
        }

        return googlePlacesClient.getPlaceDetails(
                destination.getGooglePlaceId()
        );
    }
    public List<GoogleNearbyPlace> getNearbyAttractions(
            String slug
    ) {

        Destination destination =
                getDestinationBySlug(slug);

        if (destination.getLatitude() == null ||
                destination.getLongitude() == null) {

            throw new RuntimeException(
                    "Destination coordinates not available: "
                            + destination.getName()
            );
        }

        return googlePlacesClient.searchNearbyAttractions(
                destination.getLatitude(),
                destination.getLongitude()
        );
    }
}
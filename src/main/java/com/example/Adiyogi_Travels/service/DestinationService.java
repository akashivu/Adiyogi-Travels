package com.example.Adiyogi_Travels.service;

import com.example.Adiyogi_Travels.client.GooglePlacesClient;
import com.example.Adiyogi_Travels.dto.google.GooglePlace;
import com.example.Adiyogi_Travels.model.Destination;
import com.example.Adiyogi_Travels.repository.DestinationRepository;
import org.springframework.stereotype.Service;

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
}
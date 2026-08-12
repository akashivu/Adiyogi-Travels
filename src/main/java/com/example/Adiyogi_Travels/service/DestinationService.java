package com.example.Adiyogi_Travels.service;

import com.example.Adiyogi_Travels.model.Destination;
import com.example.Adiyogi_Travels.repository.DestinationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationService(
            DestinationRepository destinationRepository
    ) {
        this.destinationRepository = destinationRepository;
    }

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Destination getDestinationBySlug(String slug) {
        return destinationRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Destination not found: " + slug
                        )
                );
    }
}
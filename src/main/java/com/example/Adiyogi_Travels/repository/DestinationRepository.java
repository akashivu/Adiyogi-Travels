package com.example.Adiyogi_Travels.repository;

import com.example.Adiyogi_Travels.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DestinationRepository
        extends JpaRepository<Destination, Long> {

    Optional<Destination> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
package com.example.Adiyogi_Travels;

import com.example.Adiyogi_Travels.model.Destination;
import com.example.Adiyogi_Travels.repository.DestinationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AdiyogiTravelsApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                AdiyogiTravelsApplication.class,
                args
        );
    }

    @Bean
    CommandLineRunner seedDestinations(
            DestinationRepository repository
    ) {
        return args -> {

            if (!repository.existsBySlug("paris")) {

                repository.save(
                        Destination.builder()
                                .name("Paris")
                                .slug("paris")
                                .country("France")
                                .countryCode("FR")
                                .continent("Europe")
                                .shortDescription(
                                        "The city of art, culture and unforgettable experiences."
                                )
                                .description(
                                        "Paris is known for its iconic landmarks, museums, architecture, food and vibrant neighborhoods."
                                )
                                .recommendedDays(4)
                                .budgetLevel("MEDIUM_HIGH")
                                .latitude(48.8566)
                                .longitude(2.3522)
                                .heroImage(
                                        "https://images.unsplash.com/photo-1502602898657-3e91760cbb34"
                                )
                                .build()
                );
            }

            if (!repository.existsBySlug("dubai")) {

                repository.save(
                        Destination.builder()
                                .name("Dubai")
                                .slug("dubai")
                                .country("United Arab Emirates")
                                .countryCode("AE")
                                .continent("Asia")
                                .shortDescription(
                                        "A modern city of architecture, luxury and adventure."
                                )
                                .description(
                                        "Dubai combines futuristic architecture, luxury experiences, desert adventures and vibrant city life."
                                )
                                .recommendedDays(4)
                                .budgetLevel("HIGH")
                                .latitude(25.2048)
                                .longitude(55.2708)
                                .heroImage(
                                        "https://images.unsplash.com/photo-1512453979798-5ea266f8880c"
                                )
                                .build()
                );
            }

            if (!repository.existsBySlug("tokyo")) {

                repository.save(
                        Destination.builder()
                                .name("Tokyo")
                                .slug("tokyo")
                                .country("Japan")
                                .countryCode("JP")
                                .continent("Asia")
                                .shortDescription(
                                        "Where tradition meets technology."
                                )
                                .description(
                                        "Tokyo offers a unique combination of traditional culture, modern technology, food and entertainment."
                                )
                                .recommendedDays(5)
                                .budgetLevel("MEDIUM_HIGH")
                                .latitude(35.6762)
                                .longitude(139.6503)
                                .heroImage(
                                        "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf"
                                )
                                .build()
                );
            }
        };
    }
}
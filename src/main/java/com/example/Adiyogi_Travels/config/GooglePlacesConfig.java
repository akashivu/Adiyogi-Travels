package com.example.Adiyogi_Travels.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GooglePlacesConfig {

    @Bean
    public RestClient googlePlacesRestClient() {
        return RestClient.builder()
                .baseUrl("https://places.googleapis.com/v1")
                .build();
    }
}
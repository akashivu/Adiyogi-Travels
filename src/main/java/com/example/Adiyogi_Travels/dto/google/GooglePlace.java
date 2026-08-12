package com.example.Adiyogi_Travels.dto.google;

public record GooglePlace(
        String id,
        GoogleDisplayName displayName,
        String formattedAddress
) {
}
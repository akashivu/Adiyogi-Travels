package com.example.Adiyogi_Travels.dto.google;

public record GooglePlaceDetails(
        String id,
        GoogleDisplayName displayName,
        String formattedAddress,
        GoogleLocation location,
        Double rating,
        Integer userRatingCount,
        String websiteUri
) {
}

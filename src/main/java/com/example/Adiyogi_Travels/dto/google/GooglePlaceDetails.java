package com.example.Adiyogi_Travels.dto.google;

import java.util.List;

public record GooglePlaceDetails(
        String id,
        GoogleDisplayName displayName,
        String formattedAddress,
        GoogleLocation location,
        Double rating,
        Integer userRatingCount,
        String websiteUri,
        List<GooglePhoto> photos
) {
}
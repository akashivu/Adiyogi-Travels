package com.example.Adiyogi_Travels.dto.google;

import java.util.List;

public record GoogleNearbyPlace(
        String id,
        GoogleDisplayName displayName,
        String formattedAddress,
        GoogleLocation location,
        String primaryType,
        List<String> types,
        List<GooglePhoto> photos
) {
}
package com.example.Adiyogi_Travels.dto.google;

import java.util.List;

public record GoogleNearbySearchResponse(
        List<GoogleNearbyPlace> places
) {
}
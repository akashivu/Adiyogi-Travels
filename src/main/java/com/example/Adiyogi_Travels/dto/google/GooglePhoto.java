package com.example.Adiyogi_Travels.dto.google;

import java.util.List;

public record GooglePhoto(
        String name,
        Integer widthPx,
        Integer heightPx,
        List<GoogleAuthorAttribution> authorAttributions
) {
}
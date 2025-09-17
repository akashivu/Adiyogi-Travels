package com.example.Adiyogi_Travels.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuoteRequest {
    private String pickup;
    private String dropoff;
    private String tripType;
}

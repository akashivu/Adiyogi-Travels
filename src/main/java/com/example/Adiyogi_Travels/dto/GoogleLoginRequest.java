package com.example.Adiyogi_Travels.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginRequest {

    @NotBlank
    private String idToken;
}
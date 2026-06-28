package com.example.Adiyogi_Travels.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;

    private Long userId;

    private String fullName;

    private String email;

    private String role;

}
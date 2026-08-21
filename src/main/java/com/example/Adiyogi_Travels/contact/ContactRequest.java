package com.example.Adiyogi_Travels.contact;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be under 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Please enter a valid email address")
        @Size(max = 150, message = "Email must be under 150 characters")
        String email,

        @NotBlank(message = "Subject is required")
        @Size(max = 200, message = "Subject must be under 200 characters")
        String subject,

        @NotBlank(message = "Message is required")
        @Size(max = 5000, message = "Message must be under 5000 characters")
        String message,


        @Size(max = 0, message = "Invalid request")
        String website
) {
}
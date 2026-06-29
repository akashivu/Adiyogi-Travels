package com.example.Adiyogi_Travels.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.mail.from}")
    private String fromEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendHtmlEmail(String to, String subject, String htmlContent) {

        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> body = Map.of(
                "sender", Map.of(
                        "name", "AdiyogiCabz",
                        "email", fromEmail
                ),
                "to", List.of(
                        Map.of("email", to)
                ),
                "subject", subject,
                "htmlContent", htmlContent
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        try {

            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, entity, String.class);

            System.out.println("Brevo Response: " + response.getStatusCode());
            System.out.println("Brevo Body: " + response.getBody());

        } catch (Exception e) {

            System.err.println("Failed to send email via Brevo");
            e.printStackTrace();
        }
    }

    public void sendBookingNotifications(
            String userEmail,
            String adminEmailOverride,
            String subjectUser,
            String htmlUser,
            String subjectAdmin,
            String htmlAdmin) {

        sendHtmlEmail(userEmail, subjectUser, htmlUser);

        String admin =
                (adminEmailOverride != null && !adminEmailOverride.isBlank())
                        ? adminEmailOverride
                        : adminEmail;

        if (admin != null && !admin.isBlank()) {
            sendHtmlEmail(admin, subjectAdmin, htmlAdmin);
        }
    }
}
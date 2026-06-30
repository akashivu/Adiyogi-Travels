package com.example.Adiyogi_Travels.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.mail.from}")
    private String fromEmail;

    private final RestTemplate restTemplate = new RestTemplate();
    private final EmailTemplateService emailTemplateService;
    public EmailService(
            EmailTemplateService emailTemplateService
    ) {
        this.emailTemplateService = emailTemplateService;
    }
    public boolean sendHtmlEmail(String to, String subject, String htmlContent) {

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

            logger.info("Brevo Response: {}", response.getStatusCode());
            logger.debug("Brevo Body: {}", response.getBody());

            return response.getStatusCode().is2xxSuccessful();

        } catch (Exception e) {

            logger.error("Failed to send email via Brevo", e);
            return false;
        }
    }

    public boolean sendBookingNotifications(
            String userEmail,
            String adminEmailOverride,
            String subjectUser,
            String htmlUser,
            String subjectAdmin,
            String htmlAdmin) {

        boolean userSent = sendHtmlEmail(userEmail, subjectUser, htmlUser);

        String admin =
                (adminEmailOverride != null && !adminEmailOverride.isBlank())
                        ? adminEmailOverride
                        : adminEmail;

        boolean adminSent = true;

        if (admin != null && !admin.isBlank()) {
            adminSent = sendHtmlEmail(admin, subjectAdmin, htmlAdmin);
        }

        return userSent && adminSent;
    }
    public boolean sendOtpEmail(
            String email,
            String fullName,
            String otp
    ) {

        String subject = "Verify Your Email | AdiyogiCabz";

        String html =
                emailTemplateService
                        .buildOtpEmail(fullName, otp);

        return sendHtmlEmail(
                email,
                subject,
                html
        );

    }
    public void sendForgotPasswordOtpEmail(
            String email,
            String fullName,
            String otp
    ) {

        String subject = "Reset Your Password | AdiyogiCabz";

        String html = emailTemplateService
                .buildForgotPasswordOtpEmail(fullName, otp);

        sendHtmlEmail(
                email,
                subject,
                html
        );
    }
}
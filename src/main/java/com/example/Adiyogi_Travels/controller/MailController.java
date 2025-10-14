package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MailController {

    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> data) {
        try {
            String fromName = data.getOrDefault("name", "Unknown");
            String fromEmail = data.getOrDefault("email", "no-reply@adiyogi-travels.com");
            String phone = data.getOrDefault("phone", "N/A");
            String messageText = data.getOrDefault("message", "");

            String subject = "New message from " + fromName;
            String htmlBody = "<h3>Contact Form Submission</h3>"
                    + "<p><b>Name:</b> " + fromName + "</p>"
                    + "<p><b>Email:</b> " + fromEmail + "</p>"
                    + "<p><b>Phone:</b> " + phone + "</p>"
                    + "<p><b>Message:</b><br>" + messageText + "</p>";


            emailService.sendHtmlEmail("vijaytourstravels6158@gmail.com", subject, htmlBody);

            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }

}

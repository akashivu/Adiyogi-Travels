package com.example.Adiyogi_Travels.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MailController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> data) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(data.get("email"));
            message.setTo("vijaytourstravels6158@gmail.com");
            message.setSubject("New message from " + data.get("name"));
            message.setText(
                    "Name: " + data.get("name") + "\n" +
                            "Email: " + data.get("email") + "\n" +
                            "Phone: " + data.getOrDefault("phone", "N/A") + "\n\n" +
                            data.get("message")
            );
            mailSender.send(message);
            return ResponseEntity.ok("Email sent");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send email");
        }
    }
}

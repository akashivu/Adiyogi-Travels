package com.example.Adiyogi_Travels.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.admin.email:}")
    private String adminEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendPlainText(String to, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromEmail);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);
    }


    @Async
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }


    public void sendBookingNotifications(String userEmail, String adminEmailOverride,
                                         String subjectUser, String htmlUser,
                                         String subjectAdmin, String htmlAdmin) {

        sendHtmlEmail(userEmail, subjectUser, htmlUser);


        String admin = (adminEmailOverride != null && !adminEmailOverride.isBlank()) ? adminEmailOverride : adminEmail;
        if (admin != null && !admin.isBlank()) {
            sendHtmlEmail(admin, subjectAdmin, htmlAdmin);
        } else {

            sendHtmlEmail(fromEmail, subjectAdmin, htmlAdmin);
        }
    }
}

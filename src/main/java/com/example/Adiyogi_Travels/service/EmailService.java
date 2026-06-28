package com.example.Adiyogi_Travels.service;

import jakarta.mail.MessagingException;
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

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.mail.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendPlainText(String to, String subject, String body) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            System.out.println("Plain email sent to: " + to);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Async
    public void sendHtmlEmail(String to, String subject, String htmlBody) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);

            System.out.println("HTML email sent to: " + to);

        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    public void sendBookingNotifications(
            String userEmail,
            String adminEmailOverride,
            String subjectUser,
            String htmlUser,
            String subjectAdmin,
            String htmlAdmin
    ) {

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
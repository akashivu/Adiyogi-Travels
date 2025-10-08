package com.example.Adiyogi_Travels.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${spring.mail.username:no-reply@adiyogi-travels.com}")
    private String fromEmail;

    private SendGrid buildClient() {
        return new SendGrid(sendGridApiKey);
    }

    @Async
    public void sendPlainText(String to, String subject, String body) {
        sendEmail(to, subject, body, "text/plain");
    }

    @Async
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        sendEmail(to, subject, htmlBody, "text/html");
    }

    private void sendEmail(String to, String subject, String contentValue, String type) {
        Email from = new Email(fromEmail);
        Email recipient = new Email(to);
        Content content = new Content(type, contentValue);
        Mail mail = new Mail(from, subject, recipient, content);

        SendGrid sg = buildClient();
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("SendGrid response: " + response.getStatusCode());
        } catch (IOException ex) {
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


package com.example.Adiyogi_Travels.contact;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String receiverEmail;

    public ContactService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendContactMessage(ContactRequest request) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(receiverEmail);

        mail.setReplyTo(request.email());

        mail.setSubject(
                "Elixway Contact: " + request.subject()
        );

        mail.setText(
                """
                New message from Elixway Contact Form
                =====================================

                Name:
                %s

                Email:
                %s

                Subject:
                %s

                Message:
                %s

                =====================================
                This message was submitted through
                the Elixway website.
                """.formatted(
                        request.name(),
                        request.email(),
                        request.subject(),
                        request.message()
                )
        );

        mailSender.send(mail);
    }
}
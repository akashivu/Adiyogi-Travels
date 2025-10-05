package com.example.Adiyogi_Travels.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Value("${twilio.accountSid}")
    private String accountSid;
    @Value("${twilio.authToken}")
    private String authToken;
    @Value("${twilio.smsFrom}")
    private String smsFrom;
    @Value("${twilio.whatsappFrom}")
    private String whatsappFrom;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendSms(String to, String body) {
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(smsFrom),
                body
        ).create();
    }

    public void sendWhatsapp(String to, String body) {
        Message.creator(
                new PhoneNumber("whatsapp:" + to),
                new PhoneNumber(whatsappFrom),
                body
        ).create();
    }
}

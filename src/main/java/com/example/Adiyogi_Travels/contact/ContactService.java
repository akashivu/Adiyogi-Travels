package com.example.Adiyogi_Travels.contact;

import com.example.Adiyogi_Travels.repository.ContactMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactService {

    private final ContactMessageRepository repository;

    public ContactService(ContactMessageRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ContactMessage saveMessage(ContactRequest request) {

        ContactMessage contactMessage =
                new ContactMessage(
                        request.name().trim(),
                        request.email().trim().toLowerCase(),
                        request.subject().trim(),
                        request.message().trim()
                );

        return repository.save(contactMessage);
    }
}
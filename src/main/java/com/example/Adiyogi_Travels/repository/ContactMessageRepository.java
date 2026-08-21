package com.example.Adiyogi_Travels.repository;

import com.example.Adiyogi_Travels.contact.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactMessageRepository
        extends JpaRepository<ContactMessage, Long> {
}
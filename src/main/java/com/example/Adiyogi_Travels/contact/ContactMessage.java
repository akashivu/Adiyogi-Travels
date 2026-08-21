package com.example.Adiyogi_Travels.contact;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_messages")
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 200)
    private String subject;

    @Column(nullable = false, length = 5000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public ContactMessage() {
    }

    public ContactMessage(
            String name,
            String email,
            String subject,
            String message
    ) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
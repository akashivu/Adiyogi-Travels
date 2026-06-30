package com.example.Adiyogi_Travels.repository;

import com.example.Adiyogi_Travels.model.EmailVerification;
import com.example.Adiyogi_Travels.model.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByEmail(String email);

    void deleteByEmail(String email);

    Optional<EmailVerification> findByEmailAndPurpose(
            String email,
            OtpPurpose purpose
    );

    void deleteByEmailAndPurpose(
            String email,
            OtpPurpose purpose
    );

}
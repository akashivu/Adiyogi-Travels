package com.example.Adiyogi_Travels.repository;

import com.example.Adiyogi_Travels.model.EmailVerification;
import com.example.Adiyogi_Travels.model.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByEmail(String email);

    @Modifying
    @Transactional
    void deleteByEmail(String email);

    Optional<EmailVerification> findByEmailAndPurpose(
            String email,
            OtpPurpose purpose
    );
    @Modifying
    @Transactional
    void deleteByEmailAndPurpose(
            String email,
            OtpPurpose purpose
    );

}
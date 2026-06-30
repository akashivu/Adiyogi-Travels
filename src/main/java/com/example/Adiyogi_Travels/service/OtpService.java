package com.example.Adiyogi_Travels.service;

import com.example.Adiyogi_Travels.model.EmailVerification;
import com.example.Adiyogi_Travels.model.OtpPurpose;
import com.example.Adiyogi_Travels.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final EmailVerificationRepository emailVerificationRepository;

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generate a secure 6-digit OTP
     */
    public String generateOtp() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }

    /**
     * Save or replace OTP for an email
     */
    public EmailVerification saveOtp(
            String fullName,
            String email,
            String encodedPassword,
            String otp,
            OtpPurpose purpose
    ) {

        emailVerificationRepository
                .findByEmailAndPurpose(email, purpose)
                .ifPresent(emailVerificationRepository::delete);

        EmailVerification verification =
                EmailVerification.builder()
                        .fullName(fullName)
                        .email(email)
                        .password(encodedPassword)
                        .otp(otp)
                        .purpose(purpose)
                        .expiresAt(LocalDateTime.now().plusMinutes(10))
                        .verified(false)
                        .build();

        return emailVerificationRepository.save(verification);
    }

    /**
     * Verify OTP
     */
    public EmailVerification verifyOtp(
            String email,
            String otp,
            OtpPurpose purpose
    ) {

        EmailVerification verification =
                emailVerificationRepository
                        .findByEmailAndPurpose(email, purpose)
                        .orElseThrow(() ->
                                new RuntimeException("OTP not found."));

        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {

            throw new RuntimeException("OTP has expired.");
        }

        if (!verification.getOtp().equals(otp)) {

            throw new RuntimeException("Invalid OTP.");
        }

        verification.setVerified(true);

        return emailVerificationRepository.save(verification);
    }
    /**
     * Get pending verification by email
     */
    public EmailVerification getVerification(
            String email,
            OtpPurpose purpose
    ) {

        return emailVerificationRepository
                .findByEmailAndPurpose(email, purpose)
                .orElseThrow(() ->
                        new RuntimeException("Verification request not found."));
    }
    /**
     * Remove OTP after successful registration
     */
    public void deleteOtp(
            String email,
            OtpPurpose purpose
    ) {

        emailVerificationRepository
                .deleteByEmailAndPurpose(email, purpose);

    }
    public String resendOtp(
            String email,
            OtpPurpose purpose
    ) {

        EmailVerification verification = emailVerificationRepository
                .findByEmailAndPurpose(email, purpose)
                .orElseThrow(() ->
                        new RuntimeException("Registration request not found."));

        String otp = generateOtp();

        verification.setOtp(otp);
        verification.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        verification.setVerified(false);

        emailVerificationRepository.save(verification);

        return otp;
    }

}
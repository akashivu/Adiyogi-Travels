package com.example.Adiyogi_Travels.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "email_verifications",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"email", "purpose"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Store the BCrypt hashed password.
     * User will be created only after OTP verification.
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * 6 digit OTP
     */
    @Column(nullable = false, length = 6)
    private String otp;

    /**
     * OTP Expiry Time
     */
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Whether OTP is verified
     */
    @Builder.Default
    private Boolean verified = false;

    /**
     * Record creation time
     */
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OtpPurpose purpose = OtpPurpose.REGISTRATION;

}
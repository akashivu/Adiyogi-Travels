package com.example.Adiyogi_Travels.controller;

import com.example.Adiyogi_Travels.dto.LoginRequest;
import com.example.Adiyogi_Travels.dto.RegisterRequest;
import com.example.Adiyogi_Travels.dto.UserResponse;
import com.example.Adiyogi_Travels.model.User;
import com.example.Adiyogi_Travels.repository.UserRepository;
import com.example.Adiyogi_Travels.security.JwtService;
import com.example.Adiyogi_Travels.service.EmailService;
import com.example.Adiyogi_Travels.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;
import com.example.Adiyogi_Travels.dto.LoginResponse;
import com.example.Adiyogi_Travels.dto.VerifyOtpRequest;
import com.example.Adiyogi_Travels.model.EmailVerification;
import com.example.Adiyogi_Travels.dto.ForgotPasswordRequest;
import com.example.Adiyogi_Travels.dto.VerifyForgotPasswordOtpRequest;
import com.example.Adiyogi_Travels.dto.ResetPasswordRequest;
import com.example.Adiyogi_Travels.model.OtpPurpose;
import com.example.Adiyogi_Travels.dto.GoogleLoginRequest;
import com.example.Adiyogi_Travels.service.GoogleAuthService;
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final GoogleAuthService googleAuthService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {

        // Already registered?
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Email already registered.");
        }

        // Generate OTP
        String otp = otpService.generateOtp();

        // Store pending registration
        otpService.saveOtp(
                req.getFullName(),
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),
                otp,
                OtpPurpose.REGISTRATION
        );

        // Send OTP email
        emailService.sendOtpEmail(
                req.getEmail(),
                req.getFullName(),
                otp
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "OTP sent successfully to your email."
                )
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request
    ) {

        // Verify OTP (throws exception if invalid/expired)
        otpService.verifyOtp(
                request.getEmail(),
                request.getOtp(),
                OtpPurpose.REGISTRATION
        );

        // Get stored registration details
        EmailVerification verification =
                otpService.getVerification(
                        request.getEmail(),
                        OtpPurpose.REGISTRATION
                );

        // Extra safety: don't create duplicate users
        if (userRepository.findByEmail(verification.getEmail()).isPresent()) {

            otpService.deleteOtp(
                    verification.getEmail(),
                    OtpPurpose.REGISTRATION
            );

            return ResponseEntity.badRequest()
                    .body("Email already registered.");
        }

        // Create actual user
        User user = User.builder()
                .fullName(verification.getFullName())
                .email(verification.getEmail())
                .password(verification.getPassword())
                .role("USER")
                .build();

        userRepository.save(user);


        otpService.deleteOtp(
                verification.getEmail(),
                OtpPurpose.REGISTRATION
        );

        // Generate JWT
        String jwtToken = jwtService.generateToken(user);

        LoginResponse response = LoginResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return ResponseEntity.ok(response);
    }
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {

        EmailVerification verification =
                otpService.getVerification(
                        email,
                        OtpPurpose.REGISTRATION
                );

        String otp = otpService.resendOtp(
                email,
                OtpPurpose.REGISTRATION
        );

        emailService.sendOtpEmail(
                verification.getEmail(),
                verification.getFullName(),
                otp
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "OTP sent successfully."
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<User> userOpt = userRepository.findByEmail(req.getEmail());

        if (userOpt.isEmpty() || !passwordEncoder.matches(req.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
        User user = userOpt.get();


        String jwtToken = jwtService.generateToken(user);




        LoginResponse response = LoginResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return ResponseEntity.ok(response);

    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            Authentication authentication
    ) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return ResponseEntity.ok(response);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("No account found with this email."));

        String otp = otpService.generateOtp();

        otpService.saveOtp(
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                otp,
                OtpPurpose.FORGOT_PASSWORD
        );

        emailService.sendForgotPasswordOtpEmail(
                user.getEmail(),
                user.getFullName(),
                otp
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Password reset OTP sent successfully."
                )
        );
    }
    @PostMapping("/verify-forgot-password-otp")
    public ResponseEntity<?> verifyForgotPasswordOtp(
            @Valid @RequestBody VerifyForgotPasswordOtpRequest request) {

        otpService.verifyOtp(
                request.getEmail(),
                request.getOtp(),
                OtpPurpose.FORGOT_PASSWORD
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "OTP verified successfully."
                )
        );
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        EmailVerification verification =
                otpService.getVerification(
                        request.getEmail(),
                        OtpPurpose.FORGOT_PASSWORD
                );

        if (!verification.getVerified()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Please verify the OTP first."
                    ));
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found."));

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        otpService.deleteOtp(
                request.getEmail(),
                OtpPurpose.FORGOT_PASSWORD
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Password updated successfully."
                )
        );
    }
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(
            @Valid @RequestBody GoogleLoginRequest request
    ) {

        try {

            LoginResponse response =
                    googleAuthService.loginWithGoogle(
                            request.getIdToken()
                    );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }
}
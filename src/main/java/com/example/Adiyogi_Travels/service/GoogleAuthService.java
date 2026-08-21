package com.example.Adiyogi_Travels.service;

import com.example.Adiyogi_Travels.dto.LoginResponse;
import com.example.Adiyogi_Travels.model.User;
import com.example.Adiyogi_Travels.repository.UserRepository;
import com.example.Adiyogi_Travels.security.JwtService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public LoginResponse loginWithGoogle(String idToken) {

        FirebaseToken decodedToken;

        try {
            decodedToken =
                    FirebaseAuth
                            .getInstance()
                            .verifyIdToken(idToken);

        } catch (FirebaseAuthException e) {

            throw new RuntimeException(
                    "Invalid Google authentication token."
            );
        }

        String firebaseUid =
                decodedToken.getUid();

        String email =
                decodedToken.getEmail();

        String name =
                decodedToken.getName();

        String picture =
                decodedToken.getPicture();

        Boolean emailVerified =
                decodedToken.isEmailVerified();

        if (email == null || email.isBlank()) {
            throw new RuntimeException(
                    "Google account does not contain an email."
            );
        }

        if (!Boolean.TRUE.equals(emailVerified)) {
            throw new RuntimeException(
                    "Google email is not verified."
            );
        }


        User user =
                userRepository
                        .findByFirebaseUid(firebaseUid)
                        .orElse(null);


        if (user == null) {

            user =
                    userRepository
                            .findByEmail(email)
                            .orElse(null);
        }


        if (user == null) {

            user = User.builder()
                    .fullName(
                            name != null && !name.isBlank()
                                    ? name
                                    : "Elixway User"
                    )
                    .email(email)
                    .password(null)
                    .role("USER")
                    .firebaseUid(firebaseUid)
                    .authProvider("GOOGLE")
                    .build();

            userRepository.save(user);
        }

        else {

            if (user.getFirebaseUid() == null) {

                user.setFirebaseUid(firebaseUid);
                user.setAuthProvider("GOOGLE");

                if (
                        name != null &&
                                !name.isBlank() &&
                                (
                                        user.getFullName() == null ||
                                                user.getFullName().isBlank()
                                )
                ) {
                    user.setFullName(name);
                }

                userRepository.save(user);
            }
        }


        String jwtToken =
                jwtService.generateToken(user);

        return LoginResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
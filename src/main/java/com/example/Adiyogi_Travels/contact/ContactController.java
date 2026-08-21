package com.example.Adiyogi_Travels.contact;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    /*
     * Simple in-memory rate limiter.
     *
     * One IP can submit only once every 60 seconds.
     */
    private final Map<String, Long> requestTimes =
            new ConcurrentHashMap<>();

    private static final long RATE_LIMIT_MILLIS = 60_000;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(
            @Valid @RequestBody ContactRequest request,
            HttpServletRequest httpRequest
    ) {

        /*
         * -----------------------------
         * 1. Honeypot protection
         * -----------------------------
         *
         * If a bot fills "website",
         * silently reject the request.
         */
        if (request.website() != null
                && !request.website().isBlank()) {

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Message received."
                    )
            );
        }

        /*
         * -----------------------------
         * 2. Identify client
         * -----------------------------
         */
        String clientIp = getClientIp(httpRequest);

        /*
         * -----------------------------
         * 3. Rate limiting
         * -----------------------------
         */
        long now = System.currentTimeMillis();

        Long previousRequest =
                requestTimes.get(clientIp);

        if (previousRequest != null
                && now - previousRequest < RATE_LIMIT_MILLIS) {

            long secondsRemaining =
                    (RATE_LIMIT_MILLIS -
                            (now - previousRequest)) / 1000;

            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(
                            Map.of(
                                    "success", false,
                                    "message",
                                    "Please wait "
                                            + secondsRemaining
                                            + " seconds before sending another message."
                            )
                    );
        }

        /*
         * Store request time before sending email.
         */
        requestTimes.put(clientIp, now);

        /*
         * -----------------------------
         * 4. Send email
         * -----------------------------
         */
        try {

            contactService.sendContactMessage(request);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message",
                            "Your message has been sent successfully."
                    )
            );

        } catch (Exception exception) {

            /*
             * Remove rate-limit entry if
             * email sending failed.
             */
            requestTimes.remove(clientIp);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            Map.of(
                                    "success", false,
                                    "message",
                                    "Unable to send your message right now. Please try again later."
                            )
                    );
        }
    }

    /*
     * Get real client IP when the application
     * is behind a proxy/load balancer.
     */
    private String getClientIp(HttpServletRequest request) {

        String forwardedFor =
                request.getHeader("X-Forwarded-For");

        if (forwardedFor != null
                && !forwardedFor.isBlank()) {

            return forwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}
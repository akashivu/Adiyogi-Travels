package com.example.Adiyogi_Travels.config;

import com.example.Adiyogi_Travels.security.CustomUserDetailsService;
import com.example.Adiyogi_Travels.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter
                         ) {
        this.jwtAuthFilter = jwtAuthFilter;

    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,  AuthenticationProvider authenticationProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/api/account/login",
                                "/api/account/register",
                                "/api/account/verify-otp",
                                "/api/account/resend-otp",
                                "/api/account/forgot-password",
                                "/api/account/verify-forgot-password-otp",
                                "/api/account/reset-password"
                        ).permitAll()

                        .requestMatchers("/api/account/me")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/quotes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/quotes").permitAll()

                        .requestMatchers("/api/auth/**", "/api/quotes").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/send-email").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/*/cancel")
                        .hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/bookings/my-bookings").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/bookings/confirm").permitAll()
                        .requestMatchers("/api/rental/**").permitAll()
                        .requestMatchers("/api/airport/**").permitAll()
                        .requestMatchers("/api/airporttab/**").permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/bookings/estimate"
                        ).permitAll()
                        .anyRequest().authenticated()

                )
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();


        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "https://adiyogicabz.com",
                "https://www.adiyogicabz.com",
                "https://vijaytravels.netlify.app",
                "https://adiyogi-travels.onrender.com"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(CustomUserDetailsService customUserDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}


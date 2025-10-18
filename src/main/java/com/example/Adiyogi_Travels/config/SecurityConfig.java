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
                        .requestMatchers("/api/account/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/quotes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/quotes").permitAll()

                        .requestMatchers("/api/auth/**", "/api/quotes", "/api/bookings/confirm").permitAll()
                        .requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers("/api/send-email").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/*/cancel").permitAll()
                        .requestMatchers("/api/bookings/my-bookings").authenticated()
                        .requestMatchers("/api/bookings/confirm").authenticated()
                        .requestMatchers("/api/rental/**").permitAll()
                        .requestMatchers("/api/airport/**").permitAll()
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

        config.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "https://adiyogi-travels.onrender.com",
                "https://adiyogicabz.com",
                "https://www.adiyogicabz.com",
                "https://vijaytravels.netlify.app"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

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


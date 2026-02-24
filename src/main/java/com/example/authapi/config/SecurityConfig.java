package com.example.authapi.config; // Replace with your actual package name

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // If you have a custom JWT filter class, autowire it here:
    // private final JwtAuthenticationFilter jwtAuthFilter;
    // public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
    //     this.jwtAuthFilter = jwtAuthFilter;
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF since we are using JWTs
                .csrf(csrf -> csrf.disable())

                // 2. Configure Route Permissions
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to all static frontend files
                        .requestMatchers("/", "/*.html", "/css/**", "/js/**", "/images/**").permitAll()
                        // Allow public access to registration and login APIs
                        .requestMatchers("/api/auth/**").permitAll()
                        // Everything else requires the user to be logged in
                        .anyRequest().authenticated()
                )

                // 3. Set Session Management to Stateless (Crucial for JWT Authentication)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // 4. Register the JWT Filter (Uncomment this once your JwtAuthenticationFilter is injected above)
        // http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Enforces BCrypt hashing for secure password storage in PostgreSQL
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
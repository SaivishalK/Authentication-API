package com.example.authapi.Util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component // This tells Spring to manage this class so we can inject it elsewhere
public class JwtUtil {

    // The Secret Key used to digitally sign the token.
    // In a real job, NEVER hardcode this. It should live in application.properties!
    // It MUST be at least 32 characters long for HS256 encryption.
    private final String SECRET_KEY = "my-super-secret-key-my-super-secret-key";

    // Helper method to format our secret string into a cryptographic Key object
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // 1. ISSUE THE PASSPORT (Generate Token)
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // The "Subject" of the token is the user's email
                .setIssuedAt(new Date(System.currentTimeMillis())) // Timestamp of right now
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expires in 10 hours
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign it with our secret key
                .compact(); // Build it into a single String
    }

    // 2. READ THE PASSPORT (Extract Email)
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 3. VERIFY THE PASSPORT (Validate Token)
    public boolean isTokenValid(String token) {
        try {
            // If the parser successfully reads the token without throwing an error, it is valid!
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // If it throws an error (e.g., expired, fake signature), it is invalid
            return false;
        }
    }
}
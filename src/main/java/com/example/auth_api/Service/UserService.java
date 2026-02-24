package com.example.authapi.service;

import com.example.authapi.model.User;
import com.example.authapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 1. Inject the BCrypt encoder we defined in SecurityConfig
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Logic for registering a user
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        // 2. Hash the password BEFORE saving to the database
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    // Logic for login
    public User loginUser(String email, String rawPassword) {
        Optional<User> foundUser = userRepository.findByEmail(email);

        if (foundUser.isPresent()) {
            User user = foundUser.get();

            // 3. Use BCrypt to safely compare the raw text to the database hash
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return user; // Login success
            }
        }
        return null; // Login failed
    }
    // Fetch a user by their email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
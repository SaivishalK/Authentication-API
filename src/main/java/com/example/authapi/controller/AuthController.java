package com.example.authapi.controller;

import com.example.authapi.model.User;
import com.example.authapi.service.UserService;
import com.example.authapi.Util.JwtUtil; // IMPORT THE NEW UTILITY
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    // 1. Inject the Passport Office!
    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint: POST /api/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            // It is okay to return the user on signup, but we usually hide the password
            registeredUser.setPassword("");
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint: POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        User user = userService.loginUser(email, password);

        if (user != null) {
            // 2. The user is valid! Generate their JWT passport.
            String token = jwtUtil.generateToken(user.getEmail());

            // 3. Package the token into a neat JSON object to send back to the browser
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
    // Endpoint: GET /api/auth/me
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@RequestHeader(value = "Authorization", required = false) String authHeader) {

        // 1. Check if the passport is missing
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid passport!");
        }

        // 2. Extract the actual token (Remove the word "Bearer ")
        String token = authHeader.substring(7);

        // 3. Verify the passport is real and not expired
        if (jwtUtil.isTokenValid(token)) {
            String email = jwtUtil.extractEmail(token);
            User user = userService.getUserByEmail(email);

            // 4. Return the user's name securely (NEVER return the password!)
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("fullName", user.getFullName());
            userInfo.put("email", user.getEmail());

            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(401).body("Fake or expired passport!");
        }
    }
}
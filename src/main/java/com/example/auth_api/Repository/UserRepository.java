package com.example.authapi.repository;

import com.example.authapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository gives us methods like save(), findAll(), delete(), etc.
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom method to find a user by email
    // Spring automatically generates the SQL for this based on the method name!
    Optional<User> findByEmail(String email);
}

package com.example.authapi.model;

import jakarta.persistence.*;
        import lombok.Data; // Lombok handles Getters/Setters automatically

@Entity
@Data
@Table(name = "users") // This will create a table named "users" in Postgres
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String fullName;
}

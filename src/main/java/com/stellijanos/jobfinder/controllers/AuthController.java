package com.stellijanos.jobfinder.controllers;


import com.stellijanos.jobfinder.models.Company;
import com.stellijanos.jobfinder.models.User;
import com.stellijanos.jobfinder.repositories.CompanyRepository;
import com.stellijanos.jobfinder.repositories.UserRepository;
import com.stellijanos.jobfinder.responses.AuthResponse;
import com.stellijanos.jobfinder.responses.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;


    @Autowired
    public AuthController(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @PostMapping("/register-user")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already exists!");
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setToken(UUID.randomUUID().toString());
        user.setToken_expires_at(LocalDateTime.now().plusDays(30));
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/register-company")
    public ResponseEntity<?> registerCompany(@RequestBody Company company) {
        if (companyRepository.findByEmail(company.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already exists!");
        }

        company.setPassword(BCrypt.hashpw(company.getPassword(), BCrypt.gensalt()));
        company.setToken(UUID.randomUUID().toString());
        company.setToken_expires_at(LocalDateTime.now().plusDays(30));
        companyRepository.save(company);

        return ResponseEntity.ok("Company registered successfully!");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user != null && BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setToken_expires_at(LocalDateTime.now().plusDays(30));
            userRepository.save(user);
            return ResponseEntity.ok(new AuthResponse(user.getToken(), "user"));
        }

        Company company = companyRepository.findByEmail(request.getEmail());
        if (company != null && BCrypt.checkpw(request.getPassword(), company.getPassword())) {
            company.setToken(UUID.randomUUID().toString());
            company.setToken_expires_at(LocalDateTime.now().plusDays(30));
            companyRepository.save(company);
            return ResponseEntity.ok(new AuthResponse(company.getToken(), "company"));
        }

        return ResponseEntity.badRequest().body("Incorrect email or password!");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String token) {
        User user = userRepository.findByToken(token);
        if (user != null) {
            user.setToken_expires_at(LocalDateTime.now());
            userRepository.save(user);
            return ResponseEntity.ok("User logged out successfully");
        }

        Company company = companyRepository.findByToken(token);
        if (company != null) {
            company.setToken_expires_at(LocalDateTime.now());
            companyRepository.save(company);
            return ResponseEntity.ok("Company logged out successfully");
        }

        return ResponseEntity.badRequest().body("Invalid token");
    }

    @GetMapping("/isLoggedIn")
    public ResponseEntity<?> isLoggedIn(@RequestParam String token) {
        User user = userRepository.findByToken(token);
        if (user != null && user.getToken_expires_at().isAfter(LocalDateTime.now())) {
            user.setToken_expires_at(LocalDateTime.now().plusDays(30));
            userRepository.save(user);
            return ResponseEntity.ok("User is logged in");
        }

        Company company = companyRepository.findByToken(token);
        if (company != null && company.getToken_expires_at().isAfter(LocalDateTime.now())) {
            company.setToken_expires_at(LocalDateTime.now().plusDays(30));
            companyRepository.save(company);
            return ResponseEntity.ok("Company is logged in");
        }

        return ResponseEntity.badRequest().body("Not logged in");
    }
}

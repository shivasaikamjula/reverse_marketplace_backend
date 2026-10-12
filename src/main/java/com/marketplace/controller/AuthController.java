package com.marketplace.controller;

import com.marketplace.dto.ApiResponse;
import com.marketplace.dto.AuthRequest;
import com.marketplace.dto.AuthResponse;
import com.marketplace.dto.RegisterRequest;
import com.marketplace.model.User;
import com.marketplace.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for User Authentication (Registration, Login, Profile).
 *
 * Viva Notes:
 * - @RestController: Marks the class as a REST controller where methods return JSON response bodies directly.
 * - @RequestMapping("/api/auth"): Base URL prefix for all endpoints in this controller.
 * - @Valid: Triggers Bean Validation on incoming request bodies.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService contentService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = contentService.register(request);
        return ResponseEntity.ok(ApiResponse.ok("User registered successfully!", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = contentService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful!", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Not authenticated"));
        }
        User user = contentService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("User profile retrieved", user));
    }
}

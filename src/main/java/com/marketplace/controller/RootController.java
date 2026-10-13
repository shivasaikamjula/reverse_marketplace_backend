package com.marketplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping({"/", "/api", "/health"})
    public ResponseEntity<Map<String, Object>> rootHealthCheck() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("service", "Reverse Marketplace Backend REST API");
        response.put("version", "1.0.0");
        response.put("database", "MySQL (Aiven Cloud)");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("message", "Reverse Marketplace Backend is running successfully!");

        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("categories", "/api/categories");
        endpoints.put("publicRequirements", "/api/requirements/public");
        endpoints.put("login", "/api/auth/login");
        endpoints.put("register", "/api/auth/register");
        response.put("endpoints", endpoints);

        return ResponseEntity.ok(response);
    }
}

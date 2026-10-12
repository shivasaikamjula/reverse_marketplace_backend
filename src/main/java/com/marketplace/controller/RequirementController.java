package com.marketplace.controller;

import com.marketplace.dto.ApiResponse;
import com.marketplace.dto.RequirementRequest;
import com.marketplace.dto.RequirementResponse;
import com.marketplace.model.User;
import com.marketplace.service.AuthService;
import com.marketplace.service.RequirementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requirements")
public class RequirementController {

    @Autowired
    private RequirementService requirementService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<ApiResponse<RequirementResponse>> createRequirement(
            @Valid @RequestBody RequirementRequest request,
            Authentication authentication) {
        User customer = authService.getUserByEmail(authentication.getName());
        RequirementResponse response = requirementService.createRequirement(request, customer);
        return ResponseEntity.ok(ApiResponse.ok("Service requirement posted successfully!", response));
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<RequirementResponse>>> getPublicRequirements() {
        List<RequirementResponse> list = requirementService.getAllOpenRequirements();
        return ResponseEntity.ok(ApiResponse.ok("Public service requirements", list));
    }

    @GetMapping("/open")
    public ResponseEntity<ApiResponse<List<RequirementResponse>>> getOpenRequirements() {
        List<RequirementResponse> list = requirementService.getAllOpenRequirements();
        return ResponseEntity.ok(ApiResponse.ok("Open service requirements", list));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<RequirementResponse>>> getMyRequirements(Authentication authentication) {
        User customer = authService.getUserByEmail(authentication.getName());
        List<RequirementResponse> list = requirementService.getMyRequirements(customer);
        return ResponseEntity.ok(ApiResponse.ok("My posted service requirements", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RequirementResponse>> getRequirementById(@PathVariable Long id) {
        RequirementResponse response = requirementService.getRequirementResponseById(id);
        return ResponseEntity.ok(ApiResponse.ok("Requirement details retrieved", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RequirementResponse>> updateRequirement(
            @PathVariable Long id,
            @Valid @RequestBody RequirementRequest request,
            Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        RequirementResponse response = requirementService.updateRequirement(id, request, user);
        return ResponseEntity.ok(ApiResponse.ok("Requirement updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRequirement(@PathVariable Long id, Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        requirementService.deleteRequirement(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Requirement deleted successfully"));
    }
}

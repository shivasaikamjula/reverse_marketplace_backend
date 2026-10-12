package com.marketplace.controller;

import com.marketplace.dto.ApiResponse;
import com.marketplace.dto.ReviewRequest;
import com.marketplace.model.Review;
import com.marketplace.model.User;
import com.marketplace.service.AuthService;
import com.marketplace.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<ApiResponse<Review>> submitReview(
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication) {
        User customer = authService.getUserByEmail(authentication.getName());
        Review review = reviewService.submitReview(request, customer);
        return ResponseEntity.ok(ApiResponse.ok("Review submitted successfully! Thank you for your feedback.", review));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<List<Review>>> getReviewsForProvider(@PathVariable Long providerId) {
        List<Review> reviews = reviewService.getReviewsForProvider(providerId);
        return ResponseEntity.ok(ApiResponse.ok("Provider reviews retrieved", reviews));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<Review>> getReviewForOrder(@PathVariable Long orderId) {
        Optional<Review> review = reviewService.getReviewForOrder(orderId);
        return review.map(r -> ResponseEntity.ok(ApiResponse.ok("Order review found", r)))
                .orElseGet(() -> ResponseEntity.ok(ApiResponse.ok("No review yet for this order", null)));
    }
}

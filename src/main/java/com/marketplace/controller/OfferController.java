package com.marketplace.controller;

import com.marketplace.dto.ApiResponse;
import com.marketplace.dto.OfferRequest;
import com.marketplace.dto.OfferResponse;
import com.marketplace.model.User;
import com.marketplace.service.AuthService;
import com.marketplace.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<ApiResponse<OfferResponse>> submitOffer(
            @Valid @RequestBody OfferRequest request,
            Authentication authentication) {
        User provider = authService.getUserByEmail(authentication.getName());
        OfferResponse response = offerService.submitOffer(request, provider);
        return ResponseEntity.ok(ApiResponse.ok("Offer submitted successfully!", response));
    }

    @GetMapping("/requirement/{requirementId}")
    public ResponseEntity<ApiResponse<List<OfferResponse>>> getOffersForRequirement(
            @PathVariable Long requirementId,
            Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        List<OfferResponse> offers = offerService.getOffersByRequirement(requirementId, user);
        return ResponseEntity.ok(ApiResponse.ok("Offers for comparison retrieved", offers));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<OfferResponse>>> getMyOffers(Authentication authentication) {
        User provider = authService.getUserByEmail(authentication.getName());
        List<OfferResponse> offers = offerService.getMyOffers(provider);
        return ResponseEntity.ok(ApiResponse.ok("My submitted offers", offers));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OfferResponse>> updateOffer(
            @PathVariable Long id,
            @Valid @RequestBody OfferRequest request,
            Authentication authentication) {
        User provider = authService.getUserByEmail(authentication.getName());
        OfferResponse response = offerService.updateOffer(id, request, provider);
        return ResponseEntity.ok(ApiResponse.ok("Offer updated successfully", response));
    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdrawOffer(
            @PathVariable Long id,
            Authentication authentication) {
        User provider = authService.getUserByEmail(authentication.getName());
        offerService.withdrawOffer(id, provider);
        return ResponseEntity.ok(ApiResponse.ok("Offer withdrawn successfully"));
    }
}

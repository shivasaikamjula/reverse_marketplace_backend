package com.marketplace.service;

import com.marketplace.dto.OfferRequest;
import com.marketplace.dto.OfferResponse;
import com.marketplace.model.*;
import com.marketplace.repository.OfferRepository;
import com.marketplace.repository.RequirementRepository;
import com.marketplace.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private RequirementRepository requirementRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public OfferResponse submitOffer(OfferRequest request, User provider) {
        if (provider.getRole() != Role.PROVIDER && provider.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only registered Service Providers can submit offers!");
        }

        ServiceRequirement requirement = requirementRepository.findById(request.getRequirementId())
                .orElseThrow(() -> new IllegalArgumentException("Requirement not found with ID: " + request.getRequirementId()));

        if (requirement.getStatus() != RequirementStatus.OPEN) {
            throw new IllegalArgumentException("Cannot submit offer. This requirement is " + requirement.getStatus());
        }

        if (requirement.getCustomer().getId().equals(provider.getId())) {
            throw new IllegalArgumentException("You cannot submit an offer on your own requirement!");
        }

        // Business Rule: Prevent multiple offers from the same provider for the same requirement
        if (offerRepository.existsByRequirementAndProvider(requirement, provider)) {
            throw new IllegalArgumentException("You have already submitted an offer for this requirement! You can edit your existing offer.");
        }

        Offer offer = new Offer();
        offer.setRequirement(requirement);
        offer.setProvider(provider);
        offer.setPrice(request.getPrice());
        offer.setEstimatedDays(request.getEstimatedDays());
        offer.setDescription(request.getDescription());
        offer.setStatus(OfferStatus.PENDING);

        Offer saved = offerRepository.save(offer);
        return mapToResponse(saved);
    }

    public List<OfferResponse> getOffersByRequirement(Long requirementId, User currentUser) {
        ServiceRequirement req = requirementRepository.findById(requirementId)
                .orElseThrow(() -> new IllegalArgumentException("Requirement not found with ID: " + requirementId));

        // Customers can only view offers for their own requirement (or Admin can view)
        // Providers can also view offers to see competitive bids
        List<Offer> offers = offerRepository.findByRequirementOrderByPriceAsc(req);
        return offers.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<OfferResponse> getMyOffers(User provider) {
        return offerRepository.findByProviderOrderByCreatedAtDesc(provider)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OfferResponse updateOffer(Long offerId, OfferRequest request, User provider) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found with ID: " + offerId));

        if (!offer.getProvider().getId().equals(provider.getId()) && provider.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("You can only edit your own offers!");
        }

        if (offer.getStatus() != OfferStatus.PENDING) {
            throw new IllegalArgumentException("Cannot edit offer. Its status is " + offer.getStatus());
        }

        offer.setPrice(request.getPrice());
        offer.setEstimatedDays(request.getEstimatedDays());
        offer.setDescription(request.getDescription());

        Offer updated = offerRepository.save(offer);
        return mapToResponse(updated);
    }

    @Transactional
    public void withdrawOffer(Long offerId, User provider) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found with ID: " + offerId));

        if (!offer.getProvider().getId().equals(provider.getId()) && provider.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("You can only withdraw your own offers!");
        }

        if (offer.getStatus() != OfferStatus.PENDING) {
            throw new IllegalArgumentException("Only pending offers can be withdrawn.");
        }

        offer.setStatus(OfferStatus.WITHDRAWN);
        offerRepository.save(offer);
    }

    public OfferResponse mapToResponse(Offer offer) {
        OfferResponse res = new OfferResponse();
        res.setId(offer.getId());
        res.setRequirementId(offer.getRequirement().getId());
        res.setRequirementTitle(offer.getRequirement().getTitle());
        res.setProviderId(offer.getProvider().getId());
        res.setProviderName(offer.getProvider().getName());
        res.setProviderEmail(offer.getProvider().getEmail());
        res.setProviderPhone(offer.getProvider().getPhone());

        // Provider Rating calculation
        Double avgRating = reviewRepository.findAverageRatingByProvider(offer.getProvider());
        res.setProviderRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 5.0); // Default 5.0 if new
        res.setProviderReviewCount(reviewRepository.countByProvider(offer.getProvider()));

        res.setPrice(offer.getPrice());
        res.setEstimatedDays(offer.getEstimatedDays());
        res.setDescription(offer.getDescription());
        res.setStatus(offer.getStatus());
        res.setCreatedAt(offer.getCreatedAt());

        return res;
    }
}

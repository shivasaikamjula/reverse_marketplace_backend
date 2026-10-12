package com.marketplace.repository;

import com.marketplace.model.Offer;
import com.marketplace.model.OfferStatus;
import com.marketplace.model.ServiceRequirement;
import com.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByRequirementOrderByPriceAsc(ServiceRequirement requirement);
    List<Offer> findByProviderOrderByCreatedAtDesc(User provider);
    Boolean existsByRequirementAndProvider(ServiceRequirement requirement, User provider);
    Optional<Offer> findByRequirementAndProvider(ServiceRequirement requirement, User provider);
    List<Offer> findByRequirementAndStatus(ServiceRequirement requirement, OfferStatus status);
    long countByProvider(User provider);
}

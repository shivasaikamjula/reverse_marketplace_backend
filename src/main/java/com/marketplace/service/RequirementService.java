package com.marketplace.service;

import com.marketplace.dto.RequirementRequest;
import com.marketplace.dto.RequirementResponse;
import com.marketplace.model.*;
import com.marketplace.repository.CategoryRepository;
import com.marketplace.repository.OfferRepository;
import com.marketplace.repository.RequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequirementService {

    @Autowired
    private RequirementRepository requirementRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Transactional
    public RequirementResponse createRequirement(RequirementRequest request, User customer) {
        if (customer.getRole() != Role.CUSTOMER && customer.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only customers can post service requirements!");
        }

        ServiceCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + request.getCategoryId()));

        ServiceRequirement req = new ServiceRequirement();
        req.setTitle(request.getTitle());
        req.setDescription(request.getDescription());
        req.setCategory(category);
        req.setBudget(request.getBudget());
        req.setLocation(request.getLocation());
        req.setDeadline(request.getDeadline());
        req.setCustomer(customer);
        req.setStatus(RequirementStatus.OPEN);

        ServiceRequirement saved = requirementRepository.save(req);
        return mapToResponse(saved);
    }

    public List<RequirementResponse> getAllOpenRequirements() {
        return requirementRepository.findByStatusOrderByCreatedAtDesc(RequirementStatus.OPEN)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RequirementResponse> getMyRequirements(User customer) {
        return requirementRepository.findByCustomerOrderByCreatedAtDesc(customer)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RequirementResponse getRequirementResponseById(Long id) {
        ServiceRequirement req = requirementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service requirement not found with ID: " + id));
        return mapToResponse(req);
    }

    public ServiceRequirement getRequirementEntityById(Long id) {
        return requirementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service requirement not found with ID: " + id));
    }

    @Transactional
    public RequirementResponse updateRequirement(Long id, RequirementRequest request, User user) {
        ServiceRequirement req = getRequirementEntityById(id);

        // Security check: Only the owning customer or an ADMIN can edit
        if (!req.getCustomer().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("You can only edit your own requirements!");
        }

        if (req.getStatus() != RequirementStatus.OPEN) {
            throw new IllegalArgumentException("Cannot edit requirement because it is already " + req.getStatus());
        }

        ServiceCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + request.getCategoryId()));

        req.setTitle(request.getTitle());
        req.setDescription(request.getDescription());
        req.setCategory(category);
        req.setBudget(request.getBudget());
        req.setLocation(request.getLocation());
        req.setDeadline(request.getDeadline());

        ServiceRequirement updated = requirementRepository.save(req);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteRequirement(Long id, User user) {
        ServiceRequirement req = getRequirementEntityById(id);

        if (!req.getCustomer().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("You can only delete your own requirements!");
        }

        if (req.getStatus() == RequirementStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Cannot delete requirement while service is in progress!");
        }

        requirementRepository.delete(req);
    }

    public RequirementResponse mapToResponse(ServiceRequirement req) {
        RequirementResponse res = new RequirementResponse();
        res.setId(req.getId());
        res.setTitle(req.getTitle());
        res.setDescription(req.getDescription());
        res.setCategory(req.getCategory());
        res.setBudget(req.getBudget());
        res.setLocation(req.getLocation());
        res.setDeadline(req.getDeadline());
        res.setCustomerId(req.getCustomer().getId());
        res.setCustomerName(req.getCustomer().getName());
        res.setCustomerEmail(req.getCustomer().getEmail());
        res.setStatus(req.getStatus());
        res.setCreatedAt(req.getCreatedAt());

        // Compute offer stats for this requirement
        List<Offer> offers = offerRepository.findByRequirementOrderByPriceAsc(req);
        res.setOfferCount(offers.size());
        if (!offers.isEmpty()) {
            res.setLowestOffer(offers.get(0).getPrice());
        } else {
            res.setLowestOffer(null);
        }

        return res;
    }
}

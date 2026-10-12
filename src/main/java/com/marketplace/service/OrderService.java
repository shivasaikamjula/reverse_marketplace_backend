package com.marketplace.service;

import com.marketplace.model.*;
import com.marketplace.repository.OfferRepository;
import com.marketplace.repository.OrderRepository;
import com.marketplace.repository.RequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private RequirementRepository requirementRepository;

    @Transactional
    public ServiceOrder acceptOfferAndCreateOrder(Long offerId, User customer) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found with ID: " + offerId));

        ServiceRequirement requirement = offer.getRequirement();

        // Business Rule: Customers can only accept offers for requirements that belong to them
        if (!requirement.getCustomer().getId().equals(customer.getId()) && customer.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("You can only accept offers for your own requirements!");
        }

        if (requirement.getStatus() != RequirementStatus.OPEN) {
            throw new IllegalArgumentException("This requirement is already " + requirement.getStatus());
        }

        if (offer.getStatus() != OfferStatus.PENDING) {
            throw new IllegalArgumentException("This offer cannot be accepted because it is " + offer.getStatus());
        }

        // 1. Mark accepted offer
        offer.setStatus(OfferStatus.ACCEPTED);
        offerRepository.save(offer);

        // 2. Mark all other offers for this requirement as REJECTED
        List<Offer> otherOffers = offerRepository.findByRequirementOrderByPriceAsc(requirement);
        for (Offer other : otherOffers) {
            if (!other.getId().equals(offer.getId()) && other.getStatus() == OfferStatus.PENDING) {
                other.setStatus(OfferStatus.REJECTED);
                offerRepository.save(other);
            }
        }

        // 3. Update requirement status to IN_PROGRESS
        requirement.setStatus(RequirementStatus.IN_PROGRESS);
        requirementRepository.save(requirement);

        // 4. Create and save new ServiceOrder
        ServiceOrder order = new ServiceOrder();
        order.setRequirement(requirement);
        order.setAcceptedOffer(offer);
        order.setCustomer(customer);
        order.setProvider(offer.getProvider());
        order.setTotalAmount(offer.getPrice());
        order.setStatus(OrderStatus.ACTIVE);

        return orderRepository.save(order);
    }

    @Transactional
    public ServiceOrder completeOrder(Long orderId, User customer) {
        ServiceOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        // Security check: Only customer or ADMIN can mark order completed
        if (!order.getCustomer().getId().equals(customer.getId()) && customer.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only the customer can mark this order as completed!");
        }

        if (order.getStatus() != OrderStatus.ACTIVE) {
            throw new IllegalArgumentException("Order is already " + order.getStatus());
        }

        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletedDate(LocalDateTime.now());

        // Update the requirement to COMPLETED as well
        ServiceRequirement requirement = order.getRequirement();
        requirement.setStatus(RequirementStatus.COMPLETED);
        requirementRepository.save(requirement);

        return orderRepository.save(order);
    }

    public List<ServiceOrder> getMyOrders(User user) {
        if (user.getRole() == Role.CUSTOMER) {
            return orderRepository.findByCustomerOrderByCreatedDateDesc(user);
        } else if (user.getRole() == Role.PROVIDER) {
            return orderRepository.findByProviderOrderByCreatedDateDesc(user);
        } else {
            return orderRepository.findAll();
        }
    }

    public ServiceOrder getOrderById(Long orderId, User user) {
        ServiceOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        boolean isAuthorized = order.getCustomer().getId().equals(user.getId())
                || order.getProvider().getId().equals(user.getId())
                || user.getRole() == Role.ADMIN;

        if (!isAuthorized) {
            throw new IllegalArgumentException("You are not authorized to view this order.");
        }

        return order;
    }
}

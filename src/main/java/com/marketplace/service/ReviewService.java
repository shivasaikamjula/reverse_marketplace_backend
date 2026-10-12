package com.marketplace.service;

import com.marketplace.dto.ReviewRequest;
import com.marketplace.model.*;
import com.marketplace.repository.OrderRepository;
import com.marketplace.repository.ReviewRepository;
import com.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Review submitReview(ReviewRequest request, User customer) {
        ServiceOrder order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + request.getOrderId()));

        // Business Rule: Prevent reviews for orders that have not been completed
        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new IllegalArgumentException("Reviews can only be submitted after the service order is marked as COMPLETED!");
        }

        // Security check: Only the customer who placed the order can submit the review
        if (!order.getCustomer().getId().equals(customer.getId()) && customer.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only the customer of this order can submit a review!");
        }

        // Prevent duplicate review for the same order
        if (reviewRepository.existsByOrder(order)) {
            throw new IllegalArgumentException("A review has already been submitted for this order!");
        }

        Review review = new Review();
        review.setOrder(order);
        review.setReviewer(customer);
        review.setProvider(order.getProvider());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForProvider(Long providerId) {
        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Provider not found with ID: " + providerId));

        return reviewRepository.findByProviderOrderByCreatedDateDesc(provider);
    }

    public Optional<Review> getReviewForOrder(Long orderId) {
        ServiceOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        return reviewRepository.findByOrder(order);
    }
}

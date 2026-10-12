package com.marketplace.service;

import com.marketplace.dto.AdminStatsResponse;
import com.marketplace.model.*;
import com.marketplace.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RequirementRepository requirementRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public AdminStatsResponse getStats() {
        AdminStatsResponse stats = new AdminStatsResponse();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalCustomers(userRepository.countByRole(Role.CUSTOMER));
        stats.setTotalProviders(userRepository.countByRole(Role.PROVIDER));
        stats.setTotalCategories(categoryRepository.count());
        stats.setTotalRequirements(requirementRepository.count());
        stats.setOpenRequirements(requirementRepository.countByStatus(RequirementStatus.OPEN));
        stats.setTotalOffers(offerRepository.count());
        stats.setTotalOrders(orderRepository.count());
        stats.setActiveOrders(orderRepository.countByStatus(OrderStatus.ACTIVE));
        stats.setCompletedOrders(orderRepository.countByStatus(OrderStatus.COMPLETED));
        return stats;
    }
}

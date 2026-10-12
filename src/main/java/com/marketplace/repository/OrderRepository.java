package com.marketplace.repository;

import com.marketplace.model.OrderStatus;
import com.marketplace.model.ServiceOrder;
import com.marketplace.model.ServiceRequirement;
import com.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<ServiceOrder, Long> {
    List<ServiceOrder> findByCustomerOrderByCreatedDateDesc(User customer);
    List<ServiceOrder> findByProviderOrderByCreatedDateDesc(User provider);
    Optional<ServiceOrder> findByRequirement(ServiceRequirement requirement);
    long countByStatus(OrderStatus status);
    long countByCustomer(User customer);
    long countByProvider(User provider);
}

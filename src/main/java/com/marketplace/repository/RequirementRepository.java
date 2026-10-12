package com.marketplace.repository;

import com.marketplace.model.RequirementStatus;
import com.marketplace.model.ServiceCategory;
import com.marketplace.model.ServiceRequirement;
import com.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementRepository extends JpaRepository<ServiceRequirement, Long> {
    List<ServiceRequirement> findByCustomerOrderByCreatedAtDesc(User customer);
    List<ServiceRequirement> findByStatusOrderByCreatedAtDesc(RequirementStatus status);
    List<ServiceRequirement> findByCategoryAndStatusOrderByCreatedAtDesc(ServiceCategory category, RequirementStatus status);
    long countByStatus(RequirementStatus status);
}

package com.marketplace.repository;

import com.marketplace.model.Review;
import com.marketplace.model.ServiceOrder;
import com.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProviderOrderByCreatedDateDesc(User provider);
    Optional<Review> findByOrder(ServiceOrder order);
    Boolean existsByOrder(ServiceOrder order);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.provider = :provider")
    Double findAverageRatingByProvider(@Param("provider") User provider);

    long countByProvider(User provider);
}

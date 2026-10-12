package com.marketplace.repository;

import com.marketplace.model.Role;
import com.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for User entity.
 *
 * Viva Notes:
 * - Extends JpaRepository<User, Long>: Gives ready-made CRUD methods
 *   (save, findById, findAll, deleteById, count) without writing SQL.
 * - Spring Data JPA generates queries automatically from method names
 *   like 'findByEmail'.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    long countByRole(Role role);
}

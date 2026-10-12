package com.marketplace.model;

/**
 * User roles in the Reverse Marketplace system.
 *
 * Viva Note:
 * An Enum (enumeration) defines a fixed set of constants.
 * In Spring Security, roles are prefixed or mapped to authorities
 * to restrict access to specific endpoints.
 */
public enum Role {
    CUSTOMER,
    PROVIDER,
    ADMIN
}

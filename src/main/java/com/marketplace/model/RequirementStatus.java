package com.marketplace.model;

public enum RequirementStatus {
    OPEN,        // Newly posted requirement, accepting offers
    IN_PROGRESS, // An offer was accepted, service order is active
    COMPLETED,   // Work has been delivered and accepted
    CANCELLED    // Cancelled by the customer
}

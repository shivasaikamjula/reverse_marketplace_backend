package com.marketplace.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OfferRequest {

    @NotNull(message = "Requirement ID is required")
    private Long requirementId;

    @NotNull(message = "Offer price is required")
    @DecimalMin(value = "1.0", message = "Price must be at least 1.0")
    private Double price;

    @NotNull(message = "Estimated days is required")
    @Min(value = 1, message = "Estimated days must be at least 1")
    private Integer estimatedDays;

    @NotBlank(message = "Offer proposal description is required")
    @Size(min = 10, max = 1500, message = "Description must be between 10 and 1500 characters")
    private String description;

    public OfferRequest() {
    }

    public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(Integer estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.marketplace.dto;

public class AdminStatsResponse {
    private long totalUsers;
    private long totalCustomers;
    private long totalProviders;
    private long totalCategories;
    private long totalRequirements;
    private long openRequirements;
    private long totalOffers;
    private long totalOrders;
    private long activeOrders;
    private long completedOrders;

    public AdminStatsResponse() {
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getTotalProviders() {
        return totalProviders;
    }

    public void setTotalProviders(long totalProviders) {
        this.totalProviders = totalProviders;
    }

    public long getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(long totalCategories) {
        this.totalCategories = totalCategories;
    }

    public long getTotalRequirements() {
        return totalRequirements;
    }

    public void setTotalRequirements(long totalRequirements) {
        this.totalRequirements = totalRequirements;
    }

    public long getOpenRequirements() {
        return openRequirements;
    }

    public void setOpenRequirements(long openRequirements) {
        this.openRequirements = openRequirements;
    }

    public long getTotalOffers() {
        return totalOffers;
    }

    public void setTotalOffers(long totalOffers) {
        this.totalOffers = totalOffers;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getActiveOrders() {
        return activeOrders;
    }

    public void setActiveOrders(long activeOrders) {
        this.activeOrders = activeOrders;
    }

    public long getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(long completedOrders) {
        this.completedOrders = completedOrders;
    }
}

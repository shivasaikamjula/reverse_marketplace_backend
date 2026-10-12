package com.marketplace.model;

public enum OfferStatus {
    PENDING,   // Provider submitted offer, awaiting customer response
    ACCEPTED,  // Customer selected this offer
    REJECTED,  // Customer chose a different offer
    WITHDRAWN  // Provider revoked this offer
}

package com.project.login.enums;

public enum PaymentStatus {

    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    EXPIRED("Expired"),
    NOT_STARTED("Not Started Yet"),
    NOT_FOUND("No Payment Found");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
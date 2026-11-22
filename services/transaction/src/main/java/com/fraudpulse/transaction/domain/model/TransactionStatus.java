package com.fraudpulse.transaction.domain.model;

import com.fraudpulse.transaction.fraud.model.RiskLevel;

public enum TransactionStatus {
    PENDING,
    APPROVED,
    UNDER_REVIEW,
    BLOCKED;

    public static TransactionStatus fromRiskLevel(RiskLevel riskLevel) {
        return switch (riskLevel) {
            case LOW -> APPROVED;
            case MEDIUM -> UNDER_REVIEW;
            case HIGH -> BLOCKED;
        };
    }

}

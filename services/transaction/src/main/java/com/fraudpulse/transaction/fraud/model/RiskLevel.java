package com.fraudpulse.transaction.fraud.model;

import com.fraudpulse.transaction.shared.exception.ApiException;
import com.fraudpulse.transaction.shared.exception.ErrorCode;

public enum RiskLevel {
    LOW,
    MEDIUM,
    HIGH;

    public static RiskLevel fromScore(int score) {
        if (score < 0 || score > 100) {
            throw ApiException.of(ErrorCode.INVALID_RISK_SCORE);
        }

        if (score >= 70) {
            return HIGH;
        }

        if (score >= 40) {
            return MEDIUM;
        }

        return LOW;
    }
}

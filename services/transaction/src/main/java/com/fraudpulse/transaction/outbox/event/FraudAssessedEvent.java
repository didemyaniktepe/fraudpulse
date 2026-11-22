package com.fraudpulse.transaction.outbox.event;

import java.util.UUID;

import com.fraudpulse.transaction.domain.model.TransactionStatus;
import com.fraudpulse.transaction.fraud.model.RiskLevel;

public record FraudAssessedEvent(
        UUID transactionId,
        int riskScore,
        RiskLevel riskLevel,
        TransactionStatus transactionStatus
) {
}
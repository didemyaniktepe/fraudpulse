package com.fraudpulse.transaction.fraud.model;

import java.util.List;
import java.util.UUID;

public record FraudResult(UUID transactionId,
                          int riskScore,
                          RiskLevel riskLevel,
                          List<RuleResult> results) {
}
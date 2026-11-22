package com.fraudpulse.transaction.fraud.model;

public record RuleResult(
        RuleCode ruleCode,
        boolean triggered,
        int score
) {
}
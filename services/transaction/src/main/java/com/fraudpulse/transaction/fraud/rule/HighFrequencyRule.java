package com.fraudpulse.transaction.fraud.rule;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.fraudpulse.transaction.fraud.model.FraudContext;
import com.fraudpulse.transaction.fraud.model.RuleCode;
import com.fraudpulse.transaction.fraud.model.RuleResult;
import com.fraudpulse.transaction.ingestion.application.TransactionQueryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HighFrequencyRule implements FraudRule {

    private static final Duration WINDOW = Duration.ofMinutes(5);
    private static final int THRESHOLD = 5;
    private static final int RISK_SCORE = 30;

    private final TransactionQueryService transactionQueryService;

    @Override
    public RuleCode getRuleCode() {
        return RuleCode.HIGH_FREQUENCY;
    }

    @Override
    public RuleResult evaluate(FraudContext context) {
        Instant to = context.occurredAt();
        Instant from = to.minus(WINDOW);

        long previousCount = transactionQueryService.countTransactions(context.accountId(),
                context.transactionId(),
                from,
                to
        );

        boolean triggered = previousCount + 1 >= THRESHOLD;
        return new RuleResult(getRuleCode(), triggered, triggered ? RISK_SCORE : 0);
    }
}

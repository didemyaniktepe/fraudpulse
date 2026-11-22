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
public class RepeatedAmountRule implements FraudRule {

    private static final Duration WINDOW = Duration.ofMinutes(2);
    private static final int THRESHOLD = 8;
    private static final int RISK_SCORE = 15;

    private final TransactionQueryService transactionQueryService;

    @Override
    public RuleCode getRuleCode() {
        return RuleCode.REPEATED_AMOUNT;
    }

    @Override
    public RuleResult evaluate(FraudContext context) {
        Instant to = context.occurredAt();
        Instant from = to.minus(WINDOW);

        long previousCount = transactionQueryService.countTransactionsWithSameAmount(
                        context.accountId(),
                        context.transactionId(),
                        context.amount(),
                        context.currency(),
                        from,
                        to
                );

        boolean triggered = previousCount + 1 >= THRESHOLD;
        return new RuleResult(getRuleCode(), triggered, triggered ? RISK_SCORE : 0);

    }
}

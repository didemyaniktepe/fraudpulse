package com.fraudpulse.transaction.fraud.rule;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.fraudpulse.transaction.fraud.model.FraudContext;
import com.fraudpulse.transaction.fraud.model.RuleCode;
import com.fraudpulse.transaction.fraud.model.RuleResult;

@Component
public class LargeAmountRule implements FraudRule {

    private static final BigDecimal THRESHOLD = new BigDecimal("5000");

    private static final int RISK_SCORE = 20;

    @Override
    public RuleCode getRuleCode() {
        return RuleCode.LARGE_AMOUNT;
    }

    @Override
    public RuleResult evaluate(FraudContext context) {
        boolean triggered = context.amount().compareTo(THRESHOLD) > 0;
        return new RuleResult(getRuleCode(), triggered, triggered ? RISK_SCORE : 0);
    }
}

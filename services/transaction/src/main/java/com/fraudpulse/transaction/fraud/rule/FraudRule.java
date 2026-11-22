package com.fraudpulse.transaction.fraud.rule;


import com.fraudpulse.transaction.fraud.model.FraudContext;
import com.fraudpulse.transaction.fraud.model.RuleCode;
import com.fraudpulse.transaction.fraud.model.RuleResult;

public interface FraudRule {
    RuleCode getRuleCode();
    RuleResult evaluate(FraudContext context);
}

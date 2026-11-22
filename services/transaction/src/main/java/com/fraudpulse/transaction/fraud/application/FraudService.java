package com.fraudpulse.transaction.fraud.application;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.fraudpulse.transaction.fraud.model.FraudContext;
import com.fraudpulse.transaction.fraud.model.FraudResult;
import com.fraudpulse.transaction.fraud.model.RiskLevel;
import com.fraudpulse.transaction.fraud.model.RuleResult;
import com.fraudpulse.transaction.fraud.persistance.FraudAssessment;
import com.fraudpulse.transaction.fraud.persistance.FraudAssessmentRepository;
import com.fraudpulse.transaction.fraud.rule.FraudRule;
import com.fraudpulse.transaction.ingestion.persistance.Transaction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FraudService {

    private final List<FraudRule> rules;
    private final FraudAssessmentRepository repository;

    @Transactional
    public FraudResult assess(Transaction transaction) {
        FraudContext context = FraudContext.from(transaction);
        FraudResult result = evaluate(context);
        repository.save(new FraudAssessment(transaction, result));
        return result;
    }

    private FraudResult evaluate(FraudContext context) {
        List<RuleResult> results = rules.stream().map(rule -> rule.evaluate(context)).toList();
        int totalScore = results.stream().mapToInt(RuleResult::score).sum();
        int riskScore = Math.min(totalScore, 100);
        RiskLevel riskLevel = RiskLevel.fromScore(riskScore);
        return new FraudResult(context.transactionId(), riskScore, riskLevel, results);
    }

}

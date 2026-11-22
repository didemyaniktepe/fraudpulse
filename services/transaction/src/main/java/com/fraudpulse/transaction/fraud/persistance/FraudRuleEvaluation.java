package com.fraudpulse.transaction.fraud.persistance;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.*;

import com.fraudpulse.transaction.fraud.model.RuleCode;
import com.fraudpulse.transaction.fraud.model.RuleResult;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fraud_rule_evaluations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FraudRuleEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assessment_id", nullable = false)
    private FraudAssessment assessment;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_code", nullable = false)
    private RuleCode ruleCode;

    @Column(nullable = false)
    private int score;

    public FraudRuleEvaluation(FraudAssessment assessment, RuleResult result) {
        this.assessment = Objects.requireNonNull(assessment);
        Objects.requireNonNull(result);
        this.ruleCode = result.ruleCode();
        this.score = result.score();
    }

    public boolean isTriggered() {
        return score > 0;
    }

}

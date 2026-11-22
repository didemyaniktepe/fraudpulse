
package com.fraudpulse.transaction.fraud.persistance;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.*;

import com.fraudpulse.transaction.fraud.model.FraudResult;
import com.fraudpulse.transaction.fraud.model.RiskLevel;
import com.fraudpulse.transaction.ingestion.persistance.Transaction;
import com.fraudpulse.transaction.shared.exception.ApiException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.fraudpulse.transaction.shared.exception.ErrorCode.FRAUD_ASSESSMENT_TRANSACTION_MISMATCH;

@Entity
@Table(name = "fraud_assessments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FraudAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false, unique = true)
    private Transaction transaction;

    @Column(name = "risk_score", nullable = false)
    private int riskScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;

    @Column(name = "evaluated_at", nullable = false)
    private Instant evaluatedAt;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FraudRuleEvaluation> ruleEvaluations = new ArrayList<>();

    public FraudAssessment(Transaction transaction, FraudResult result) {
        this.transaction = Objects.requireNonNull(transaction);
        Objects.requireNonNull(result);

        if (!Objects.equals(transaction.getId(), result.transactionId())) {
            throw ApiException.of(FRAUD_ASSESSMENT_TRANSACTION_MISMATCH);
        }

        this.riskScore = result.riskScore();
        this.riskLevel = result.riskLevel();
        this.evaluatedAt = Instant.now();

        result.results().forEach(ruleResult ->
                ruleEvaluations.add(
                        new FraudRuleEvaluation(this, ruleResult)
                )
        );
    }
}

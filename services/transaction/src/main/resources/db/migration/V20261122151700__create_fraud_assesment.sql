CREATE TABLE fraud_assessments
(
    id             UUID PRIMARY KEY,
    transaction_id UUID NOT NULL UNIQUE,
    risk_score     INTEGER NOT NULL,
    risk_level     VARCHAR(20) NOT NULL,
    evaluated_at   TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_fraud_assessment_transaction
        FOREIGN KEY (transaction_id)
            REFERENCES transactions(id),

    CONSTRAINT chk_fraud_assessment_score
        CHECK (risk_score BETWEEN 0 AND 100),

    CONSTRAINT chk_fraud_assessment_level
        CHECK (risk_level IN ('LOW', 'MEDIUM', 'HIGH'))
);

CREATE TABLE fraud_rule_evaluations
(
    id            UUID PRIMARY KEY,
    assessment_id UUID NOT NULL,
    rule_code     VARCHAR(50) NOT NULL,
    score         INTEGER NOT NULL,

    CONSTRAINT fk_fraud_rule_evaluation_assessment
        FOREIGN KEY (assessment_id)
            REFERENCES fraud_assessments(id)
            ON DELETE CASCADE,

    CONSTRAINT uq_fraud_rule_evaluation
        UNIQUE (assessment_id, rule_code),

    CONSTRAINT chk_fraud_rule_evaluation_score
        CHECK (score >= 0)
);

CREATE INDEX idx_fraud_rule_evaluations_rule_code
    ON fraud_rule_evaluations(rule_code);

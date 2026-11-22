package com.fraudpulse.transaction.fraud.persistance;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraudAssessmentRepository extends JpaRepository<FraudAssessment, UUID> {
}

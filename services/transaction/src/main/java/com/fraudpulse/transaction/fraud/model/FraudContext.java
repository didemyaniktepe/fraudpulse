package com.fraudpulse.transaction.fraud.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.fraudpulse.transaction.ingestion.persistance.Transaction;

public record FraudContext (UUID transactionId,
                            UUID accountId,
                            UUID deviceId,
                            BigDecimal amount,
                            String currency,
                            Instant occurredAt){

    public static FraudContext from(Transaction transaction) {
        return new FraudContext(
                transaction.getId(),
                transaction.getAccount().getId(),
                transaction.getDevice() != null
                        ? transaction.getDevice().getId()
                        : null,
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getOccurredAt()
        );
    }
}

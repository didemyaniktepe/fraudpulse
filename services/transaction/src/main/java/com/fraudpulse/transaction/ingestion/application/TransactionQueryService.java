package com.fraudpulse.transaction.ingestion.application;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fraudpulse.transaction.ingestion.persistance.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionQueryService {

    private final TransactionRepository repository;

    public long countTransactions(UUID accountId, UUID excludeTransactionId,
                                  Instant from, Instant to) {
        return repository.countTransactions(accountId, excludeTransactionId, from, to);
    }

    public long countTransactionsWithSameAmount(UUID accountId, UUID transactionId, BigDecimal amount,
                                                String currency,Instant from, Instant to) {
        return repository.countTransactionsWithSameAmount(accountId, transactionId, amount, currency, from, to);
    }
}

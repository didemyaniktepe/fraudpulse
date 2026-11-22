package com.fraudpulse.transaction.ingestion.persistance;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("""
            SELECT COUNT(t)
            FROM Transaction t
            WHERE t.account.id = :accountId
              AND t.id <> :transactionId
              AND t.occurredAt >= :from
              AND t.occurredAt <= :to
            """)
    long countTransactions(@Param("accountId") UUID accountId,
                           @Param("transactionId") UUID transactionId,
                           @Param("from") Instant from,
                           @Param("to") Instant to);


    @Query("""
            SELECT COUNT(t)
            FROM Transaction t
            WHERE t.account.id = :accountId
              AND t.id <> :transactionId
              AND t.amount = :amount
              AND t.currency = :currency
              AND t.occurredAt >= :from
              AND t.occurredAt <= :to
            """)
    long countTransactionsWithSameAmount(@Param("accountId") UUID accountId,
                                         @Param("transactionId") UUID transactionId,
                                         @Param("amount") BigDecimal amount,
                                         @Param("currency") String currency,
                                         @Param("from") Instant from,
                                         @Param("to") Instant to);


}

package com.fraudpulse.transaction.ingestion.persistance;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fraudpulse.transaction.account.persistance.Account;
import com.fraudpulse.transaction.device.persistance.Device;
import com.fraudpulse.transaction.domain.model.TransactionStatus;
import com.fraudpulse.transaction.domain.model.TransactionType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;
    
    private BigDecimal amount;
    private String currency;
    private String channel;

    private String source;
    private String sourceTransactionId;

    private Instant occurredAt;
    @CreatedDate
    private Instant createdAt;


    @Builder
    public Transaction(Account account, Device device, TransactionType transactionType, 
                       BigDecimal amount, String currency, String channel, Instant occurredAt,
                       String source, String sourceTransactionId) {
        this.account = account;
        this.device = device;
        this.transactionType = transactionType;
        this.amount = amount;
        this.currency = currency;
        this.channel = channel;
        this.occurredAt = occurredAt;
        this.source = source;
        this.sourceTransactionId = sourceTransactionId;
    }

    public void updateStatus(TransactionStatus status) {
        this.status = Objects.requireNonNull(status);
    }
}

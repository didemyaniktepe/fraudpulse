package com.fraudpulse.transaction.ingestion.persistance;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.fraudpulse.transaction.domain.model.TransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.EntityListeners;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import com.fraudpulse.transaction.account.persistance.Account;
import com.fraudpulse.transaction.device.persistance.Device;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

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
    
    private BigDecimal amount;
    private String currency;
    private String channel;
    private Instant occurredAt;
    @CreatedDate
    private Instant createdAt;


    @Builder
    public Transaction(Account account, Device device, TransactionType transactionType, 
                       BigDecimal amount, String currency, String channel, Instant occurredAt) {
        this.account = account;
        this.device = device;
        this.transactionType = transactionType;
        this.amount = amount;
        this.currency = currency;
        this.channel = channel;
        this.occurredAt = occurredAt;
    }
}

package com.fraudpulse.transaction.ingestion.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fraudpulse.transaction.account.application.AccountService;
import com.fraudpulse.transaction.account.persistance.Account;
import com.fraudpulse.transaction.device.application.DeviceService;
import com.fraudpulse.transaction.device.persistance.Device;
import com.fraudpulse.transaction.domain.model.TransactionStatus;
import com.fraudpulse.transaction.fraud.application.FraudService;
import com.fraudpulse.transaction.fraud.model.FraudResult;
import com.fraudpulse.transaction.ingestion.api.dto.request.CreateTransactionRequest;
import com.fraudpulse.transaction.ingestion.mapper.TransactionEntityMapper;
import com.fraudpulse.transaction.ingestion.persistance.Transaction;
import com.fraudpulse.transaction.ingestion.persistance.TransactionRepository;
import com.fraudpulse.transaction.outbox.application.OutboxService;
import com.fraudpulse.transaction.outbox.event.FraudAssessedEvent;
import com.fraudpulse.transaction.outbox.mapper.EventMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final TransactionEntityMapper entityMapper;

    private final AccountService accountService;
    private final DeviceService deviceService;

    private final FraudService fraudService;

    private final OutboxService outboxService;
    private final EventMapper eventMapper;

    @Transactional
    public Transaction create(CreateTransactionRequest request) {
        Account account = accountService.findOrCreate(request.accountId());
        Device device = deviceService.findOrCreate(request.deviceId());

        Transaction transaction = entityMapper.toEntity(request, account, device);
        repository.save(transaction);

        FraudResult fraudResult = fraudService.assess(transaction);
        transaction.updateStatus(TransactionStatus.fromRiskLevel(fraudResult.riskLevel()));

        FraudAssessedEvent event = eventMapper.toEvent(transaction, fraudResult);
        outboxService.enqueue(event);

        return transaction;
    }

}

package com.fraudpulse.transaction.ingestion.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fraudpulse.transaction.account.application.AccountService;
import com.fraudpulse.transaction.account.persistance.Account;
import com.fraudpulse.transaction.device.application.DeviceService;
import com.fraudpulse.transaction.device.persistance.Device;
import com.fraudpulse.transaction.ingestion.api.dto.request.CreateTransactionRequest;
import com.fraudpulse.transaction.ingestion.mapper.TransactionEntityMapper;
import com.fraudpulse.transaction.ingestion.persistance.Transaction;
import com.fraudpulse.transaction.ingestion.persistance.TransactionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TransactionService {
    
    private final TransactionEntityMapper entityMapper;
    private final TransactionRepository repository;
    private final AccountService accountService;
    private final DeviceService deviceService;

    @Transactional
    public Transaction create(CreateTransactionRequest request) {
        Account account = accountService.findByExternalRef(request.accountId()).orElseGet(() -> accountService.create(request.accountId()));
        Device device = deviceService.findByDeviceRef(request.deviceId()).orElseGet(() -> deviceService.create(request.deviceId()));
        Transaction transaction = entityMapper.toEntity(request, account, device);
        return repository.save(transaction);
    }

}

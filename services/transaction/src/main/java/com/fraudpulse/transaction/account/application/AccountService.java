package com.fraudpulse.transaction.account.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fraudpulse.transaction.account.persistance.Account;
import com.fraudpulse.transaction.account.persistance.AccountRepository;
import com.fraudpulse.transaction.shared.exception.ApiException;
import com.fraudpulse.transaction.shared.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    @Transactional
    public Account findOrCreate(String externalRef) {
        if (externalRef == null) {
            return null;
        }
        return repository.findByExternalRef(externalRef).orElseGet(() -> createIfAbsent(externalRef));
    }

    private Account createIfAbsent(String deviceRef) {
        repository.insertIfAbsent(UUID.randomUUID(), deviceRef);
        return repository.findByExternalRef(deviceRef).orElseThrow(() -> ApiException.of(ErrorCode.RESOLUTION_FAILED));
    }

}

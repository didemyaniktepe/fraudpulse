package com.fraudpulse.transaction.account.application;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fraudpulse.transaction.account.persistance.Account;
import com.fraudpulse.transaction.account.persistance.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    @Transactional
    public Account create(String externalRef) {
        Account account = new Account(externalRef);
        return repository.save(account);
    }

    @Transactional(readOnly = true)
    public Optional<Account> findByExternalRef(String externalRef) {
        return repository.findByExternalRef(externalRef);
    }

}

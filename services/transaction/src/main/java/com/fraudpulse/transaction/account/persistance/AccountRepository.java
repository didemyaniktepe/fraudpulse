package com.fraudpulse.transaction.account.persistance;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByExternalRef(String externalRef);

    @Modifying
    @Query(value = """
            INSERT INTO accounts (id, external_ref)
            VALUES (:id, :externalRef)
            ON CONFLICT (external_ref) DO NOTHING
            """, nativeQuery = true)
    void insertIfAbsent(UUID id, String externalRef);
}

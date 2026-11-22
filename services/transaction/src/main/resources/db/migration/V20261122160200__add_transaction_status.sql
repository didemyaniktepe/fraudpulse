ALTER TABLE transactions
    ADD COLUMN status VARCHAR(20)
        NOT NULL DEFAULT 'PENDING';

ALTER TABLE transactions
    ADD CONSTRAINT chk_transaction_status
        CHECK (status IN (
                          'PENDING',
                          'APPROVED',
                          'UNDER_REVIEW',
                          'BLOCKED'
            ));
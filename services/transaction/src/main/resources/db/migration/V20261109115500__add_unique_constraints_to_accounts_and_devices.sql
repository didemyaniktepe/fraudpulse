ALTER TABLE accounts
    ADD CONSTRAINT uk_accounts_external_ref
    UNIQUE (external_ref);

ALTER TABLE devices
    ADD CONSTRAINT uk_devices_device_ref
    UNIQUE (device_ref);
## MVP user stories and acceptance criteria

Each story maps to requirements and can later become a separate GitHub Issue.

### US-01 — Ingest a transaction (FR-01, FR-03)

**As an** API client, **I want** to submit a synthetic transaction **so that** FraudPulse can monitor it.

- **Given** a valid EUR transaction request, **when** the client submits it, **then** the API persists it and returns its unique ID and initial processing status.
- **Given** a request with a missing customer ID, non-positive amount, unsupported currency, invalid coordinates, or invalid timestamp, **when** submitted, **then** it is rejected with a clear validation error and no transaction is stored.
- **Given** the same idempotency key and identical payload, **when** submitted again, **then** the previously created transaction is returned rather than creating another one.
- **Given** the same idempotency key with a different payload, **when** submitted, **then** the API rejects the conflict.

### US-02 — Generate reproducible scenarios (FR-02)

**As a** developer, **I want** to run normal and suspicious synthetic scenarios **so that** I can demonstrate detection deterministically.

- **Given** a documented scenario seed, **when** I run the simulator, **then** it emits a reproducible number and sequence of synthetic transactions.
- **Given** the normal scenario, **when** analysis completes, **then** expected baseline risk outputs are asserted by automated tests.
- **Given** a suspicious scenario, **when** analysis completes, **then** at least one expected rule triggers.

### US-03 — Deliver transaction events reliably (FR-04, FR-06; NFR-02, NFR-06)

**As the** monitoring system, **I want** every accepted transaction to be available to downstream detection **so that** transactions are not missed during failures.

- **Given** a persisted transaction, **when** asynchronous delivery succeeds, **then** an event with the transaction ID, event ID, version, and occurred-at time is delivered for processing.
- **Given** a process restart after the transaction is committed but before event publication, **when** services recover, **then** the event is still eventually published.
- **Given** temporary Kafka unavailability, **when** the broker recovers, **then** pending events are retried without losing the transaction.

### US-04 — Calculate an explainable risk score (FR-07–FR-10)

**As a** fraud analyst, **I want** a score and clear reasons **so that** I can understand why a transaction was flagged.

- **Given** a transaction with no matching rules, **when** it is evaluated, **then** its score is 0, level LOW, and rule list empty.
- **Given** a transaction with matching rules, **when** it is evaluated, **then** the score equals the documented weighted sum capped at 100, and each triggered rule is recorded.
- **Given** scores of 39, 40, 69, and 70, **when** classified, **then** levels are LOW, MEDIUM, MEDIUM, and HIGH respectively.
- **Given** a recorded assessment, **when** its details are requested, **then** score, level, rule IDs, explanations, rule-set version, and evaluation timestamp are available.

### US-05 — Detect time/location patterns (FR-07; R-02–R-04)

**As the** fraud detection engine, **I want** to compare a customer's recent transactions **so that** I can identify suspicious sequences.

- **Given** four customer transactions in a five-minute window, **when** a fifth arrives inside that window, **then** R-02 triggers for the fifth transaction.
- **Given** transactions from two locations more than 1,000 km apart, **when** the second occurs within ten minutes of the first and comparison data is valid, **then** R-03 triggers.
- **Given** seven equal-amount customer transactions in a two-minute window, **when** an eighth arrives in that window, **then** R-04 triggers.
- **Given** transactions from different customers, **when** patterns are evaluated, **then** their histories are not combined.

### US-06 — Produce unique alerts (FR-12, FR-13, FR-15; NFR-03)

**As a** fraud analyst, **I want** an alert for each HIGH-risk assessment **so that** I can inspect suspicious activity.

- **Given** a completed HIGH-risk assessment, **when** alert processing runs, **then** one OPEN alert is stored with links to the assessment and transaction.
- **Given** a LOW or MEDIUM assessment, **when** alert processing runs, **then** no alert is created.
- **Given** the same event delivered twice, **when** processing repeats, **then** there remains one assessment and at most one alert for that transaction and rule-set version.

### US-07 — Investigate via read APIs (FR-05, FR-15, FR-18, FR-19)

**As a** fraud analyst, **I want** to browse transactions and alerts **so that** I can investigate detection results.

- **Given** stored transactions, **when** I query the list with paging/customer/time filters, **then** only matching records are returned with stable pagination.
- **Given** completed assessments, **when** I filter by HIGH risk, **then** only transactions with completed HIGH-risk assessments are returned.
- **Given** a newly accepted, unevaluated transaction, **when** I query its details, **then** its processing status is PENDING and it is not reported as LOW.
- **Given** an alert ID, **when** I request details, **then** the linked transaction, assessment, and triggered reasons are available.

### US-08 — Run the system locally and verify behavior (NFR-05, NFR-08–NFR-10)

**As a** contributor, **I want** a documented local startup and automated test suite **so that** I can reproduce the project without manual infrastructure setup.

- **Given** a clean clone with prerequisites installed, **when** I follow the README instructions, **then** required services start using Docker Compose and health checks become ready.
- **Given** a known simulation scenario, **when** I run it, **then** I can query the resulting risk assessments and alerts.
- **Given** duplicate Kafka deliveries and simulated temporary failures, **when** integration tests run, **then** the documented idempotency/recovery behavior is verified.
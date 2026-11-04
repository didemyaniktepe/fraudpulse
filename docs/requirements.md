# FraudPulse — Software Requirements Specification (SRS)

> **Status:** Draft v0.1 · **Last updated:** 2026-11-02 · **Target release:** MVP (v0.1)
>
> **Project:** FraudPulse — Real-Time Transaction Monitoring & Fraud Detection
>
> **Document owner:** FraudPulse project team

## 1. Purpose

This document defines **what FraudPulse must do** and **how its behavior will be verified**. It is the source of truth for the MVP's product scope, functional requirements, quality attributes, business rules, and acceptance criteria.

Architectural decisions (service boundaries, database schema, Kafka topic configuration, deployment topology) belong in separate design documents. Technologies mentioned below reflect current project constraints, not a substitute for business requirements.

### 1.1 Problem statement

Financial transaction streams can contain suspicious patterns that are difficult for analysts to recognize manually, especially across short time windows and multiple locations. A monitoring platform should analyze incoming transactions continuously, provide explainable risk assessments, and surface potentially fraudulent activity for investigation.

### 1.2 Product vision

**FraudPulse** is a portfolio-grade, event-driven system that ingests **synthetic financial transactions**, analyzes them asynchronously using configurable-in-code detection rules, assigns an explainable risk score, and creates alerts for high-risk activity. Fraud analysts can inspect transaction history, risk assessments, and alerts through APIs. A live dashboard and analyst workflows can follow in later releases.

**Important:** FraudPulse is a **simulation and demonstration system**, not a certified fraud-prevention service or a real payment processor. The MVP **does not approve, decline, block, or transfer real funds**.

### 1.3 Goals and success criteria

1. Demonstrate an end-to-end flow: transaction ingestion → durable storage → event publishing → detection → risk assessment → alert.
2. Explain **why** a transaction received a given risk score, including triggered rules.
3. Safely process events that may be delivered more than once without duplicate results or alerts.
4. Make the full local MVP runnable and testable using Docker Compose.
5. Provide measurable quality targets and repeatable automated tests suitable for a GitHub portfolio.

## 2. Scope

### 2.1 In scope — MVP (P0)

- REST API to submit synthetic transactions with input validation.
- A simulator that generates normal and suspicious synthetic transactions.
- Transaction persistence and query APIs.
- Asynchronous publication and consumption of transaction-created events through Apache Kafka.
- Four deterministic, rule-based fraud checks.
- Risk scoring and classification into LOW, MEDIUM, and HIGH.
- Persisted, explainable assessments linked to transactions.
- Alert creation for HIGH-risk assessments, with read APIs.
- Duplicate-event handling, retry/recovery strategy, structured logs, and automated tests.
- Local startup with Docker Compose and reproducible sample scenarios.

### 2.2 Out of scope — MVP

- Real payment processing, bank integrations, or card-network connectivity.
- Blocking/declining/holding a financial transaction.
- Machine-learning inference, model training, or production AI decision-making.
- Full analyst case management, assignments, comments, or notifications.
- User-facing React dashboard, live push updates, and analytics charts.
- Rule configuration through a UI or admin API.
- Multi-currency conversion and real-time foreign exchange feeds.
- Production Kubernetes rollout, multi-region availability, or regulatory certification.

These are **future enhancements**, not MVP completion requirements.

## 3. Actors and use cases

| Actor | Description | MVP actions |
|---|---|---|
| **Transaction Simulator** | Internal component that generates synthetic activity. | Generate normal/fraud-like scenarios and submit transactions. |
| **Transaction API Client** | A developer or test harness acting on behalf of a source system. | Submit and retrieve transactions. |
| **Fraud Analyst** | Human reviewing potentially suspicious events. | List transactions and alerts; inspect risk scores and triggered rules using read APIs. |
| **System Operator / Developer** | Maintains the local demonstration. | Start services, run tests, inspect failures and metrics/logs. |

### Core use cases

- **UC-01 — Submit a transaction:** Client sends a valid request; the system validates, records, and returns an identifier.
- **UC-02 — Publish a transaction event:** A persisted transaction is eventually represented by one logically unique event for downstream analysis.
- **UC-03 — Evaluate fraud rules:** The system processes a transaction and relevant historical context to compute a reproducible assessment.
- **UC-04 — Generate an alert:** A HIGH-risk assessment leads to one alert; LOW/MEDIUM assessments do not.
- **UC-05 — Investigate:** Analyst queries an alert and sees its transaction, risk score, and triggered rules.
- **UC-06 — Run a simulation:** Developer starts a deterministic dataset/scenario and observes the expected results.

## 4. Domain model and definitions

| Term | Meaning |
|---|---|
| **Transaction** | Immutable record of a simulated financial activity submitted for monitoring. |
| **Customer** | Simulated subject to whom one or more transactions belong. |
| **Transaction event** | Durable notification of a newly recorded transaction, with event identity and schema version. |
| **Fraud rule** | Deterministic predicate that can identify a suspicious behavioral pattern. |
| **Risk assessment** | Stored result for one transaction, including score, level, triggered rules and evaluation timestamp. |
| **Alert** | Stored item requiring analyst attention when an assessment is HIGH risk. |
| **Event time** | Timestamp when the simulated transaction happened (not necessarily when Kafka delivered it). |

### 4.1 Minimum transaction fields

| Field | Type | Constraints |
|---|---|---|
| `transactionId` | UUID | Generated or assigned exactly once; unique. |
| `customerId` | String | Required, non-blank simulated identifier. |
| `amount` | Decimal | Required; greater than 0; exact decimal handling. |
| `currency` | String | `EUR` only in MVP. |
| `merchantId` | String | Required, non-blank simulated identifier. |
| `location` | Coordinates + country | Required for location-dependent rules; valid latitude/longitude and ISO country code. |
| `occurredAt` | ISO-8601 timestamp | Required; timezone-aware UTC representation. |
| `createdAt` | ISO-8601 timestamp | Assigned by the system on ingestion. |

Use **fictional customer/merchant identifiers and synthetic locations**. Do not store real card numbers, credentials, or personal financial records.

### 4.2 Example input (illustrative, not a finalized API contract)

```json
{
  "customerId": "cust-42",
  "amount": 2500.00,
  "currency": "EUR",
  "merchantId": "merchant-12",
  "location": { "country": "DE", "latitude": 52.5200, "longitude": 13.4050 },
  "occurredAt": "2026-11-02T10:30:00Z"
}
```

The API generates `transactionId` and `createdAt`. A client-provided **idempotency key** should be supported for safe retries (see FR-03 and NFR-03).

## 5. Functional requirements

**Priority legend:** **P0 = MVP required**, **P1 = post-MVP enhancement**. Each requirement must be traceable to at least one verification test.

### 5.1 Transaction ingestion and history

| ID | Priority | Requirement |
|---|---|---|
| **FR-01** | P0 | The system **shall** accept valid synthetic transactions through a REST endpoint and reject invalid input with actionable errors. |
| **FR-02** | P0 | The system **shall** provide a simulator that produces both normal and known suspicious transaction scenarios. |
| **FR-03** | P0 | The system **shall** assign each accepted transaction a unique ID, persist it durably, and avoid creating duplicate transactions for retries with the same idempotency key and payload. |
| **FR-04** | P0 | The system **shall** publish a versioned transaction-created event for every durably accepted transaction, asynchronously and recoverably. |
| **FR-05** | P0 | The system **shall** offer paginated transaction-list and transaction-detail read endpoints, with customer and time-range filters. |

### 5.2 Detection and risk assessment

| ID | Priority | Requirement |
|---|---|---|
| **FR-06** | P0 | The system **shall** automatically and asynchronously evaluate each eligible transaction event. |
| **FR-07** | P0 | The system **shall** evaluate the four MVP detection rules specified in §6. |
| **FR-08** | P0 | The system **shall** calculate a deterministic risk score in the inclusive range **0–100**. |
| **FR-09** | P0 | The system **shall** map the score to **LOW**, **MEDIUM**, or **HIGH** using the thresholds in §6. |
| **FR-10** | P0 | The system **shall** persist the assessment, including triggered rule IDs, reasons, and rule-set version, and make it retrievable by transaction. |
| **FR-11** | P1 | Authorized administrators **should** be able to configure and version rule thresholds without redeploying. |

### 5.3 Alert management

| ID | Priority | Requirement |
|---|---|---|
| **FR-12** | P0 | The system **shall** create an alert when an assessment is HIGH risk. |
| **FR-13** | P0 | Each alert **shall** have a unique identifier and reference its transaction and assessment. |
| **FR-14** | P1 | Alerts **should** support a workflow with **OPEN**, **INVESTIGATING**, and **RESOLVED** states. In the MVP, newly created alerts have `OPEN` status and are read-only. |
| **FR-15** | P0 | The system **shall** provide paginated alert-list and alert-detail endpoints, including the reason for detection. |
| **FR-16** | P1 | Authorized fraud analysts **should** be able to change alert status and record a resolution reason. |
| **FR-17** | P1 | New alerts **should** be pushed to subscribed dashboards in near real time. |

### 5.4 Queries and reporting

| ID | Priority | Requirement |
|---|---|---|
| **FR-18** | P0 | The system **shall** support filtering transactions by risk level once their assessments are available. |
| **FR-19** | P0 | Transaction details **shall** expose assessment status, risk score, risk level, and triggered rules (when evaluated). |
| **FR-20** | P1 | A dashboard **should** show totals and breakdowns of transactions, HIGH-risk assessments, and alerts. |
| **FR-21** | P1 | Analysts **should** be able to view fraud metrics over selected time ranges. |

### 5.5 Asynchronous processing states

Because assessment is asynchronous, a newly accepted transaction can initially be **PENDING**. The transaction remains recorded even if its fraud evaluation has not completed. Processing status will be one of `PENDING`, `COMPLETED`, or `FAILED` (with an observable error status); this processing status is **separate** from the risk level. The system must never present a pending assessment as LOW risk.

## 6. Business rules — initial proposal

The following values are **demonstration defaults**, not evidence-based bank policies. The implementation must document their version; values may change through a reviewed requirements update.

| Rule ID | Name | MVP rule definition | Example contribution |
|---|---|---|---:|
| **R-01** | High amount | Transaction amount **> €5,000**. | +20 |
| **R-02** | High velocity | The customer's **5th or later transaction within an inclusive 5-minute event-time window**, including the current transaction. | +30 |
| **R-03** | Implausible travel | Consecutive known customer locations are **>1,000 km apart** with transaction event times **≤10 minutes apart**, for positive time differences. | +40 |
| **R-04** | Repeated equal amount | The customer's **8th or later transaction of the same exact EUR amount within an inclusive 2-minute event-time window**, including the current transaction. | +15 |

### 6.1 Scoring and classification

- Score = `min(100, sum(contributions from all triggered rules))`.
- **LOW:** 0–39, **MEDIUM:** 40–69, **HIGH:** 70–100.
- Save each triggered rule's ID, contribution, and explanation alongside the score.
- A HIGH result generates an alert; LOW and MEDIUM results do not.
- No rule matched means score = 0 (LOW), **not** proof that a transaction is safe.

### 6.2 Temporal and location rules

- Time windows use **event time** (`occurredAt`), not consumer processing time.
- Evaluate activity for the **same `customerId`** only.
- For MVP, a deterministic simulator should emit events in chronological order **per customer**. Preserving per-customer ordering should be part of the architecture design.
- The initial MVP may exclude out-of-order or excessively late events from velocity/travel calculations, but **must record that decision and its effect**. Proper event-time reordering/late-arrival handling is a follow-up design item.
- Distance calculations use geographical coordinates; **country code alone is insufficient** to infer implausible travel.
- A rule should not trigger when required comparison data is missing. Missing or invalid **required input** must be rejected at ingestion rather than silently interpreted as low risk.

## 7. Non-functional requirements

The following are **targets for validation**, not claims that the system already meets them.

| ID | Quality attribute | Verifiable requirement / target | Verification |
|---|---|---|---|
| **NFR-01** | Performance | Under a documented local workload of **10 transactions/second sustained for 10 minutes**, at least **95%** of accepted transactions shall reach a completed risk assessment within **2 seconds**. | Repeatable load test measuring ingestion-to-assessment latency. |
| **NFR-02** | Reliability | A committed transaction must not be silently lost before event publication. A recoverable, durable publication strategy is required. | Restart/failure-injection test between DB commit and Kafka publication. |
| **NFR-03** | Idempotency | Replaying an accepted event shall not create duplicate assessments or alerts for the same transaction/rule-set version; API retries with the same idempotency key and payload shall not create duplicate transactions. | Duplicate-delivery and API-retry integration tests. |
| **NFR-04** | Scalability | The design shall support adding detection consumers while preserving customer-local ordering and correctness. No numerical scale claim until benchmarked. | Parallel-consumer integration test and documented partitioning strategy. |
| **NFR-05** | Fault tolerance | Transient processing failures shall be retryable; unrecoverable/poison events shall be isolated in a DLQ or equivalent recoverable failure path with diagnostics. | Failure-injection tests. |
| **NFR-06** | Consistency | Transaction persistence and event publication must be coordinated to avoid the dual-write failure mode; eventual consistency of risk results must be explicit. | Outbox/recovery integration tests and state checks. |
| **NFR-07** | Security | No real payment data; validate all inputs; do not expose credentials in source control. Local MVP may use a documented development-only access mode; production-style role-based authentication is P1. | Validation/security checks, secrets scan, documentation review. |
| **NFR-08** | Observability | Services shall produce structured logs with transaction/event correlation identifiers, error diagnostics, and basic health metrics. Distributed tracing is P1. | Test run plus example logs/health checks. |
| **NFR-09** | Testability | All MVP detection rules and risk boundaries shall have automated unit tests; critical Kafka/DB paths shall have integration tests. No arbitrary coverage percentage substitutes for scenario coverage. | CI test report and coverage of acceptance scenarios. |
| **NFR-10** | Reproducibility | A fresh developer machine with documented prerequisites shall be able to start all MVP dependencies with Docker Compose and run sample scenarios. | Clean-clone setup walkthrough. |
| **NFR-11** | Maintainability | APIs/events shall have documented schemas/versions; each component's responsibility shall be documented; builds and tests shall run in CI. | README, schema review, CI run. |
| **NFR-12** | Data handling | Synthetic data only; logs must not contain secrets; sample data reset must be documented. | Fixture review and log inspection. |

**Measurement note:** NFR-01 measures from **successful acceptance of the transaction** to a **persisted completed assessment**, including asynchronous processing, under a documented local hardware profile. Failed/pending transactions must be counted and reported rather than silently excluded. Throughput and latency should be revisited after the first baseline benchmark.

## 8. MVP user stories and acceptance criteria

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

## 9. Traceability and verification plan

| Requirement groups | Primary verification |
|---|---|
| FR-01–FR-05 | REST validation/API integration tests, simulator fixtures, persistence, and event recovery tests. |
| FR-06–FR-10 | Fraud rule unit tests, risk-boundary tests, and Kafka-consumer integration tests. |
| FR-12–FR-13, FR-15 | Alert creation/idempotency tests and alert-query API tests. |
| FR-18–FR-19 | Transaction filtering and detail-response API tests. |
| NFR-01, NFR-04 | Repeatable local load/parallel-consumer tests with captured results. |
| NFR-02–NFR-06 | Restart, duplicate-event, outage, and failure-path integration tests. |
| NFR-07–NFR-12 | Validation/security review, logs/metrics inspection, CI checks, and clean-clone run. |

P1 requirements are not blockers for the MVP. They should receive their own stories and acceptance criteria when scheduled.

## 10. Constraints, assumptions, and risks

### Constraints and assumptions

- All data is fictional/synthetic; no real-world account data is needed.
- The MVP supports one currency (`EUR`) and a single local environment.
- Transactions arrive asynchronously; a temporary PENDING state is normal.
- Kafka event delivery can be **at least once**, not exactly once end-to-end; idempotent downstream processing is required.
- The project may use Java 21, Spring Boot, PostgreSQL, Kafka, Maven, and Docker Compose. These are intended stack choices, to be justified in architecture documentation.
- The simulator makes per-customer event order deterministic for MVP scenarios.

### Known risks and trade-offs

| Risk / trade-off | MVP response |
|---|---|
| False positives from simplistic fraud rules | Explainable scores; explicit simulation-only disclaimer; no automatic blocking. |
| DB/Kafka dual-write inconsistency | Durable publish coordination (e.g., transactional outbox) and recovery test. |
| Duplicate Kafka deliveries | Stable event identity and database-enforced uniqueness for results/alerts. |
| Out-of-order or late events | Specify and document deterministic MVP ordering assumptions; future event-time handling. |
| Unbounded state/window growth | Define retention/window strategy during architecture design; test normal workload. |
| Scope expansion | P0/P1 separation and change review before adding AI, dashboard, or Kubernetes. |

## 11. Open decisions to confirm before architecture work

1. **API authentication:** Is a local, development-only access mode acceptable for MVP, with Keycloak/role-based access scheduled for P1? **Proposed: yes.**
2. **Simulator interface:** CLI command, internal scheduled job, or separate service? **Proposed: CLI or script sending requests to the same REST API; no separate microservice initially.**
3. **Rule thresholds:** Approve illustrative thresholds and weights in §6? **Proposed: yes for MVP, versioned as `rules-v1`.**
4. **Late events:** Should out-of-order transactions be explicitly rejected, parked for later handling, or evaluated with known limitations? **Proposed: deterministic chronological simulator data for MVP, document late-event handling in architecture.**
5. **Alert workflow:** Is read-only `OPEN` status sufficient for the first release? **Proposed: yes; state transitions in P1.**

**These are proposed decisions, not silently finalized choices.** Architecture and API contracts should resolve them explicitly.


## 12. Next deliverables

1. `docs/architecture.md` — architecture decision records, service boundaries, sync vs async flows, fault handling.
2. `docs/diagrams/` — context/container/sequence diagrams (C4 and event flow).
3. `docs/api-contracts/` — OpenAPI definitions and validation/error semantics.
4. `docs/events/` — versioned Kafka event schemas, topic/partition/key conventions, retry/DLQ policy.
5. `docs/data-model.md` — entities, ownership, indexes, and data retention decisions.
6. Implementation issues derived from US-01 through US-08.

---

*This SRS is deliberately written before implementation. Update it when product decisions change, and keep architectural details in their own documents.*

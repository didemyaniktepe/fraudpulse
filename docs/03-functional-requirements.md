
## 1. Functional requirements

**Priority legend:** **P0 = MVP required**, **P1 = post-MVP enhancement**. Each requirement must be traceable to at least one verification test.

### 1.1 Transaction ingestion and history

| ID | Priority | Requirement |
|---|---|---|
| **FR-01** | P0 | The system **shall** accept valid synthetic transactions through a REST endpoint and reject invalid input with actionable errors. |
| **FR-02** | P0 | The system **shall** provide a simulator that produces both normal and known suspicious transaction scenarios. |
| **FR-03** | P0 | The system **shall** assign each accepted transaction a unique ID, persist it durably, and avoid creating duplicate transactions for retries with the same idempotency key and payload. |
| **FR-04** | P0 | The system **shall** publish a versioned transaction-created event for every durably accepted transaction, asynchronously and recoverably. |
| **FR-05** | P0 | The system **shall** offer paginated transaction-list and transaction-detail read endpoints, with customer and time-range filters. |

### 1.2 Detection and risk assessment

| ID | Priority | Requirement |
|---|---|---|
| **FR-06** | P0 | The system **shall** automatically and asynchronously evaluate each eligible transaction event. |
| **FR-07** | P0 | The system **shall** evaluate the four MVP detection rules specified in §6. |
| **FR-08** | P0 | The system **shall** calculate a deterministic risk score in the inclusive range **0–100**. |
| **FR-09** | P0 | The system **shall** map the score to **LOW**, **MEDIUM**, or **HIGH** using the thresholds in §6. |
| **FR-10** | P0 | The system **shall** persist the assessment, including triggered rule IDs, reasons, and rule-set version, and make it retrievable by transaction. |
| **FR-11** | P1 | Authorized administrators **should** be able to configure and version rule thresholds without redeploying. |

### 1.3 Alert management

| ID | Priority | Requirement |
|---|---|---|
| **FR-12** | P0 | The system **shall** create an alert when an assessment is HIGH risk. |
| **FR-13** | P0 | Each alert **shall** have a unique identifier and reference its transaction and assessment. |
| **FR-14** | P1 | Alerts **should** support a workflow with **OPEN**, **INVESTIGATING**, and **RESOLVED** states. In the MVP, newly created alerts have `OPEN` status and are read-only. |
| **FR-15** | P0 | The system **shall** provide paginated alert-list and alert-detail endpoints, including the reason for detection. |
| **FR-16** | P1 | Authorized fraud analysts **should** be able to change alert status and record a resolution reason. |
| **FR-17** | P1 | New alerts **should** be pushed to subscribed dashboards in near real time. |

### 1.4 Queries and reporting

| ID | Priority | Requirement |
|---|---|---|
| **FR-18** | P0 | The system **shall** support filtering transactions by risk level once their assessments are available. |
| **FR-19** | P0 | Transaction details **shall** expose assessment status, risk score, risk level, and triggered rules (when evaluated). |
| **FR-20** | P1 | A dashboard **should** show totals and breakdowns of transactions, HIGH-risk assessments, and alerts. |
| **FR-21** | P1 | Analysts **should** be able to view fraud metrics over selected time ranges. |

### 5.5 Asynchronous processing states

Because assessment is asynchronous, a newly accepted transaction can initially be **PENDING**. The transaction remains recorded even if its fraud evaluation has not completed. Processing status will be one of `PENDING`, `COMPLETED`, or `FAILED` (with an observable error status); this processing status is **separate** from the risk level. The system must never present a pending assessment as LOW risk.

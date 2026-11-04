## Non-functional requirements

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
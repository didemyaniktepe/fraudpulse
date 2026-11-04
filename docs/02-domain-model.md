## 1. Domain model and definitions

| Term | Meaning |
|---|---|
| **Transaction** | Immutable record of a simulated financial activity submitted for monitoring. |
| **Customer** | Simulated subject to whom one or more transactions belong. |
| **Transaction event** | Durable notification of a newly recorded transaction, with event identity and schema version. |
| **Fraud rule** | Deterministic predicate that can identify a suspicious behavioral pattern. |
| **Risk assessment** | Stored result for one transaction, including score, level, triggered rules and evaluation timestamp. |
| **Alert** | Stored item requiring analyst attention when an assessment is HIGH risk. |
| **Event time** | Timestamp when the simulated transaction happened (not necessarily when Kafka delivered it). |

### 1.1 Minimum transaction fields

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

### 1.2 Example input (illustrative, not a finalized API contract)

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
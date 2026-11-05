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

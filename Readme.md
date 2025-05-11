# 🏟️ Live Betting Platform - Case Study

This repository implements a clean architecture-based live betting system with support for real-time odds updates, bet placement, and concurrency-safe operations using Spring Boot 3.x, Java 17+, Redis, and H2.

---

## 📚 Features

* 📄 **Bulletin Management**

    * Create and view live match events
    * Automatic odds refresh with schedulers (every 5 minutes)

* 🎯 **Bet Slip Management**

    * Place bets on live events with multiplier & stake
    * Expected rate validation
    * Timeout and max investment enforcement
    * Redis-based distributed locking for concurrency safety

* 🛡️ **Authentication**

    * Basic Auth for UI & Swagger
    * Header-based mock customer authentication (`X-Customer-Id`)

* 📊 **API Documentation**

    * Fully annotated with Swagger/OpenAPI 3
    * Accessible via `/swagger-ui.html`

---

## 🧱 Tech Stack

| Layer         | Technology                  |
| ------------- |-----------------------------|
| Framework     | Spring Boot 3.x             |
| Language      | Java 17+                    |
| DB (Dev/Test) | H2 in-memory DB             |
| Cache/Lock    | Redis + Lettuce             |
| API Docs      | SpringDoc OpenAPI (Swagger) |
| Testing       | JUnit 5, Mockito, AssertJ   |

---

## 🚀 Getting Started

```bash
# Clone project
$ git clone https://github.com/daidorian09/Live-Betting-App
$ cd live-betting-app

# Run with Maven
$ ./mvnw spring-boot:run

# Or run with Docker
$ docker compose up --build

# Swagger UI
Visit: http://localhost:8080/swagger-ui.html
```

> Default credentials for Basic Auth:
>
> * Username: `admin`
> * Password: `admin`

---

## 📦 API Endpoints (Summary)

### 🎮 Bulletin

* `GET /api/bulletin` — View live matches with updated odds
* `POST /api/bulletin/events` — Create a new match

### 🎫 Bet Slip

* `POST /api/betslips` — Place a bet on an event

> Requires both `Authorization: Basic ...` and `X-Customer-Id` header

---

## ✅ Tests

![Coverage](https://img.shields.io/badge/Coverage-99%25-brightgreen)
[![JaCoCo Coverage](https://img.shields.io/badge/JaCoCo-View%20Report-brightgreen)](https://github.com/daidorian09/Live-Betting-App/blob/dev/docs/coverage/)

```bash
$ ./mvnw test
```

* Covers: Controller, UseCase, Repository behaviors
* Total test cases: 98 (unit + integration)
* Test coverage: **99%**
* Code coverage reports are generated using JaCoCo

---

## 🧠 Notes

* Odds i.e Events update every 5 seconds via a scheduled task
* Redis lock ensures safe concurrent betting on the same event
* Timeout (2s) and total investment limits are enforced per bet
* ⚠️ H2 does not fully support **PESSIMISTIC_READ** locking — Redis is used to guarantee concurrency control during betting operations.
---

## 📄 License

MIT © 2025 – Live Betting Clean Architecture Case Study

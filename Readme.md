# 🏟️ Live Betting Platform - Case Study

This repository implements a clean architecture-based live betting system with support for real-time odds updates, bet placement, and concurrency-safe operations using Spring Boot 3.x, Java 17+, Redis, and H2.

---

## 📚 Features

* 📄 **Bulletin Management**

    * Create and view live match events
    * Automatic odds refresh with schedulers (every 5s)

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
$ git clone https://github.com/your-org/live-betting-case-study.git
$ cd live-betting-case-study

# Run with Maven
$ ./mvnw spring-boot:run

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

```bash
$ ./mvnw test
```

* Covers: Controller, UseCase, Repository behaviors
* Total 98 test cases including both unit and integration tests

---

## 🧠 Notes

* Odds update every 5 seconds via a scheduled task
* Redis lock ensures safe concurrent betting on the same event
* Timeout (2s) and total investment limits are enforced per bet

---

## 📄 License

MIT © 2025 – Live Betting Clean Architecture Case Study

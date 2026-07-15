# 🎰 Blackjack API — Sprint 5.01

Blackjack backend built with **hexagonal architecture** using **Java 21** and **Spring Boot 3.5**. Dual persistence: **MongoDB Atlas** for game state, **MySQL** (Railway) for the player ranking.


---

## 🌍 Live demo

- 🚀 **API:** https://s05-01-blackjack.onrender.com
- 📖 **Swagger UI:** https://s05-01-blackjack.onrender.com/swagger-ui.html
- 🐳 **Docker Hub:** [`borgesjesk/blackjack-api`](https://hub.docker.com/r/borgesjesk/blackjack-api)



---

## 🧱 Stack

- ☕ Java 21 (Temurin)
- 🍃 Spring Boot 3.5.0 (Spring MVC)
- 🍀 MongoDB Atlas — game state
- 🐬 MySQL / Railway — player ranking
- 📖 Swagger / OpenAPI (springdoc)
- 🧪 JUnit 5 + Mockito + AssertJ
- 📊 JaCoCo (coverage)
- 🐳 Docker
- ☁️ Render (deployment)
- 🎯 Maven

---

## 🏛️ Architecture

Hexagonal (Ports & Adapters). The domain is **pure Java** — no Spring, no framework annotations, fully isolated from the infrastructure.

```
cat.itacademy.s05.t01.blackjack
├── domain/                       ← núcleo: Game, Hand, Deck, Card, GameState
│   ├── model/                    ← agregado + snapshot
│   ├── event/                    ← GameFinishedEvent
│   └── port/                     ← GameRepository, RankingRepository
├── application/                  ← use cases + DTOs + exceptions
│   └── usecase/                  ← StartGame, PlayerHit, PlayerStand,
│                                     RenamePlayer, GetGame, GetRanking
└── infrastructure/               ← adapters
    ├── adapter/in/rest/          ← controllers REST
    ├── adapter/out/mongo/        ← persistência do jogo
    ├── adapter/out/mysql/        ← persistência do ranking
    ├── adapter/out/event/        ← listener de eventos de domínio
    └── config/                   ← Spring configuration
```

**Key decisions:**

- 🃏 `Deck` takes a `Consumer<List<Card>>` as its shuffle strategy → production uses `Collections::shuffle`, tests use a fixed order → **deterministic tests**
- 📸 `Game.toSnapshot()` / `Game.fromSnapshot()` → persistence without breaking encapsulation
- 📢 `GameFinishedEvent` published via `ApplicationEventPublisher` → listener writes the result to MySQL in a decoupled way
- 🎯 Dealer stands on **17+** (classic casino rule, explicit and tested)
- 🔐 Credentials **never** in code — everything via environment variables

---

## 🎮 Endpoints

Base URL: `http://localhost:8082`

### Games — `/api/v1/games`

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/v1/games` | Creates a new game and deals the initial cards |
| `GET` | `/api/v1/games/{id}` | Returns the current game state |
| `POST` | `/api/v1/games/{id}/hit` | Player draws an extra card |
| `POST` | `/api/v1/games/{id}/stand` | Player stands — dealer plays and the game resolves |
| `PUT` | `/api/v1/games/{id}/player` | Renames the player of a game |
| `DELETE` | `/api/v1/games/{id}` | Deletes a game |

### Ranking — `/api/v1/ranking`

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/v1/ranking?limit=10` | Returns the leaderboard, sorted by wins |

📖 **Swagger documentation:** `/swagger-ui.html`

---

## ▶️ Run locally

### 1️⃣ Prerequisites

- Java 21
- Maven 3.9+
- A MongoDB instance (Atlas or local)
- A MySQL instance (Railway, Docker, local...)

### 2️⃣ Environment variables

Configure in IntelliJ (Run Configuration → Environment variables) or export before running:

```
MONGODB_URI=mongodb+srv://<user>:<pass>@<cluster>.mongodb.net/blackjack
MYSQL_URL=jdbc:mysql://<host>:<port>/<database>
MYSQL_USER=<user>
MYSQL_PASS=<pass>
```

### 3️⃣ Build + Run

```bash
mvn clean install
mvn spring-boot:run
```

App runs at `http://localhost:8082` 🚀

---

## 🐳 Run with Docker

**Pull the public image:**

```bash
docker pull borgesjesk/blackjack-api:latest
```

**Run the container:**

```bash
docker run -d --name blackjack -p 8082:8082 \
  -e MONGODB_URI="mongodb+srv://<user>:<pass>@<cluster>.mongodb.net/blackjack" \
  -e MYSQL_URL="jdbc:mysql://<host>:<port>/<database>" \
  -e MYSQL_USER="<user>" \
  -e MYSQL_PASS="<pass>" \
  borgesjesk/blackjack-api:latest
```

Or build it locally:

```bash
docker build -t blackjack-api .
```

---

## 🧪 Tests

```bash
mvn test
```

**Coverage:**

- ✅ Pure domain — `Card`, `Hand`, `Deck`, `Game` (lifecycle, blackjack, bust, stand, guards, events)
- ✅ Use cases — happy path + unhappy path for every one
- ✅ Listener — verifies that `GameFinishedEvent` writes correctly into the ranking
- ✅ Deterministic tests using injected shuffle strategy

JaCoCo report available at `target/site/jacoco/index.html` after `mvn verify`.

---

## 🎲 Full flow — `curl` example

Replace `<HOST>` with `localhost:8082` (local) or `s05-01-blackjack.onrender.com` (production).

```bash
# 1) Create a game (playerName in body is optional)
curl -X POST http://<HOST>/api/v1/games \
  -H "Content-Type: application/json" \
  -d '{"playerName":"Jess"}'

# 2) Rename the player at any point
curl -X PUT http://<HOST>/api/v1/games/<GAME_ID>/player \
  -H "Content-Type: application/json" \
  -d '{"playerName":"Jess"}'

# 3) Draw an extra card
curl -X POST http://<HOST>/api/v1/games/<GAME_ID>/hit

# 4) Stand → dealer plays → resolve the game
curl -X POST http://<HOST>/api/v1/games/<GAME_ID>/stand

# 5) See the ranking
curl http://<HOST>/api/v1/ranking

# 6) Delete a game
curl -X DELETE http://<HOST>/api/v1/games/<GAME_ID>
```

---

## 🎯 Game rules

- 🃏 Standard 52-card deck, shuffled at every new game
- 👤 Player plays first: can `hit` (draw) or `stand`
- 🤖 Dealer stands on **17 or above** (classic casino rule)
- 🎉 **Blackjack** = 21 with the first two cards → instant win
- 💥 **Bust** > 21 → instant loss
- 🤝 Tie = **PUSH**
- 🏆 Every game result is stored in the ranking

---

**👩‍💻 Jessica Borges**  
Java Backend Developer & Cybersecurity Student  
🔗 [linkedin.com/in/jessica-borges-cyber](https://linkedin.com/in/jessica-borges-cyber)  
🐙 [github.com/Borgesjesk](https://github.com/Borgesjesk)

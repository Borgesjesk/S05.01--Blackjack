# 🎰 Blackjack API — Sprint 5.01

Backend de Blackjack construído em **arquitetura hexagonal** com **Java 21**, **Spring Boot 3.5** e persistência dupla — **MongoDB** para o estado do jogo, **MySQL** para o ranking dos jogadores.

Projeto final do bootcamp de Java Backend do **IT Academy — Barcelona Activa** 🇪🇸

---

## 🧱 Stack

- ☕ Java 21 (Temurin)
- 🍃 Spring Boot 3.5.0
- 🍀 MongoDB Atlas (estado do jogo)
- 🐬 MySQL / Railway (rankings)
- 📖 Swagger / OpenAPI
- 🧪 JUnit 5 + Mockito + AssertJ
- 📊 JaCoCo (coverage)
- 🎯 Maven

---

## 🏛️ Arquitetura

Hexagonal (Ports & Adapters). O domínio é **Java puro**, sem Spring, sem anotações de framework — totalmente isolado da infraestrutura.

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

**Decisões-chave:**

- 🃏 `Deck` recebe um `Consumer<List<Card>>` como estratégia de shuffle → em produção usa `Collections::shuffle`, nos testes usa ordem fixa (**testes determinísticos**)
- 📸 `Game.toSnapshot()` / `Game.fromSnapshot()` → persistência sem quebrar encapsulamento
- 📢 `GameFinishedEvent` publicado via `ApplicationEventPublisher` → listener grava o resultado no MySQL de forma assíncrona
- 🎯 Dealer para em 17+ (regra clássica, explícita e testada)
- 🔐 Credenciais nunca no código — tudo por variáveis de ambiente

---

## 🎮 Endpoints

Base URL: `http://localhost:8082`

### Jogo — `/api/v1/games`

| Método | Path | Descrição |
|---|---|---|
| `POST` | `/api/v1/games` | Cria um novo jogo e distribui as cartas iniciais |
| `GET` | `/api/v1/games/{id}` | Consulta o estado atual do jogo |
| `POST` | `/api/v1/games/{id}/hit` | Player pede mais uma carta |
| `POST` | `/api/v1/games/{id}/stand` | Player planta — dealer joga e o resultado é resolvido |
| `PUT` | `/api/v1/games/{id}/player` | Renomeia o player de um jogo |

### Ranking — `/api/v1/ranking`

| Método | Path | Descrição |
|---|---|---|
| `GET` | `/api/v1/ranking?limit=10` | Retorna os players ordenados por vitórias |

📖 **Documentação Swagger**: `http://localhost:8082/swagger-ui.html`

---

## ▶️ Como rodar

### 1️⃣ Pré-requisitos

- Java 21
- Maven 3.9+
- Uma instância MongoDB (Atlas ou local)
- Uma instância MySQL (Railway, local, Docker...)

### 2️⃣ Variáveis de ambiente

Configure no IntelliJ (Run Configuration → Environment variables):

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

App sobe em `http://localhost:8082` 🚀

---

## 🧪 Testes

```bash
mvn test
```

**Cobertura:**

- ✅ Domínio puro — `Card`, `Hand`, `Deck`, `Game` (lifecycle, blackjack, bust, stand, guards, eventos)
- ✅ Use cases — happy path + unhappy path para todos
- ✅ Listener — verifica que `GameFinishedEvent` grava corretamente no ranking
- ✅ Testes determinísticos com shuffle strategy injetado

Relatório JaCoCo em `target/site/jacoco/index.html` depois de `mvn verify`.

---

## 🎲 Fluxo completo — exemplo com `curl`

```bash
# 1) Criar jogo
curl -X POST http://localhost:8082/api/v1/games

# 2) Renomear player (usa o gameId retornado acima)
curl -X PUT http://localhost:8082/api/v1/games/<GAME_ID>/player \
  -H "Content-Type: application/json" \
  -d '{"playerName":"Jess"}'

# 3) Pedir carta
curl -X POST http://localhost:8082/api/v1/games/<GAME_ID>/hit

# 4) Plantar → dealer joga → resultado
curl -X POST http://localhost:8082/api/v1/games/<GAME_ID>/stand

# 5) Ver ranking
curl http://localhost:8082/api/v1/ranking
```

---

## 🎯 Regras do jogo

- 🃏 Baralho padrão de 52 cartas, embaralhado a cada nova partida
- 👤 Player joga primeiro: pode `hit` (pedir carta) ou `stand` (plantar)
- 🤖 Dealer para em **17 ou mais** (regra clássica de casino)
- 🎉 **Blackjack** = 21 com as duas primeiras cartas → vitória imediata
- 💥 **Bust** > 21 → derrota imediata
- 🤝 Empate = **PUSH**
- 🏆 O resultado de cada partida é registrado no ranking

---
 
** 👩‍💻  Jessica Borges**  
Java Backend Developer & Cybersecurity Student  
🔗 [linkedin.com/in/jessica-borges-cyber](https://linkedin.com/in/jessica-borges-cyber)  
🐙 [github.com/Borgesjesk](https://github.com/Borgesjesk)

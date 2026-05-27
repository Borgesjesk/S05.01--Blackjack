package cat.itacademy.s05.t01.blackjack.domain.event;

import cat.itacademy.s05.t01.blackjack.domain.model.GameState;

import java.time.Instant;

public record GameFinishedEvent(
        String gameId,
        GameState result,
        int playerScore,
        int dealerScore,
        Instant finishedAt
) {}

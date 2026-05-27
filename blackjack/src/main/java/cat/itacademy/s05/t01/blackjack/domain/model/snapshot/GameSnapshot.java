package cat.itacademy.s05.t01.blackjack.domain.model.snapshot;

import cat.itacademy.s05.t01.blackjack.domain.model.GameState;

import java.time.Instant;

public record GameSnapshot(
        String gameId,
        DeckSnapshot deck,
        HandSnapshot playerHand,
        HandSnapshot dealerHand,
        GameState gameState,
        Instant createdAt
) {
}
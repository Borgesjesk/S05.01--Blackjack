package cat.itacademy.s05.t01.blackjack.domain.model.snapshot;

import cat.itacademy.s05.t01.blackjack.domain.model.Card;

import java.util.List;

public record DeckSnapshot(List<Card> cards) {
    public DeckSnapshot {
        cards = List.copyOf(cards);
    }
}
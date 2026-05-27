package cat.itacademy.s05.t01.blackjack.domain.model.snapshot;

import cat.itacademy.s05.t01.blackjack.domain.model.Card;

import java.util.List;

public record HandSnapshot(List<Card> cards) {
    public HandSnapshot {
        cards = List.copyOf(cards);
    }
}
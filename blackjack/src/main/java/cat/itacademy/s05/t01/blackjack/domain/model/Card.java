package cat.itacademy.s05.t01.blackjack.domain.model;

import java.util.Objects;

public record Card(Suit suit, Rank rank) {

    public Card {
        Objects.requireNonNull(suit, "Suit must not be null");
        Objects.requireNonNull(rank, "Rank must not be null");
    }

    public int value() {
        return rank.getValue();
    }
}

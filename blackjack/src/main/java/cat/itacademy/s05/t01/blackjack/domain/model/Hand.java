package cat.itacademy.s05.t01.blackjack.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Hand {

    private static final int BLACKJACK = 21;
    private static final int ACE_REDUCTION = 10;

    private final List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public Hand(List<Card> cards) {
        Objects.requireNonNull(cards, "Cards list must not be null");
        this.cards = new ArrayList<>(cards);
    }

    public void addCard(Card card) {
        Objects.requireNonNull(card, "Card must not be null");
        cards.add(card);
    }

    public int score() {
        int total = cards.stream()
                .mapToInt(Card::value)
                .sum();

        long aceCount = cards.stream()
                .filter(card -> card.rank() == Rank.ACE)
                .count();

        while (total > BLACKJACK && aceCount > 0) {
            total -= ACE_REDUCTION;
            aceCount--;
        }

        return total;
    }

    public boolean isBust() {
        return score() > BLACKJACK;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && score() == BLACKJACK;
    }

    public int cardCount() {
        return cards.size();
    }

    public List<Card> cards() {
        return Collections.unmodifiableList(cards);
    }
}

package cat.itacademy.s05.t01.blackjack.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Deck {

    private final List<Card> cards;

    private Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public static Deck createShuffled(Consumer<List<Card>> shuffleStrategy) {
        Objects.requireNonNull(shuffleStrategy, "Shuffle strategy must not be null");
        List<Card> fullDeck = buildStandardDeck();
        shuffleStrategy.accept(fullDeck);
        return new Deck(fullDeck);
    }

    public static Deck fromExisting(List<Card> remainingCards) {
        Objects.requireNonNull(remainingCards, "Remaining cards must not be null");
        return new Deck(remainingCards);
    }

    public Card draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Cannot draw from an empty deck");
        }
        return cards.removeFirst();
    }

    public int remainingCards() {
        return cards.size();
    }

    public List<Card> cards() {
        return Collections.unmodifiableList(cards);
    }

    private static List<Card> buildStandardDeck() {
        List<Card> deck = new ArrayList<>(52);
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
        return deck;
    }
}

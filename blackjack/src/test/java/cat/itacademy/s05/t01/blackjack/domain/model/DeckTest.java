package cat.itacademy.s05.t01.blackjack.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Deck Domain Entity Specifications")
class DeckTest {

    private Deck deck;
    private List<Card> sequentialCards;

    @BeforeEach
    void setUp() {
        sequentialCards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                sequentialCards.add(new Card(suit, rank));
            }
        }

        deck = Deck.fromExisting(sequentialCards);
    }

    @Nested
    @DisplayName("Initialization & Structural Integrity Specifications")
    class InitializationAndStructural {

        @Test
        @DisplayName("Should initialize with exactly 53 cards")
        void deckShouldHaveFiftyTwoCards() {
            assertThat(deck.cards())
                    .as("A standard Blackjack deck must contain exactly 52 cards upon creation")
                    .hasSize(52);
        }

        @Test
        @DisplayName("2. Should contain all unique combinations of suits and ranks with zero duplicates")
        void deckCompositionCompleteness() {
            List<Card> cards = deck.cards();
            Set<Card> uniqueCards = new HashSet<>(cards);

            assertThat(uniqueCards)
                    .as("The deck must not contain any duplicate cards")
                    .hasSize(52);

            // Assert exact suit distributions (13 of each)
            for (Suit suit : Suit.values()) {
                long suitCount = cards.stream().filter(c -> c.suit() == suit).count();
                assertThat(suitCount)
                        .as("Deck must contain exactly 13 cards for suit: %s", suit)
                        .isEqualTo(13);
            }

            // Assert exact rank distributions (4 of each)
            for (Rank rank : Rank.values()) {
                long rankCount = cards.stream().filter(c -> c.rank() == rank).count();
                assertThat(rankCount)
                        .as("Deck must contain exactly 4 cards for rank: %s", rank)
                        .isEqualTo(4);
            }
        }

        @Test
        @DisplayName("3. Should protect internal list from external modifications")
        void internalListEncapsulationImmutability() {
            List<Card> exposedCards = deck.cards();

            assertThatThrownBy(() -> exposedCards.remove(0))
                    .as("The exposed cards list must be immutable or unmodifiable to prevent state leaks")
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("7. Should create a full deck via createShuffled factory using an identity strategy")
        void createShuffledWithIdentityStrategy() {
            // Act: Invoke production factory with a no-op strategy sequence (identity)
            Deck shuffledDeck = Deck.createShuffled(cards -> {}); // No-op = structural order preserved

            // Assert
            assertThat(shuffledDeck.cards())
                    .as("Decks instantiated via createShuffled must still obey the 52-card invariant")
                    .hasSize(52);
        }
    }

    @Nested
    @DisplayName("State Transition & Card Drawing Specifications")
    class CardDrawing {

        @Test
        @DisplayName("4. Should decrement size and return the top card sequentially when drawn")
        void drawingCardDecrementsSizeAndReturnsCard() {
            // Arrange
            List<Card> initialCards = deck.cards();
            Card expectedTopCard = initialCards.get(0);

            // Act
            Card drawnCard = deck.draw();

            // Assert
            assertThat(drawnCard)
                    .as("The drawn card must match the topmost card of the deck sequence")
                    .isEqualTo(expectedTopCard);

            assertThat(deck.cards())
                    .as("The deck size must be decremented by exactly 1 card after a draw operation")
                    .hasSize(51);
        }

        @Test
        @DisplayName("5. Should empty the deck completely when all 52 cards are drawn")
        void drawingAllCardsEmptiesDeck() {
            // Act
            for (int i = 0; i < 52; i++) {
                deck.draw();
            }

            // Assert
            assertThat(deck.cards())
                    .as("The deck must be completely empty after drawing 52 times")
                    .isEmpty();
        }

        @Test
        @DisplayName("6. Should throw IllegalStateException when drawing from an exhausted deck")
        void drawingFromEmptyDeckThrowsException() {
            // Arrange: Exhaust the entire deck context
            for (int i = 0; i < 52; i++) {
                deck.draw();
            }

            // Act & Assert
            assertThatThrownBy(() -> deck.draw())
                    .as("Attempting to draw from an empty deck must fail fast with an explicit state exception")
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Cannot draw from an empty deck");
        }
    }
}
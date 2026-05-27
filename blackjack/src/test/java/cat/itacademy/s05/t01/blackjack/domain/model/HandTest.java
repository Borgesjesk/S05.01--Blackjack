package cat.itacademy.s05.t01.blackjack.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Hand Domain Entity Specification")
class HandTest {

    private Hand hand;

    @BeforeEach
    void setUp() {
        hand = new Hand();
    }

    @Nested
    @DisplayName("Score Calculation Specification")
    class ScoreCalculation {

        @Test
        @DisplayName("Should calculate correct score when no Aces are present")
        void scoreWithNoAces() {

            Card ten = new Card(Suit.SPADES, Rank.TEN);
            Card seven = new Card(Suit.HEARTS, Rank.SEVEN);

            hand.addCard(ten);
            hand.addCard(seven);

            assertThat(hand.score())
                    .as("A hand with a 10 and a 7 must equal 17")
                    .isEqualTo(17);
        }

        @Test
        @DisplayName("Should value an Ace as 11 when it does not cause a bust")
        void aceCountsAsElevenWhenSafe() {
            Card ace = new Card(Suit.SPADES, Rank.ACE);
            Card nine = new Card(Suit.DIAMONDS, Rank.NINE);

            hand.addCard(ace);
            hand.addCard(nine);

            assertThat(hand.score())
                    .as("An Ace and a 9 should safely evaluate to 20")
                    .isEqualTo(20);
        }

        @Test
        @DisplayName("Should reduce an Ace value to 1 when a 11-valuation causes a bust")
        void aceReducesToOneWhenBust() {

            Card ace = new Card(Suit.SPADES, Rank.ACE);
            Card ten = new Card(Suit.CLUBS, Rank.TEN);
            Card five = new Card(Suit.HEARTS, Rank.FIVE);

            hand.addCard(ace);
            hand.addCard(ten);
            hand.addCard(five);

            assertThat(hand.score())
                    .as("Ace must reduce to 1 to prevent an artificial bust, totaling 16")
                    .isEqualTo(16);
            assertThat(hand.isBust())
                    .as("Hand score is 16, which is safe from busting")
                    .isFalse();
        }

        @Test
        @DisplayName("Should evaluate two Aces as 12 to maximize safe score")
        void twoAcesAs12ToMaximizeSafe() {

            Card firstAce = new Card(Suit.SPADES, Rank.ACE);
            Card secondAce = new Card(Suit.HEARTS, Rank.ACE);

            hand.addCard(firstAce);
            hand.addCard(secondAce);

            assertThat(hand.score())
                    .as("Two Aces must be evaluated as 12 to stay below the bust threshold")
                    .isEqualTo(12);
        }
    }

    @Nested
    @DisplayName("Blackjack Detection Specification")
    class BlackjackDetection {

        @Test
        @DisplayName("Should identify a Natural Blackjack with exactly two cards totaling 21")
        void blackjackWithTwoCards() {

            Card ace = new Card(Suit.SPADES, Rank.ACE);
            Card jack = new Card(Suit.DIAMONDS, Rank.JACK);

            hand.addCard(ace);
            hand.addCard(jack);

            assertThat(hand.score()).isEqualTo(21);
            assertThat(hand.isBlackjack())
                    .as("An initial deal of an Ace and a 10-value card is a Natural Blackjack")
                    .isTrue();
        }

        @Test
        @DisplayName("Should NOT flag a hand as Blackjack if score is 21 but comprised of three cards")
        void twentyOneWithThreeCardsIsNotBlackjack() {

            Card sevenOfSpades = new Card(Suit.SPADES, Rank.SEVEN);
            Card sevenOfHearts = new Card(Suit.HEARTS, Rank.SEVEN);
            Card sevenOfClubs = new Card(Suit.CLUBS, Rank.SEVEN);

            hand.addCard(sevenOfSpades);
            hand.addCard(sevenOfHearts);
            hand.addCard(sevenOfClubs);

            assertThat(hand.score()).isEqualTo(21);
            assertThat(hand.isBlackjack())
                    .as("A score of 21 achieved via three cards is a standard 21, not a Natural Blackjack")
                    .isFalse();
        }
    }
}
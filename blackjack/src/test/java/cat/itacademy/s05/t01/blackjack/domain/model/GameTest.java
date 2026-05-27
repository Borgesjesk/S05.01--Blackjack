package cat.itacademy.s05.t01.blackjack.domain.model;

import cat.itacademy.s05.t01.blackjack.domain.event.GameFinishedEvent;
import cat.itacademy.s05.t01.blackjack.domain.model.snapshot.GameSnapshot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Game Aggregate Root Specifications")
class GameTest {

    @Nested
    @DisplayName("Initial Deal Specifications")
    class InitialDeal {

        @Test
        @DisplayName("1. Should correctly distribute initial cards and set state to PLAYER_TURN")
        void initialDealDistribution() {
            // Arrange & Act: Rig deck for a standard opening (Player: 19, Dealer: 11)
            Game game = Game.start(cards -> {
                cards.clear();
                cards.addAll(List.of(
                        new Card(Suit.HEARTS, Rank.TEN),   // Player card 1
                        new Card(Suit.SPADES, Rank.FIVE),  // Dealer card 1
                        new Card(Suit.CLUBS, Rank.NINE),   // Player card 2
                        new Card(Suit.DIAMONDS, Rank.SIX)  // Dealer card 2
                ));
            });

            GameSnapshot snapshot = game.toSnapshot();

            // Assert
            assertThat(snapshot.gameState()).isEqualTo(GameState.PLAYER_TURN);
            assertThat(snapshot.playerHand().cards()).hasSize(2)
                    .containsExactly(new Card(Suit.HEARTS, Rank.TEN), new Card(Suit.CLUBS, Rank.NINE));
            assertThat(snapshot.dealerHand().cards()).hasSize(2)
                    .containsExactly(new Card(Suit.SPADES, Rank.FIVE), new Card(Suit.DIAMONDS, Rank.SIX));
        }

        @Test
        @DisplayName("2. Should automatically transition to PLAYER_BLACKJACK when Player gets a Natural Blackjack")
        void naturalBlackjackOnInitialDeal() {
            // Arrange & Act: Rig deck for immediate player blackjack (Ace + King)
            Game game = Game.start(cards -> {
                cards.clear();
                cards.addAll(List.of(
                        new Card(Suit.SPADES, Rank.ACE),    // Player card 1
                        new Card(Suit.HEARTS, Rank.FIVE),   // Dealer card 1
                        new Card(Suit.CLUBS, Rank.KING),    // Player card 2 (Total 21!)
                        new Card(Suit.DIAMONDS, Rank.TEN)   // Dealer card 2
                ));
            });

            GameSnapshot snapshot = game.toSnapshot();

            // Assert
            assertThat(snapshot.gameState())
                    .as("Game must detect natural Blackjack immediately and transition to PLAYER_BLACKJACK")
                    .isEqualTo(GameState.PLAYER_BLACKJACK);
        }
    }

    @Nested
    @DisplayName("Player Action Specifications")
    class PlayerActions {

        @Test
        @DisplayName("3. Should add card to Player hand and remain in PLAYER_TURN when hit is safe")
        void safePlayerHit() {
            // Arrange: Player starts with 12 (Five + Seven)
            Game game = Game.start(cards -> {
                cards.clear();
                cards.addAll(List.of(
                        new Card(Suit.HEARTS, Rank.FIVE),
                        new Card(Suit.SPADES, Rank.TEN),
                        new Card(Suit.CLUBS, Rank.SEVEN),
                        new Card(Suit.DIAMONDS, Rank.FOUR),
                        new Card(Suit.CLUBS, Rank.FIVE)     // Next card drawn on hit (12 + 5 = 17)
                ));
            });

            // Act
            game.playerHit();
            GameSnapshot snapshot = game.toSnapshot();

            // Assert
            assertThat(snapshot.gameState()).isEqualTo(GameState.PLAYER_TURN);
            assertThat(snapshot.playerHand().cards()).hasSize(3)
                    .contains(new Card(Suit.CLUBS, Rank.FIVE));
        }

        @Test
        @DisplayName("4. Should automatically transition to PLAYER_BUST when Player hit causes a bust")
        void playerHitCausesBust() {
            // Arrange: Player starts with 19 (Ten + Nine)
            Game game = Game.start(cards -> {
                cards.clear();
                cards.addAll(List.of(
                        new Card(Suit.HEARTS, Rank.TEN),
                        new Card(Suit.SPADES, Rank.FIVE),
                        new Card(Suit.CLUBS, Rank.NINE),
                        new Card(Suit.DIAMONDS, Rank.SIX),
                        new Card(Suit.CLUBS, Rank.FIVE)     // Next card drawn on hit (19 + 5 = 24 -> Bust)
                ));
            });

            // Act
            game.playerHit();
            GameSnapshot snapshot = game.toSnapshot();

            // Assert
            assertThat(snapshot.gameState())
                    .as("Player busted, game must immediately transition state to PLAYER_BUST")
                    .isEqualTo(GameState.PLAYER_BUST);
        }
    }

    @Nested
    @DisplayName("Dealer Execution & Stand Specifications")
    class StandAndDealerAI {

        @Test
        @DisplayName("5. Should trigger Dealer execution loop and result in PLAYER_WINS if Player score is higher")
        void dealerStandsOnSeventeenPlusAndPlayerWins() {
            // Arrange: Player stands on 19. Dealer starts with 11 (Five + Six)
            Game game = Game.start(cards -> {
                cards.clear();
                cards.addAll(List.of(
                        new Card(Suit.HEARTS, Rank.TEN),
                        new Card(Suit.SPADES, Rank.FIVE),
                        new Card(Suit.CLUBS, Rank.NINE),
                        new Card(Suit.DIAMONDS, Rank.SIX),
                        new Card(Suit.HEARTS, Rank.SEVEN)   // Dealer draws this on stand (11 + 7 = 18). 18 >= 17, so dealer stops.
                ));
            });

            // Act
            game.playerStand();
            GameSnapshot snapshot = game.toSnapshot();

            // Assert
            assertThat(snapshot.dealerHand().cards()).hasSize(3)
                    .contains(new Card(Suit.HEARTS, Rank.SEVEN));
            assertThat(snapshot.gameState())
                    .as("Player has 19, Dealer has 18 -> State must transition to PLAYER_WINS")
                    .isEqualTo(GameState.PLAYER_WINS);
        }

        @Test
        @DisplayName("6. Should evaluate a PUSH game if scores are equal after Dealer execution")
        void gameEvaluatesToPush() {
            // Arrange: Player stands on 18. Dealer starts with 11 (Five + Six)
            Game game = Game.start(cards -> {
                cards.clear();
                cards.addAll(List.of(
                        new Card(Suit.HEARTS, Rank.TEN),
                        new Card(Suit.SPADES, Rank.FIVE),
                        new Card(Suit.CLUBS, Rank.EIGHT),
                        new Card(Suit.DIAMONDS, Rank.SIX),
                        new Card(Suit.HEARTS, Rank.SEVEN)   // Dealer draws this on stand (11 + 7 = 18). Equal scores.
                ));
            });

            // Act
            game.playerStand();
            GameSnapshot snapshot = game.toSnapshot();

            // Assert
            assertThat(snapshot.gameState())
                    .as("Both player and dealer have 18 -> Result must transition to PUSH")
                    .isEqualTo(GameState.PUSH);
        }
    }

    @Nested
    @DisplayName("Invalid State Action Specifications")
    class InvalidStateActions {

        @Test
        @DisplayName("7. Should throw IllegalStateException when hitting on a finished game")
        void hitOnFinishedGameThrows() {
            // Arrange: Player starts with natural Blackjack, immediately finishing the game
            Game game = Game.start(cards -> {
                cards.clear();
                cards.addAll(List.of(
                        new Card(Suit.SPADES, Rank.ACE),
                        new Card(Suit.HEARTS, Rank.FIVE),
                        new Card(Suit.CLUBS, Rank.KING),
                        new Card(Suit.DIAMONDS, Rank.TEN)
                ));
            });

            // Act & Assert
            assertThatThrownBy(() -> game.playerHit())
                    .as("Executing playerHit() on an already completed game must throw an IllegalStateException")
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("8. Should throw IllegalStateException when standing on a finished game")
        void standOnFinishedGameThrows() {
            // Arrange: Player starts with natural Blackjack, immediately finishing the game
            Game game = Game.start(cards -> {
                cards.clear();
                cards.addAll(List.of(
                        new Card(Suit.SPADES, Rank.ACE),
                        new Card(Suit.HEARTS, Rank.FIVE),
                        new Card(Suit.CLUBS, Rank.KING),
                        new Card(Suit.DIAMONDS, Rank.TEN)
                ));
            });

            // Act & Assert
            assertThatThrownBy(() -> game.playerStand())
                    .as("Executing playerStand() on an already completed game must throw an IllegalStateException")
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    @DisplayName("Domain Event Specifications")
    class DomainEvents {

        @Test
        @DisplayName("9. Should record a GameFinishedEvent inside domainEvents collection after game ends")
        void containsGameFinishedEventAfterGameEnds() {
            // Arrange: Player stands on 19. Dealer starts with 11 (Five + Six)
            Game game = Game.start(cards -> {
                cards.clear();
                cards.addAll(List.of(
                        new Card(Suit.HEARTS, Rank.TEN),
                        new Card(Suit.SPADES, Rank.FIVE),
                        new Card(Suit.CLUBS, Rank.NINE),
                        new Card(Suit.DIAMONDS, Rank.SIX),
                        new Card(Suit.HEARTS, Rank.SEVEN) // Dealer draws this and stands on 18, terminating the game
                ));
            });

            // Act
            game.playerStand();

            // Assert
            assertThat(game.domainEvents())
                    .as("The domain event registry must capture a GameFinishedEvent instance upon game completion")
                    .anyMatch(event -> event instanceof GameFinishedEvent);
        }
    }
}
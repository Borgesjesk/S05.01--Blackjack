package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.out.mongo;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Document(collection = "blackjack_games")
@TypeAlias("Game")
public class GameDocument {

    @Id
    private String id;

    @Version
    private Long version;

    @CreatedDate
    @Field("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;

    @Indexed
    @Field("game_state")
    private final String gameState;

    @Field("player_name")
    private final String playerName;

    @Field("player_hand")
    private final HandDocument playerHand;

    @Field("dealer_hand")
    private final HandDocument dealerHand;

    @Field("shoe_deck")
    private final List<CardDocument> deck;

    @PersistenceCreator
    public GameDocument(
            String id,
            String gameState,
            String playerName,
            HandDocument playerHand,
            HandDocument dealerHand,
            List<CardDocument> deck
    ) {
        this.id = id;
        this.gameState = gameState;
        this.playerName = playerName;
        this.playerHand = playerHand;
        this.dealerHand = dealerHand;
        this.deck = deck;
    }

    public String getId() {
        return id;
    }

    public String getGameState() {
        return gameState;
    }

    public String getPlayerName() {
        return playerName;
    }

    public HandDocument getPlayerHand() {
        return playerHand;
    }

    public HandDocument getDealerHand() {
        return dealerHand;
    }

    public List<CardDocument> getDeck() {
        return deck;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public static class HandDocument {
        private final List<CardDocument> cards;

        @PersistenceCreator
        public HandDocument(List<CardDocument> cards) {
            this.cards = List.copyOf(cards);
        }

        public List<CardDocument> getCards() {
            return cards;
        }
    }

    public static class CardDocument {
        private final String suit;
        private final String rank;

        @PersistenceCreator
        public CardDocument(String suit, String rank) {
            this.suit = suit;
            this.rank = rank;
        }

        public String getSuit() {
            return suit;
        }

        public String getRank() {
            return rank;
        }
    }
}
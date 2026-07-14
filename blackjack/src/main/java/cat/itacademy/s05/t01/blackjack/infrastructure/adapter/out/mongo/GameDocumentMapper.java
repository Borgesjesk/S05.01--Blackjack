package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.out.mongo;

import cat.itacademy.s05.t01.blackjack.domain.model.Card;
import cat.itacademy.s05.t01.blackjack.domain.model.GameState;
import cat.itacademy.s05.t01.blackjack.domain.model.Rank;
import cat.itacademy.s05.t01.blackjack.domain.model.Suit;
import cat.itacademy.s05.t01.blackjack.domain.model.snapshot.DeckSnapshot;
import cat.itacademy.s05.t01.blackjack.domain.model.snapshot.GameSnapshot;
import cat.itacademy.s05.t01.blackjack.domain.model.snapshot.HandSnapshot;

import java.util.List;

public class GameDocumentMapper {

    private GameDocumentMapper() {
    }

    public static GameDocument toDocument(GameSnapshot snapshot) {
        if (snapshot == null) return null;

        List<GameDocument.CardDocument> playerCards = mapToCardDocuments(snapshot.playerHand().cards());
        List<GameDocument.CardDocument> dealerCards = mapToCardDocuments(snapshot.dealerHand().cards());
        List<GameDocument.CardDocument> deckCards = mapToCardDocuments(snapshot.deck().cards());

        return new GameDocument(
                snapshot.gameId(),
                snapshot.gameState().name(),
                snapshot.playerName(),
                new GameDocument.HandDocument(playerCards),
                new GameDocument.HandDocument(dealerCards),
                deckCards
        );
    }

    public static GameSnapshot toSnapshot(GameDocument document) {
        if (document == null) return null;

        List<Card> playerCards = mapToDomainCards(document.getPlayerHand().getCards());
        List<Card> dealerCards = document.getDealerHand() != null
                ? mapToDomainCards(document.getDealerHand().getCards())
                : List.of();
        List<Card> deckCards = mapToDomainCards(document.getDeck());

        return new GameSnapshot(
                document.getId(),
                new DeckSnapshot(deckCards),
                new HandSnapshot(playerCards),
                new HandSnapshot(dealerCards),
                GameState.valueOf(document.getGameState()),
                document.getCreatedAt(),
                document.getPlayerName()
        );
    }

    private static List<GameDocument.CardDocument> mapToCardDocuments(List<Card> cards) {
        if (cards == null) return List.of();
        return cards.stream()
                .map(c -> new GameDocument.CardDocument(c.suit().name(), c.rank().name()))
                .toList();
    }

    private static List<Card> mapToDomainCards(List<GameDocument.CardDocument> cardDocs) {
        if (cardDocs == null) return List.of();
        return cardDocs.stream()
                .map(doc -> new Card(Suit.valueOf(doc.getSuit()), Rank.valueOf(doc.getRank())))
                .toList();
    }
}
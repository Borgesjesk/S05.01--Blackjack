package cat.itacademy.s05.t01.blackjack.application.dto;

import cat.itacademy.s05.t01.blackjack.domain.model.Card;
import cat.itacademy.s05.t01.blackjack.domain.model.GameState;
import cat.itacademy.s05.t01.blackjack.domain.model.Hand;
import cat.itacademy.s05.t01.blackjack.domain.model.snapshot.GameSnapshot;

import java.util.List;

public record GameResponseDTO(
        String gameId,
        String playerName,
        GameState status,
        HandDTO playerHand,
        DealerHandDTO dealerHand
) {
    public record HandDTO(List<CardDTO> cards, int score) {}

    public record DealerHandDTO(List<CardDTO> cards, int visibleScore) {}

    public record CardDTO(String suit, String rank) {}

    public static GameResponseDTO fromDomainSnapshot(GameSnapshot snapshot) {
        List<Card> playerDomainCards = snapshot.playerHand().cards();
        int playerScore = new Hand(playerDomainCards).score();
        HandDTO playerHandMapped = new HandDTO(mapCardsToDTO(playerDomainCards), playerScore);

        DealerHandDTO dealerHandMapped;
        List<Card> dealerDomainCards = snapshot.dealerHand().cards();
        if (snapshot.gameState() == GameState.PLAYER_TURN) {
            Card upcard = dealerDomainCards.get(0);
            int visibleScore = new Hand(List.of(upcard)).score();
            dealerHandMapped = new DealerHandDTO(mapCardsToDTO(List.of(upcard)), visibleScore);
        } else {
            int finalDealerScore = new Hand(dealerDomainCards).score();
            dealerHandMapped = new DealerHandDTO(mapCardsToDTO(dealerDomainCards), finalDealerScore);
        }

        return new GameResponseDTO(
                snapshot.gameId(),
                snapshot.playerName(),
                snapshot.gameState(),
                playerHandMapped,
                dealerHandMapped
        );
    }

    private static List<CardDTO> mapCardsToDTO(List<Card> cards) {
        return cards.stream()
                .map(c -> new CardDTO(c.suit().name(), c.rank().name()))
                .toList();
    }
}
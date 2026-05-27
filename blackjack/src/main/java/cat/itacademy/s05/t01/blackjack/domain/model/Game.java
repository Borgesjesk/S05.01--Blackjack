package cat.itacademy.s05.t01.blackjack.domain.model;

import cat.itacademy.s05.t01.blackjack.domain.event.GameFinishedEvent;
import cat.itacademy.s05.t01.blackjack.domain.model.snapshot.DeckSnapshot;
import cat.itacademy.s05.t01.blackjack.domain.model.snapshot.GameSnapshot;
import cat.itacademy.s05.t01.blackjack.domain.model.snapshot.HandSnapshot;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class Game {

    private static final int DEALER_STAND_THRESHOLD = 17;

    private final String id;
    private final Deck deck;
    private final Hand playerHand;
    private final Hand dealerHand;
    private final Instant createdAt;
    private GameState state;

    private final List<Object> domainEvents = new ArrayList<>();

    private Game(String id, Deck deck, Hand playerHand, Hand dealerHand,
                 GameState state, Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.deck = Objects.requireNonNull(deck);
        this.playerHand = Objects.requireNonNull(playerHand);
        this.dealerHand = Objects.requireNonNull(dealerHand);
        this.state = Objects.requireNonNull(state);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    // --- Factory: new game ---

    public static Game start(Consumer<List<Card>> shuffleStrategy) {
        Objects.requireNonNull(shuffleStrategy, "Shuffle strategy must not be null");

        Deck deck = Deck.createShuffled(shuffleStrategy);
        Hand playerHand = new Hand();
        Hand dealerHand = new Hand();

        // Deal initial hands: player-dealer-player-dealer
        playerHand.addCard(deck.draw());
        dealerHand.addCard(deck.draw());
        playerHand.addCard(deck.draw());
        dealerHand.addCard(deck.draw());

        Game game = new Game(
                UUID.randomUUID().toString(),
                deck,
                playerHand,
                dealerHand,
                GameState.PLAYER_TURN,
                Instant.now()
        );

        game.checkInitialBlackjack();
        return game;
    }

    // --- Factory: reconstitute from persistence ---

    public static Game reconstitute(String id, Deck deck, Hand playerHand,
                                    Hand dealerHand, GameState state,
                                    Instant createdAt) {
        return new Game(id, deck, playerHand, dealerHand, state, createdAt);
    }

    public void playerHit() {
        assertState(GameState.PLAYER_TURN, "Player can only hit during their turn");
        playerHand.addCard(deck.draw());

        if (playerHand.isBust()) {
            finishGame(GameState.PLAYER_BUST);
        }
    }

    public void playerStand() {
        assertState(GameState.PLAYER_TURN, "Player can only stand during their turn");
        state = GameState.DEALER_TURN;
        resolveDealerTurn();
    }

    // --- Dealer logic ---

    private void resolveDealerTurn() {
        while (dealerHand.score() < DEALER_STAND_THRESHOLD) {
            dealerHand.addCard(deck.draw());
        }

        if (dealerHand.isBust()) {
            finishGame(GameState.DEALER_BUST);
        } else {
            resolveWinner();
        }
    }

    private void resolveWinner() {
        int playerScore = playerHand.score();
        int dealerScore = dealerHand.score();

        if (playerScore > dealerScore) {
            finishGame(GameState.PLAYER_WINS);
        } else if (dealerScore > playerScore) {
            finishGame(GameState.DEALER_WINS);
        } else {
            finishGame(GameState.PUSH);
        }
    }

    // --- Initial blackjack check ---

    private void checkInitialBlackjack() {
        boolean playerBj = playerHand.isBlackjack();
        boolean dealerBj = dealerHand.isBlackjack();

        if (playerBj && dealerBj) {
            finishGame(GameState.PUSH);
        } else if (playerBj) {
            finishGame(GameState.PLAYER_BLACKJACK);
        } else if (dealerBj) {
            finishGame(GameState.DEALER_WINS);
        }
    }

    // --- Domain event management ---

    private void finishGame(GameState finalState) {
        this.state = finalState;
        domainEvents.add(new GameFinishedEvent(
                id, finalState, playerHand.score(), dealerHand.score(), Instant.now()
        ));
    }

    public List<Object> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    // --- State guards ---

    private void assertState(GameState expected, String message) {
        if (this.state != expected) {
            throw new IllegalStateException(message + ". Current state: " + state);
        }
    }

    public boolean isFinished() {
        return state != GameState.PLAYER_TURN && state != GameState.DEALER_TURN;
    }

    // --- Getters ---

    public String id() {
        return id;
    }

    public Hand playerHand() {
        return playerHand;
    }

    public Hand dealerHand() {
        return dealerHand;
    }

    public Deck deck() {
        return deck;
    }

    public GameState state() {
        return state;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public GameSnapshot toSnapshot() {
        return new GameSnapshot(
                this.id,
                new DeckSnapshot(this.deck.cards()),
                new HandSnapshot(this.playerHand.cards()),
                new HandSnapshot(this.dealerHand.cards()),
                this.state,
                this.createdAt
        );
    }
}
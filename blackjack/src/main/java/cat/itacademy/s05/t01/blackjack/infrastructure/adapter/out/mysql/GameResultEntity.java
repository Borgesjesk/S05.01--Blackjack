package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.out.mysql;

import cat.itacademy.s05.t01.blackjack.domain.model.GameState;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(
        name = "game_results",
        indexes = {
                @Index(name = "idx_player_name", columnList = "player_name"),
                @Index(name = "idx_result", columnList = "result")
        }
)
public class GameResultEntity {

    @Id
    @Column(name = "game_id", length = 36, nullable = false, updatable = false)
    private String gameId;

    @Column(name = "player_name", length = 100, nullable = false, updatable = false)
    private String playerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", length = 20, nullable = false, updatable = false)
    private GameState result;

    @Column(name = "player_score", nullable = false, updatable = false)
    private int playerScore;

    @Column(name = "dealer_score", nullable = false, updatable = false)
    private int dealerScore;

    @Column(name = "finished_at", nullable = false, updatable = false)
    private Instant finishedAt;

    protected GameResultEntity() {
    }

    public GameResultEntity(String gameId, String playerName, GameState result,
                            int playerScore, int dealerScore, Instant finishedAt) {
        this.gameId = Objects.requireNonNull(gameId);
        this.playerName = Objects.requireNonNull(playerName);
        this.result = Objects.requireNonNull(result);
        this.playerScore = playerScore;
        this.dealerScore = dealerScore;
        this.finishedAt = Objects.requireNonNull(finishedAt);
    }

    public String getGameId() {
        return gameId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public GameState getResult() {
        return result;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getDealerScore() {
        return dealerScore;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }
}
package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.out.event;

import cat.itacademy.s05.t01.blackjack.domain.event.GameFinishedEvent;
import cat.itacademy.s05.t01.blackjack.domain.model.GameState;
import cat.itacademy.s05.t01.blackjack.domain.port.RankingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GameFinishedEventListenerTest {

    private RankingRepository rankingRepository;
    private GameFinishedEventListener listener;

    @BeforeEach
    void setUp() {
        rankingRepository = mock(RankingRepository.class);
        listener = new GameFinishedEventListener(rankingRepository);
    }

    @Test
    @DisplayName("Persists a GameResult with all event fields")
    void onGameFinished_persistsResult() {
        Instant finishedAt = Instant.now();
        GameFinishedEvent event = new GameFinishedEvent(
                "game-1", "Jess", GameState.PLAYER_WINS, 21, 18, finishedAt
        );

        listener.onGameFinished(event);

        ArgumentCaptor<RankingRepository.GameResult> captor =
                ArgumentCaptor.forClass(RankingRepository.GameResult.class);
        verify(rankingRepository).saveGameResult(captor.capture());

        RankingRepository.GameResult saved = captor.getValue();
        assertThat(saved.gameId()).isEqualTo("game-1");
        assertThat(saved.playerName()).isEqualTo("Jess");
        assertThat(saved.result()).isEqualTo("PLAYER_WINS");
        assertThat(saved.playerScore()).isEqualTo(21);
        assertThat(saved.dealerScore()).isEqualTo(18);
        assertThat(saved.finishedAt()).isEqualTo(finishedAt);
    }
}
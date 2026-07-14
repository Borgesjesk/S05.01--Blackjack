package cat.itacademy.s05.t01.blackjack.application.usecase;

import cat.itacademy.s05.t01.blackjack.application.dto.GameResponseDTO;
import cat.itacademy.s05.t01.blackjack.application.exception.GameNotFoundException;
import cat.itacademy.s05.t01.blackjack.domain.model.Game;
import cat.itacademy.s05.t01.blackjack.domain.model.GameState;
import cat.itacademy.s05.t01.blackjack.domain.port.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlayerStandUseCaseTest {

    private GameRepository gameRepository;
    private PlayerStandUseCase useCase;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        useCase = new PlayerStandUseCase(gameRepository);
    }

    @Test
    @DisplayName("Resolves the game after player stands")
    void stand_happyPath() {
        Game game = Game.start(cards -> Collections.shuffle(cards));
        when(gameRepository.findById(game.id())).thenReturn(Optional.of(game));

        GameResponseDTO response = useCase.execute(game.id());

        assertThat(response.status()).isNotIn(GameState.PLAYER_TURN, GameState.DEALER_TURN);
        verify(gameRepository).save(game);
    }

    @Test
    @DisplayName("Throws GameNotFoundException when game does not exist")
    void stand_gameNotFound() {
        when(gameRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute("missing"))
                .isInstanceOf(GameNotFoundException.class);

        verify(gameRepository, never()).save(any());
    }
}
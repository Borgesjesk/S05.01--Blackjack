package cat.itacademy.s05.t01.blackjack.application.usecase;

import cat.itacademy.s05.t01.blackjack.application.dto.GameResponseDTO;
import cat.itacademy.s05.t01.blackjack.application.exception.GameNotFoundException;
import cat.itacademy.s05.t01.blackjack.domain.model.Game;
import cat.itacademy.s05.t01.blackjack.domain.port.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GetGameUseCaseTest {

    private GameRepository gameRepository;
    private GetGameUseCase useCase;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        useCase = new GetGameUseCase(gameRepository);
    }

    @Test
    @DisplayName("Returns game DTO when game exists")
    void get_happyPath() {
        Game game = Game.start(cards -> Collections.shuffle(cards));
        when(gameRepository.findById(game.id())).thenReturn(Optional.of(game));

        GameResponseDTO response = useCase.execute(game.id());

        assertThat(response.gameId()).isEqualTo(game.id());
    }

    @Test
    @DisplayName("Throws GameNotFoundException when game does not exist")
    void get_gameNotFound() {
        when(gameRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute("missing"))
                .isInstanceOf(GameNotFoundException.class);
    }
}
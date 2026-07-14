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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RenamePlayerUseCaseTest {

    private GameRepository gameRepository;
    private RenamePlayerUseCase useCase;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        useCase = new RenamePlayerUseCase(gameRepository);
    }

    @Test
    @DisplayName("Renames player and saves game when game exists")
    void rename_happyPath() {
        Game game = Game.start(cards -> Collections.shuffle(cards));
        when(gameRepository.findById(game.id())).thenReturn(Optional.of(game));

        GameResponseDTO response = useCase.execute(game.id(), "Jess");

        assertThat(response.playerName()).isEqualTo("Jess");
        verify(gameRepository).save(game);
    }

    @Test
    @DisplayName("Throws GameNotFoundException when game does not exist")
    void rename_gameNotFound() {
        when(gameRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute("missing", "Jess"))
                .isInstanceOf(GameNotFoundException.class);

        verify(gameRepository, never()).save(any());
    }

    @Test
    @DisplayName("Throws IllegalArgumentException when name is blank")
    void rename_blankName() {
        Game game = Game.start(cards -> Collections.shuffle(cards));
        when(gameRepository.findById(game.id())).thenReturn(Optional.of(game));

        assertThatThrownBy(() -> useCase.execute(game.id(), "  "))
                .isInstanceOf(IllegalArgumentException.class);

        verify(gameRepository, never()).save(any());
    }

    @Test
    @DisplayName("Throws IllegalArgumentException when name is null")
    void rename_nullName() {
        Game game = Game.start(cards -> Collections.shuffle(cards));
        when(gameRepository.findById(game.id())).thenReturn(Optional.of(game));

        assertThatThrownBy(() -> useCase.execute(game.id(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
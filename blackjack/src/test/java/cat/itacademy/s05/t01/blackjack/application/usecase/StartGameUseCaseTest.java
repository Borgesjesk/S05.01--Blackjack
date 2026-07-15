package cat.itacademy.s05.t01.blackjack.application.usecase;

import cat.itacademy.s05.t01.blackjack.application.dto.GameResponseDTO;
import cat.itacademy.s05.t01.blackjack.domain.model.Game;
import cat.itacademy.s05.t01.blackjack.domain.port.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class StartGameUseCaseTest {

    private GameRepository gameRepository;
    private StartGameUseCase useCase;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        useCase = new StartGameUseCase(gameRepository);
    }

    @Test
    @DisplayName("Creates a new game with default player name Anonymous")
    void start_createsGameWithDefaultName() {
        GameResponseDTO response = useCase.execute(null);

        assertThat(response.gameId()).isNotBlank();
        assertThat(response.playerName()).isEqualTo("Anonymous");
    }

    @Test
    @DisplayName("Persists the game via repository")
    void start_savesGame() {
        useCase.execute(null);
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    @DisplayName("Deals two cards to the player initially")
    void start_dealsTwoCardsToPlayer() {
        GameResponseDTO response = useCase.execute(null);

        assertThat(response.playerHand().cards()).hasSize(2);
    }
}
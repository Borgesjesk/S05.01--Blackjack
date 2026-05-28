package cat.itacademy.s05.t01.blackjack.application.exception;

import cat.itacademy.s05.t01.blackjack.application.dto.GameResponseDTO;
import cat.itacademy.s05.t01.blackjack.domain.model.Game;
import cat.itacademy.s05.t01.blackjack.domain.port.GameRepository;

public class GetGameUseCase {

    private final GameRepository gameRepository;

    public GetGameUseCase(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameResponseDTO execute(String gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        return GameResponseDTO.fromDomainSnapshot(game.toSnapshot());
    }
}
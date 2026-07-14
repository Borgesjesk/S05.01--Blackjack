package cat.itacademy.s05.t01.blackjack.application.usecase;

import cat.itacademy.s05.t01.blackjack.application.dto.GameResponseDTO;
import cat.itacademy.s05.t01.blackjack.application.exception.GameNotFoundException;
import cat.itacademy.s05.t01.blackjack.domain.model.Game;
import cat.itacademy.s05.t01.blackjack.domain.port.GameRepository;

public class PlayerStandUseCase {

    private final GameRepository gameRepository;

    public PlayerStandUseCase(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameResponseDTO execute(String gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        game.playerStand();
        gameRepository.save(game);
        return GameResponseDTO.fromDomainSnapshot(game.toSnapshot());
    }
}
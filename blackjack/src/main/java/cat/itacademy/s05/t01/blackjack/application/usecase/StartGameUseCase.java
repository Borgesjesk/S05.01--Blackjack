package cat.itacademy.s05.t01.blackjack.application.usecase;

import cat.itacademy.s05.t01.blackjack.application.dto.GameResponseDTO;
import cat.itacademy.s05.t01.blackjack.domain.model.Game;
import cat.itacademy.s05.t01.blackjack.domain.port.GameRepository;

import java.util.Collections;

public class StartGameUseCase {

    private final GameRepository gameRepository;

    public StartGameUseCase(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameResponseDTO execute(String playerName) {
        Game game = Game.start(cards -> Collections.shuffle(cards));
        if (playerName != null && !playerName.isBlank()) {
            game.renamePlayer(playerName);
        }
        gameRepository.save(game);
        return GameResponseDTO.fromDomainSnapshot(game.toSnapshot());
    }
}
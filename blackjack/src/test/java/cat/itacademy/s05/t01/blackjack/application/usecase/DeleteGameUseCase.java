package cat.itacademy.s05.t01.blackjack.application.usecase;

import cat.itacademy.s05.t01.blackjack.application.exception.GameNotFoundException;
import cat.itacademy.s05.t01.blackjack.domain.port.GameRepository;

public class DeleteGameUseCase {

    private final GameRepository gameRepository;

    public DeleteGameUseCase(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void execute(String gameId) {
        if (!gameRepository.findById(gameId).isPresent()) {
            throw new GameNotFoundException(gameId);
        }
        gameRepository.deleteById(gameId);
    }
}
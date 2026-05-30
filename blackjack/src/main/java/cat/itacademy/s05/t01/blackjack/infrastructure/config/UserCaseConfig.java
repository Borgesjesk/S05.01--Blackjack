package cat.itacademy.s05.t01.blackjack.infrastructure.config;

import cat.itacademy.s05.t01.blackjack.application.usecase.GetGameUseCase;
import cat.itacademy.s05.t01.blackjack.application.usecase.PlayerHitUseCase;
import cat.itacademy.s05.t01.blackjack.application.usecase.PlayerStandUseCase;
import cat.itacademy.s05.t01.blackjack.application.usecase.StartGameUseCase;
import cat.itacademy.s05.t01.blackjack.domain.port.GameRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserCaseConfig {

    @Bean
    public StartGameUseCase startGameUseCase(GameRepository gameRepository) {
        return new StartGameUseCase(gameRepository);
    }

    @Bean
    public PlayerHitUseCase playerHitUseCase(GameRepository gameRepository) {
        return new PlayerHitUseCase(gameRepository);
    }

    @Bean
    public PlayerStandUseCase playerStandUseCase(GameRepository gameRepository) {
        return new PlayerStandUseCase(gameRepository);
    }

    @Bean
    public GetGameUseCase getGameUseCase(GameRepository gameRepository) {
        return new GetGameUseCase(gameRepository);
    }
}
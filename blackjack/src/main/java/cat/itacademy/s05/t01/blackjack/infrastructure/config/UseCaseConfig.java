package cat.itacademy.s05.t01.blackjack.infrastructure.config;

import cat.itacademy.s05.t01.blackjack.application.usecase.*;
import cat.itacademy.s05.t01.blackjack.domain.port.GameRepository;
import cat.itacademy.s05.t01.blackjack.domain.port.RankingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

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

    @Bean
    public RenamePlayerUseCase renamePlayerUseCase(GameRepository gameRepository) {
        return new RenamePlayerUseCase(gameRepository);
    }

    @Bean
    public GetRankingUseCase getRankingUseCase(RankingRepository rankingRepository) {
        return new GetRankingUseCase(rankingRepository);
    }
}
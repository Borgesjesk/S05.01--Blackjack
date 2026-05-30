package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.out.mysql;

import cat.itacademy.s05.t01.blackjack.domain.model.GameState;
import cat.itacademy.s05.t01.blackjack.domain.port.RankingRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MySqlRankingRepositoryAdapter implements RankingRepository {

    private final SpringDataGameResultRepository repository;

    public MySqlRankingRepositoryAdapter(SpringDataGameResultRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveGameResult(GameResult result) {
        GameResultEntity entity = new GameResultEntity(
                result.gameId(),
                GameState.valueOf(result.result()),
                result.playerScore(),
                result.dealerScore(),
                result.finishedAt()
        );
        repository.save(entity);
    }

    @Override
    public List<GameResult> findTopPlayers(int limit) {
        return repository.findTopWinners(PageRequest.of(0, limit)).stream()
                .map(entity -> new GameResult(
                        entity.getGameId(),
                        entity.getResult().name(),
                        entity.getPlayerScore(),
                        entity.getDealerScore(),
                        entity.getFinishedAt()
                ))
                .toList();
    }
}
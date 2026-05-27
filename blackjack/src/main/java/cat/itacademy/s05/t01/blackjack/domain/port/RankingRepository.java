package cat.itacademy.s05.t01.blackjack.domain.port;

import java.util.List;

public interface RankingRepository {

    void saveGameResult(GameResult result);

    List<GameResult> findTopPlayers(int limit);

    record GameResult(
            String gameId,
            String result,
            int playerScore,
            int dealerScore,
            java.time.Instant finishedAt
    ) {}
}

package cat.itacademy.s05.t01.blackjack.domain.port;

import java.time.Instant;
import java.util.List;

public interface RankingRepository {

    void saveGameResult(GameResult result);

    List<PlayerRanking> findRanking(int limit);

    record GameResult(
            String gameId,
            String playerName,
            String result,
            int playerScore,
            int dealerScore,
            Instant finishedAt
    ) {}

    record PlayerRanking(
            String playerName,
            long wins,
            long gamesPlayed
    ) {}
}
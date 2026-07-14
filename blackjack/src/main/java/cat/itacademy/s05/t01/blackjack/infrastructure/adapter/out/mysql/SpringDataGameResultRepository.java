package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.out.mysql;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringDataGameResultRepository extends JpaRepository<GameResultEntity, String> {

    @Query("""
            SELECT g.playerName as playerName,
                   SUM(CASE WHEN g.result IN (cat.itacademy.s05.t01.blackjack.domain.model.GameState.PLAYER_WINS,
                                              cat.itacademy.s05.t01.blackjack.domain.model.GameState.PLAYER_BLACKJACK,
                                              cat.itacademy.s05.t01.blackjack.domain.model.GameState.DEALER_BUST)
                            THEN 1 ELSE 0 END) as wins,
                   COUNT(g) as gamesPlayed
            FROM GameResultEntity g
            GROUP BY g.playerName
            ORDER BY wins DESC, gamesPlayed ASC
            """)
    List<PlayerRankingProjection> findRanking(Pageable pageable);
}
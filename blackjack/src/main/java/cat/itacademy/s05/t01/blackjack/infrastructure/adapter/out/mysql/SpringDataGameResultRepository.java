package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.out.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SpringDataGameResultRepository extends JpaRepository<GameResultEntity, String> {

    @Query("SELECT g FROM GameResultEntity g WHERE g.result = 'PLAYER_WINS' OR g.result = 'PLAYER_BLACKJACK' ORDER BY g.playerScore DESC, g.finishedAt ASC")
    List<GameResultEntity> findTopWinners(Pageable pageable);
}

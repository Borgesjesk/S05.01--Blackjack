package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.out.event;

import cat.itacademy.s05.t01.blackjack.domain.event.GameFinishedEvent;
import cat.itacademy.s05.t01.blackjack.domain.port.RankingRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GameFinishedEventListener {

    private final RankingRepository rankingRepository;

    public GameFinishedEventListener(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    @EventListener
    public void onGameFinished(GameFinishedEvent event) {
        rankingRepository.saveGameResult(new RankingRepository.GameResult(
                event.gameId(),
                event.playerName(),
                event.result().name(),
                event.playerScore(),
                event.dealerScore(),
                event.finishedAt()
        ));
    }
}
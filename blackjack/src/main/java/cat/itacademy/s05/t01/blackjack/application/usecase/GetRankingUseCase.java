package cat.itacademy.s05.t01.blackjack.application.usecase;

import cat.itacademy.s05.t01.blackjack.application.dto.RankingEntryDTO;
import cat.itacademy.s05.t01.blackjack.domain.port.RankingRepository;

import java.util.List;

public class GetRankingUseCase {

    private static final int DEFAULT_LIMIT = 10;

    private final RankingRepository rankingRepository;

    public GetRankingUseCase(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    public List<RankingEntryDTO> execute(Integer limit) {
        int safeLimit = (limit == null || limit <= 0) ? DEFAULT_LIMIT : limit;
        return rankingRepository.findRanking(safeLimit).stream()
                .map(p -> new RankingEntryDTO(p.playerName(), p.wins(), p.gamesPlayed()))
                .toList();
    }
}
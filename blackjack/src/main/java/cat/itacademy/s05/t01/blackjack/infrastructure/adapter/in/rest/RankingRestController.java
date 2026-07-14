package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.in.rest;

import cat.itacademy.s05.t01.blackjack.application.dto.RankingEntryDTO;
import cat.itacademy.s05.t01.blackjack.application.usecase.GetRankingUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ranking")
@Tag(name = "Ranking", description = "Player leaderboard endpoints")
public class RankingRestController {

    private final GetRankingUseCase getRankingUseCase;

    public RankingRestController(GetRankingUseCase getRankingUseCase) {
        this.getRankingUseCase = getRankingUseCase;
    }

    @GetMapping
    @Operation(summary = "Get leaderboard", description = "Returns the top players sorted by number of wins.")
    public ResponseEntity<List<RankingEntryDTO>> getRanking(
            @Parameter(description = "Maximum number of players to return (default 10)")
            @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(getRankingUseCase.execute(limit));
    }
}
package cat.itacademy.s05.t01.blackjack.application.usecase;

import cat.itacademy.s05.t01.blackjack.application.dto.RankingEntryDTO;
import cat.itacademy.s05.t01.blackjack.domain.port.RankingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GetRankingUseCaseTest {

    private RankingRepository rankingRepository;
    private GetRankingUseCase useCase;

    @BeforeEach
    void setUp() {
        rankingRepository = mock(RankingRepository.class);
        useCase = new GetRankingUseCase(rankingRepository);
    }

    @Test
    @DisplayName("Returns ranking mapped to DTOs")
    void get_happyPath() {
        when(rankingRepository.findRanking(10)).thenReturn(List.of(
                new RankingRepository.PlayerRanking("Jess", 5, 8),
                new RankingRepository.PlayerRanking("Bot", 2, 6)
        ));

        List<RankingEntryDTO> result = useCase.execute(null);

        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(new RankingEntryDTO("Jess", 5, 8));
        assertThat(result.get(1)).isEqualTo(new RankingEntryDTO("Bot", 2, 6));
    }

    @Test
    @DisplayName("Applies default limit of 10 when limit is null")
    void get_defaultLimit_null() {
        useCase.execute(null);
        verify(rankingRepository).findRanking(eq(10));
    }

    @Test
    @DisplayName("Applies default limit of 10 when limit is zero or negative")
    void get_defaultLimit_negative() {
        useCase.execute(0);
        useCase.execute(-5);
        verify(rankingRepository, times(2)).findRanking(eq(10));
    }

    @Test
    @DisplayName("Uses provided limit when positive")
    void get_customLimit() {
        useCase.execute(25);
        verify(rankingRepository).findRanking(eq(25));
    }

    @Test
    @DisplayName("Returns empty list when repository has no results")
    void get_empty() {
        when(rankingRepository.findRanking(10)).thenReturn(List.of());

        List<RankingEntryDTO> result = useCase.execute(null);

        assertThat(result).isEmpty();
    }
}
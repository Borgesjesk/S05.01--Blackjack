package cat.itacademy.s05.t01.blackjack.application.dto;

public record RankingEntryDTO(
        String playerName,
        long wins,
        long gamesPlayed
) {}
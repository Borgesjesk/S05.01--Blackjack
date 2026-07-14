package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.out.mysql;

public interface PlayerRankingProjection {
    String getPlayerName();
    long getWins();
    long getGamesPlayed();
}
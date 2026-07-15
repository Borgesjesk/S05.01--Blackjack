package cat.itacademy.s05.t01.blackjack.domain.port;

import cat.itacademy.s05.t01.blackjack.domain.model.Game;

import java.util.Optional;

public interface GameRepository {
    void save(Game game);

    Optional<Game> findById(String id);

    void deleteById(String id);
}
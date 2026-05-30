package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.out.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataGameResultRepository extends JpaRepository<GameResultEntity, String> {
}

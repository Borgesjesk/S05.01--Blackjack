package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.out.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataMongoGameRepository extends MongoRepository<GameDocument, String> {
}
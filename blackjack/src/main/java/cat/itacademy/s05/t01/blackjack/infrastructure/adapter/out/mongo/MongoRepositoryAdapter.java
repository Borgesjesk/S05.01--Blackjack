package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.out.mongo;

import cat.itacademy.s05.t01.blackjack.domain.model.Game;
import cat.itacademy.s05.t01.blackjack.domain.port.GameRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MongoRepositoryAdapter implements GameRepository {

    private final SpringDataMongoGameRepository repository;
    private final MongoTemplate mongoTemplate;
    private final ApplicationEventPublisher eventPublisher;

    public MongoRepositoryAdapter(SpringDataMongoGameRepository repository,
                                  MongoTemplate mongoTemplate,
                                  ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void save(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Persistence aborted: Domain aggregate cannot be null.");
        }
        var snapshot = game.toSnapshot();
        GameDocument document = GameDocumentMapper.toDocument(snapshot);

        repository.findById(document.getId()).ifPresentOrElse(
                existing -> mongoTemplate.save(document),
                () -> repository.insert(document)
        );

        game.domainEvents().forEach(eventPublisher::publishEvent);
        game.clearDomainEvents();
    }

    @Override
    public Optional<Game> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return repository.findById(id)
                .map(GameDocumentMapper::toSnapshot)
                .map(Game::fromSnapshot);
    }
}
package br.com.company.infrastructure.persistence;

import br.com.company.core.domain.model.Message;
import br.com.company.core.domain.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository Implementation (Adapter) - Infrastructure Layer
 * This is an ADAPTER in Hexagonal Architecture
 *
 * Implements the PORT (interface) defined in Domain layer
 * This layer CAN depend on frameworks and external libraries
 */
@Repository
public class InMemoryMessageRepository implements MessageRepository {

    private final Map<String, Message> storage = new ConcurrentHashMap<>();

    @Override
    public Message save(Message message) {
        storage.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }
}

